package com.example

import io.opentracing.{SpanContext, Tracer}
import io.opentracing.contrib.kafka.{TracingKafkaConsumer, TracingKafkaProducer, TracingKafkaUtils}
import io.opentracing.util.GlobalTracer
import org.apache.kafka.clients.consumer.{ConsumerRecord, KafkaConsumer}
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}
import io.jaegertracing.Configuration
import io.jaegertracing.Configuration.{ReporterConfiguration, SamplerConfiguration, SenderConfiguration}
import io.jaegertracing.internal.samplers.ConstSampler
import org.apache.kafka.common.header.{Header, Headers}

import java.nio.charset.StandardCharsets
import java.util.Map
import scala.jdk.CollectionConverters._

class TracingInitSpec extends SpecBase {

  val service: String = this.suiteName

   val samplerConfig: SamplerConfiguration = new SamplerConfiguration().withType(ConstSampler.TYPE).withParam(1) //("const", 1)
   val reporterConfig: ReporterConfiguration = new ReporterConfiguration().withLogSpans(true)
     .withFlushInterval(1000)
     .withMaxQueueSize(10000)
     .withSender(SenderConfiguration.fromEnv()
       .withAgentHost("localhost") // default
       .withAgentPort(6831)) // default
   val config: Configuration = new Configuration(service).withSampler(samplerConfig).withReporter(reporterConfig)  //, samplerConfig, reporterConfig)

  // Get the actual OpenTracing-compatible Tracer.
  val tracer: Tracer = config.getTracer()
  // val tracer: Tracer = GlobalTracer.get() //noop tracer
  GlobalTracer.registerIfAbsent(tracer)

  "must create and read msgs with spans" in {

    val topicName = "perftest"
    KafkaSpecHelper.truncateTopic(adminClient, topicName, 6, 3)

    val producer: KafkaProducer[String, String]  = new KafkaProducer[String, String](props)
    val tracingProducer: TracingKafkaProducer[String, String] = new TracingKafkaProducer(producer,
      tracer)

    val record: ProducerRecord[String, String] = new ProducerRecord[String, String](topicName, "key", "value")

    tracingProducer.send(record)

    val consumer: KafkaConsumer[String, String] = new KafkaConsumer[String, String](props)
    val tracingConsumer: TracingKafkaConsumer[String, String] = new TracingKafkaConsumer(consumer,
      tracer)
    tracingConsumer.subscribe(List(topicName).asJava);

    val records: Iterable[ConsumerRecord[String, String]] = KafkaSpecHelper.fetchAndProcessTraceRecords(tracingConsumer)

    val headers: Headers = records.head.headers()
    println("record headers: ")
    println(headers)
    val traceHeaders: Iterable[Header] = headers.headers("uber-trace-id").asScala

    traceHeaders foreach { th =>
      println(s"trace header: ${th.key()} :${new String(th.value(), StandardCharsets.UTF_8)}")
    }

    val spanContext: SpanContext = TracingKafkaUtils.extractSpanContext(headers, tracer)
    val bagItems: Iterable[Map.Entry[String, String]] = spanContext.baggageItems().asScala

    println(s"spanContext: $spanContext | ${spanContext.toSpanId} : ${spanContext.toTraceId} ")
    println("bag items: ")
    bagItems foreach println
  }

}
