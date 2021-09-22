package com.example

import io.opentracing.{Span, SpanContext, Tracer}
import io.opentracing.contrib.kafka.{TracingKafkaConsumer, TracingKafkaProducer, TracingKafkaUtils}
import io.opentracing.util.GlobalTracer
import org.apache.kafka.clients.consumer.{ConsumerRecord, KafkaConsumer}
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}
import io.jaegertracing.Configuration
import io.jaegertracing.Configuration.{ReporterConfiguration, SamplerConfiguration, SenderConfiguration}
import io.jaegertracing.internal.samplers.ConstSampler
import org.apache.kafka.common.header.{Header, Headers}

import java.nio.charset.StandardCharsets
import scala.jdk.CollectionConverters._

class DecoratedClientsTracingSpec extends SpecBase {

   val samplerConfig: SamplerConfiguration = new SamplerConfiguration().withType(ConstSampler.TYPE).withParam(1)
   val reporterConfig: ReporterConfiguration = new ReporterConfiguration().withLogSpans(true)
     .withFlushInterval(1000)
     .withMaxQueueSize(10000)
     .withSender(SenderConfiguration.fromEnv()
       .withAgentHost("localhost") // default
       .withAgentPort(6831)) // default
   val config: Configuration = new Configuration(suiteName).withSampler(samplerConfig).withReporter(reporterConfig)  //, samplerConfig, reporterConfig)

  val tracer: Tracer = config.getTracer
  // val tracer: Tracer = GlobalTracer.get() //noop tracer
  GlobalTracer.registerIfAbsent(tracer)

  val topicName = s"tracetest-$suiteName"

  "must create spans while producing and consuming" in {

    KafkaSpecHelper.truncateTopic(adminClient, topicName, 6, 3)

    val producer: KafkaProducer[String, String]  = new KafkaProducer[String, String](props)
    val tracingProducer: TracingKafkaProducer[String, String] = new TracingKafkaProducer(producer,
      tracer)

    // wrapping span
    val builder: Tracer.SpanBuilder = tracer.buildSpan("testing")
    val r: Span = builder.start()
    tracer.activateSpan(r)
    r.log("stuff happened!")

    val recordKey = "key"
    val recordValue = "value"
    r.setTag("recordKey", recordKey)
    r.setTag("recordValue", recordValue)
    val record: ProducerRecord[String, String] = new ProducerRecord[String, String](topicName, recordKey, recordValue)
    tracingProducer.send(record)

    val consumer: KafkaConsumer[String, String] = new KafkaConsumer[String, String](props)
    val tracingConsumer: TracingKafkaConsumer[String, String] = new TracingKafkaConsumer(consumer,
      tracer)
    tracingConsumer.subscribe(List(topicName).asJava)
    println(s"active span: ${tracer.activeSpan()}")
    val records: Iterable[ConsumerRecord[String, String]] = KafkaSpecHelper.fetchAndProcessTraceRecords(tracingConsumer)
    val headers: Headers = records.head.headers()
    val traceHeaders: Iterable[Header] = headers.headers("uber-trace-id").asScala
    println(s"active span: ${tracer.activeSpan()}")
    traceHeaders foreach { th =>
      println(s"trace header: ${th.key()} :${new String(th.value(), StandardCharsets.UTF_8)}")
    }

    val spanContext: SpanContext = TracingKafkaUtils.extractSpanContext(headers, tracer)
    println(s"spanContext: $spanContext | ${spanContext.toSpanId} : ${spanContext.toTraceId} ")

    r.finish()
  }

}
