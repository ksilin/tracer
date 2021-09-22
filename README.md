# tracer #

Welcome to tracer!

##

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
