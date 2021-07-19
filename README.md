# source-path-repro

A Clojure library designed to reproduce an error with clojure-lsp automatically
detecting `project.clj` source paths when functions are used.


## Result from starting LSP mode

``` shell
Jul 18, 2021 11:46:50 PM org.eclipse.lsp4j.jsonrpc.json.StreamMessageProducer fireError
SEVERE: An error occurred while processing an incoming message.
java.lang.NullPointerException
	at org.eclipse.lsp4j.jsonrpc.RemoteEndpoint.handleRequest(RemoteEndpoint.java:279)
	at org.eclipse.lsp4j.jsonrpc.RemoteEndpoint.consume(RemoteEndpoint.java:190)
	at org.eclipse.lsp4j.jsonrpc.json.StreamMessageProducer.handleMessage(StreamMessageProducer.java:194)
	at org.eclipse.lsp4j.jsonrpc.json.StreamMessageProducer.listen(StreamMessageProducer.java:94)
	at org.eclipse.lsp4j.jsonrpc.json.ConcurrentMessageProcessor.run(ConcurrentMessageProcessor.java:113)
	at java.base/java.util.concurrent.Executors$RunnableAdapter.call(Executors.java:515)
	at java.base/java.util.concurrent.FutureTask.run(FutureTask.java:264)
	at java.base/java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1128)
	at java.base/java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:628)
	at java.base/java.lang.Thread.run(Thread.java:834)

```


## clojure-lsp.log

``` shell
2021-07-19T02:49:03.674Z WFERREIR-M-D2BB INFO [clojure-lsp.server:409] - Starting server...
2021-07-19T02:49:03.752Z WFERREIR-M-D2BB DEBUG [clojure-lsp.nrepl:24] - nrepl not found, skipping nrepl server start...
2021-07-19T02:49:03.784Z WFERREIR-M-D2BB INFO [clojure-lsp.server:294] - Initializing...
2021-07-19T02:49:03.836Z WFERREIR-M-D2BB INFO [clojure-lsp.crawler:250] - Automatically resolved source-paths from project.clj: #{"feature" unquote get-path "lib" "test" "src/test/clojure"}
2021-07-19T02:49:03.840Z WFERREIR-M-D2BB ERROR [clojure-lsp.server:?] -
                                                                                  java.lang.Thread.run                        Thread.java:  834
                                                    java.util.concurrent.ThreadPoolExecutor$Worker.run            ThreadPoolExecutor.java:  628
                                                     java.util.concurrent.ThreadPoolExecutor.runWorker            ThreadPoolExecutor.java: 1128
                                                                   java.util.concurrent.FutureTask.run                    FutureTask.java:  264
                                                   java.util.concurrent.Executors$RunnableAdapter.call                     Executors.java:  515
                                         org.eclipse.lsp4j.jsonrpc.json.ConcurrentMessageProcessor.run    ConcurrentMessageProcessor.java:  113
                                           org.eclipse.lsp4j.jsonrpc.json.StreamMessageProducer.listen         StreamMessageProducer.java:   94
                                    org.eclipse.lsp4j.jsonrpc.json.StreamMessageProducer.handleMessage         StreamMessageProducer.java:  194
                                                      org.eclipse.lsp4j.jsonrpc.RemoteEndpoint.consume                RemoteEndpoint.java:  190
                                                org.eclipse.lsp4j.jsonrpc.RemoteEndpoint.handleRequest                RemoteEndpoint.java:  261
                                            org.eclipse.lsp4j.jsonrpc.services.GenericEndpoint.request               GenericEndpoint.java:  120
                                      org.eclipse.lsp4j.jsonrpc.services.GenericEndpoint.lambda$null$0               GenericEndpoint.java:   65
                                                                                                   ...
                                              jdk.internal.reflect.DelegatingMethodAccessorImpl.invoke  DelegatingMethodAccessorImpl.java:   43
                                                  jdk.internal.reflect.NativeMethodAccessorImpl.invoke      NativeMethodAccessorImpl.java:   62
                                                 jdk.internal.reflect.NativeMethodAccessorImpl.invoke0       NativeMethodAccessorImpl.java
clojure_lsp.server.proxy$clojure_lsp.ClojureExtensions$ExtraMethods$LanguageServer$4129860f.initialize
                                                                              clojure-lsp.server/fn/fn                         server.clj:  295
                                                                       clojure-lsp.handlers/initialize                       handlers.clj:   49
                                                                clojure-lsp.crawler/initialize-project                        crawler.clj:  281
                                                                                   clojure.core/update                           core.clj: 6185
                                                                               clojure.core/partial/fn                           core.clj: 2635
                                                              clojure-lsp.crawler/process-source-paths                        crawler.clj:  263
                                                                                     clojure.core/mapv                           core.clj: 6905
                                                                                   clojure.core/reduce                           core.clj: 6830
                                                                           clojure.core.protocols/fn/G                      protocols.clj:   13
                                                                             clojure.core.protocols/fn                      protocols.clj:   75
                                                                    clojure.core.protocols/iter-reduce                      protocols.clj:   49
                                                                                  clojure.core/mapv/fn                           core.clj: 6914
                                                           clojure-lsp.crawler/process-source-paths/fn                        crawler.clj:  263
                                                                           clojure-lsp.crawler/to-file                        crawler.clj:   23
java.lang.ClassCastException: class clojure.lang.Symbol cannot be cast to class java.lang.String (clojure.lang.Symbol is in unnamed module of loader 'app'; java.lang.String is in module java.base of loader 'bootstrap')

2021-07-19T02:49:03.850Z WFERREIR-M-D2BB DEBUG [clojure-lsp.server:?] - :initialize 67ms
```


# Issue report

From previous logs, the error happens at `2021-07-19T02:49:03.836Z WFERREIR-M-D2BB INFO [clojure-lsp.crawler:250] - Automatically resolved source-paths from project.clj: #{"feature" unquote get-path "lib" "test" "src/test/clojure"}`

Because `:source-paths` in `project.clj` is using a function to produce the desired vector of strings, but `clojure-lsp` is returning symbols too.
