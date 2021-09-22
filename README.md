# tracer #

Welcome to tracer!

## integration variants

* intrusive - client wrappers

see 

* less intrusive -

* non-intrusive - javaagent

https://github.com/open-telemetry/opentelemetry-java-instrumentation

I did not success in running the sbt plugin (https://github.com/sbt/sbt-javaagent), so I resorted to editing the run config for the tests

in Intellij, go to `Run -> Edit Configurations`

`-javaagent:<path-to-project>/tracer/src/main/resources/opentelemetry-javaagent-all.jar -Dotel.traces.exporter=jaeger`

data in the agent traces:

```json
{
    "data": [
        {
            "traceID": "e30fc7a65ffe0ffa3fd3c7a8bd1a7642",
            "spans": [
                {
                    "traceID": "e30fc7a65ffe0ffa3fd3c7a8bd1a7642",
                    "spanID": "bccc2f6e629575df",
                    "operationName": "perftest receive",
                    "references": [],
                    "startTime": 1632326470634464,
                    "duration": 88847,
                    "tags": [
                        {
                            "key": "messaging.destination_kind",
                            "type": "string",
                            "value": "topic"
                        },
                        {
                            "key": "messaging.system",
                            "type": "string",
                            "value": "kafka"
                        },
                        {
                            "key": "messaging.destination",
                            "type": "string",
                            "value": "perftest"
                        },
                        {
                            "key": "thread.id",
                            "type": "int64",
                            "value": 1
                        },
                        {
                            "key": "messaging.operation",
                            "type": "string",
                            "value": "receive"
                        },
                        {
                            "key": "thread.name",
                            "type": "string",
                            "value": "ScalaTest-run-running-TracingInitSpec"
                        },
                        {
                            "key": "span.kind",
                            "type": "string",
                            "value": "consumer"
                        },
                        {
                            "key": "otel.library.name",
                            "type": "string",
                            "value": "io.opentelemetry.kafka-clients-0.11"
                        },
                        {
                            "key": "otel.library.version",
                            "type": "string",
                            "value": "1.6.0"
                        },
                        {
                            "key": "internal.span.format",
                            "type": "string",
                            "value": "proto"
                        }
                    ],
                    "logs": [],
                    "processID": "p1",
                    "warnings": null
                },
                {
                    "traceID": "e30fc7a65ffe0ffa3fd3c7a8bd1a7642",
                    "spanID": "b1c0097a00448d3c",
                    "operationName": "perftest process",
                    "references": [
                        {
                            "refType": "FOLLOWS_FROM",
                            "traceID": "ba8a7dc238d0a18ff853440f5ce35434",
                            "spanID": "61e1caa8a5207eec"
                        },
                        {
                            "refType": "CHILD_OF",
                            "traceID": "e30fc7a65ffe0ffa3fd3c7a8bd1a7642",
                            "spanID": "bccc2f6e629575df"
                        }
                    ],
                    "startTime": 1632326470724244,
                    "duration": 1092,
                    "tags": [
                        {
                            "key": "messaging.destination_kind",
                            "type": "string",
                            "value": "topic"
                        },
                        {
                            "key": "messaging.system",
                            "type": "string",
                            "value": "kafka"
                        },
                        {
                            "key": "messaging.message_payload_size_bytes",
                            "type": "int64",
                            "value": 5
                        },
                        {
                            "key": "messaging.destination",
                            "type": "string",
                            "value": "perftest"
                        },
                        {
                            "key": "thread.id",
                            "type": "int64",
                            "value": 1
                        },
                        {
                            "key": "messaging.operation",
                            "type": "string",
                            "value": "process"
                        },
                        {
                            "key": "messaging.kafka.partition",
                            "type": "int64",
                            "value": 1
                        },
                        {
                            "key": "thread.name",
                            "type": "string",
                            "value": "ScalaTest-run-running-TracingInitSpec"
                        },
                        {
                            "key": "span.kind",
                            "type": "string",
                            "value": "consumer"
                        },
                        {
                            "key": "otel.library.name",
                            "type": "string",
                            "value": "io.opentelemetry.kafka-clients-0.11"
                        },
                        {
                            "key": "otel.library.version",
                            "type": "string",
                            "value": "1.6.0"
                        },
                        {
                            "key": "internal.span.format",
                            "type": "string",
                            "value": "proto"
                        }
                    ],
                    "logs": [],
                    "processID": "p1",
                    "warnings": null
                },
                {
                    "traceID": "e30fc7a65ffe0ffa3fd3c7a8bd1a7642",
                    "spanID": "0224cedd26716413",
                    "operationName": "perftest process",
                    "references": [
                        {
                            "refType": "FOLLOWS_FROM",
                            "traceID": "ba8a7dc238d0a18ff853440f5ce35434",
                            "spanID": "61e1caa8a5207eec"
                        },
                        {
                            "refType": "CHILD_OF",
                            "traceID": "e30fc7a65ffe0ffa3fd3c7a8bd1a7642",
                            "spanID": "bccc2f6e629575df"
                        }
                    ],
                    "startTime": 1632326470742060,
                    "duration": 40,
                    "tags": [
                        {
                            "key": "messaging.destination_kind",
                            "type": "string",
                            "value": "topic"
                        },
                        {
                            "key": "messaging.system",
                            "type": "string",
                            "value": "kafka"
                        },
                        {
                            "key": "messaging.message_payload_size_bytes",
                            "type": "int64",
                            "value": 5
                        },
                        {
                            "key": "messaging.destination",
                            "type": "string",
                            "value": "perftest"
                        },
                        {
                            "key": "thread.id",
                            "type": "int64",
                            "value": 1
                        },
                        {
                            "key": "messaging.operation",
                            "type": "string",
                            "value": "process"
                        },
                        {
                            "key": "messaging.kafka.partition",
                            "type": "int64",
                            "value": 1
                        },
                        {
                            "key": "thread.name",
                            "type": "string",
                            "value": "ScalaTest-run-running-TracingInitSpec"
                        },
                        {
                            "key": "span.kind",
                            "type": "string",
                            "value": "consumer"
                        },
                        {
                            "key": "otel.library.name",
                            "type": "string",
                            "value": "io.opentelemetry.kafka-clients-0.11"
                        },
                        {
                            "key": "otel.library.version",
                            "type": "string",
                            "value": "1.6.0"
                        },
                        {
                            "key": "internal.span.format",
                            "type": "string",
                            "value": "proto"
                        }
                    ],
                    "logs": [],
                    "processID": "p1",
                    "warnings": null
                }
            ],
            "processes": {
                "p1": {
                    "serviceName": "unknown_service:java",
                    "tags": [
                        {
                            "key": "host.arch",
                            "type": "string",
                            "value": "amd64"
                        },
                        {
                            "key": "host.name",
                            "type": "string",
                            "value": "ksilin-X1"
                        },
                        {
                            "key": "hostname",
                            "type": "string",
                            "value": "ksilin-X1"
                        },
                        {
                            "key": "ip",
                            "type": "string",
                            "value": "127.0.1.1"
                        },
                        {
                            "key": "jaeger.version",
                            "type": "string",
                            "value": "opentelemetry-java"
                        },
                        {
                            "key": "os.description",
                            "type": "string",
                            "value": "Linux 5.11.0-34-generic"
                        },
                        {
                            "key": "os.type",
                            "type": "string",
                            "value": "linux"
                        },
                        {
                            "key": "process.command_line",
                            "type": "string",
                            "value": "REDACTED"
                        },
                        {
                            "key": "process.executable.path",
                            "type": "string",
                            "value": "/home/ksilin/.jdks/adopt-openjdk-11.0.11:bin:java"
                        },
                        {
                            "key": "process.pid",
                            "type": "int64",
                            "value": 1130912
                        },
                        {
                            "key": "process.runtime.description",
                            "type": "string",
                            "value": "AdoptOpenJDK OpenJDK 64-Bit Server VM 11.0.11+9"
                        },
                        {
                            "key": "process.runtime.name",
                            "type": "string",
                            "value": "OpenJDK Runtime Environment"
                        },
                        {
                            "key": "process.runtime.version",
                            "type": "string",
                            "value": "11.0.11+9"
                        },
                        {
                            "key": "service.name",
                            "type": "string",
                            "value": "unknown_service:java"
                        },
                        {
                            "key": "telemetry.auto.version",
                            "type": "string",
                            "value": "1.6.0"
                        },
                        {
                            "key": "telemetry.sdk.language",
                            "type": "string",
                            "value": "java"
                        },
                        {
                            "key": "telemetry.sdk.name",
                            "type": "string",
                            "value": "opentelemetry"
                        },
                        {
                            "key": "telemetry.sdk.version",
                            "type": "string",
                            "value": "1.6.0"
                        }
                    ]
                }
            },
            "warnings": null
        }
    ],
    "total": 0,
    "limit": 0,
    "offset": 0,
    "errors": null
}
```

interesting detail - the trace contains two `process` spans - would liekt o knowwhat they are

### seeing tracing in action in your logs

```
2021-09-22T11:35:31,327Z] [DEBUG] [ThriftSenderFactory] - Using the UDP Sender to send spans to the agent.
[2021-09-22T11:35:31,334Z] [DEBUG] [SenderResolver] - Using sender UdpSender(host=localhost, port=6831)
[2021-09-22T11:35:31,348Z] [INFO ] [Configuration] - Initialized tracer=JaegerTracer(version=Java-1.6.0, serviceName=TracingInitSpec, reporter=CompositeReporter(reporters=[RemoteReporter(sender=UdpSender(host=localhost, port=6831), closeEnqueueTimeout=1000), LoggingReporter(logger=Logger[io.jaegertracing.internal.reporters.LoggingReporter])]), sampler=ConstSampler(decision=true, tags={sampler.type=const, sampler.param=true}), tags={hostname=ksilin-X1, jaeger.version=Java-1.6.0, ip=127.0.1.1}, zipkinSharedRpcSpan=false, expandExceptionLogs=false, useTraceId128Bit=false)
...
[2021-09-22T11:35:37,653Z] [INFO ] [LoggingReporter] - Span reported: 9675c0a51267a8ff:9675c0a51267a8ff:0:1 - To_perftest
```

what do we see from the logs?

* port 6831 - jaeger.thrift over compact thrift protocol

* Tags:

component: java-kafka
internal.span.format: proto
message_bus.destination: perftest
peer.service: kafka
sampler.param: true
sampler.type: const
span.kind: producer

* Process:

hostname: ksilin-X1
ip: 127.0.1.1
jaeger.version: Java-1.6.0

## TODO 

KStreams integration: https://github.com/opentracing-contrib/java-kafka-client#kafka-streams-1

## Contribution policy ##

Contributions via GitHub pull requests are gladly accepted from their original author. Along with
any pull requests, please state that the contribution is your original work and that you license
the work to the project under the project's open source license. Whether or not you state this
explicitly, by submitting any copyrighted material via pull request, email, or other means you
agree to license the material under the project's open source license and warrant that you have the
legal authority to do so.

## License ##

This code is open source software licensed under the
[Apache-2.0](http://www.apache.org/licenses/LICENSE-2.0) license.
