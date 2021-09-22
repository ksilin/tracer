package com.example

import io.jaegertracing.Configuration
import io.jaegertracing.Configuration.{ReporterConfiguration, SamplerConfiguration, SenderConfiguration}
import io.jaegertracing.internal.samplers.ConstSampler
import io.opentracing.contrib.kafka.{TracingConsumerInterceptor, TracingKafkaUtils, TracingProducerInterceptor}
import io.opentracing.util.GlobalTracer
import io.opentracing.{SpanContext, Tracer}
import org.apache.kafka.clients.consumer.{ConsumerConfig, ConsumerRecord, KafkaConsumer}
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerConfig, ProducerRecord}
import org.apache.kafka.common.header.{Header, Headers}

import java.nio.charset.StandardCharsets
import scala.jdk.CollectionConverters._

class InterceptorTracingSpec extends SpecBase {

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
    props.put(ProducerConfig.INTERCEPTOR_CLASSES_CONFIG,
      classOf[TracingProducerInterceptor[String, String]].getName)

    val producer: KafkaProducer[String, String]  = new KafkaProducer[String, String](props)
    val record: ProducerRecord[String, String] = new ProducerRecord[String, String](topicName, "key", "value")
    producer.send(record)

    props.put(ConsumerConfig.INTERCEPTOR_CLASSES_CONFIG,
      classOf[TracingConsumerInterceptor[String, String]].getName)
    val consumer: KafkaConsumer[String, String] = new KafkaConsumer[String, String](props)
    consumer.subscribe(List(topicName).asJava)

    val records: Iterable[ConsumerRecord[String, String]] = KafkaSpecHelper.fetchAndProcessRecords(consumer)
    val headers: Headers = records.head.headers()
    val traceHeaders: Iterable[Header] = headers.headers("uber-trace-id").asScala

    traceHeaders foreach { th =>
      println(s"trace header: ${th.key()} :${new String(th.value(), StandardCharsets.UTF_8)}")
    }

    val spanContext: SpanContext = TracingKafkaUtils.extractSpanContext(headers, tracer)
    println(s"spanContext: $spanContext | ${spanContext.toSpanId} : ${spanContext.toTraceId} ")
  }

}
