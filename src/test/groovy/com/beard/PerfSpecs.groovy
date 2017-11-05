package com.beard

import akka.actor.ActorSystem
import akka.http.javadsl.Http
import akka.http.javadsl.model.HttpRequest
import akka.http.javadsl.model.HttpResponse
import akka.stream.ActorMaterializer
import groovy.json.JsonSlurper
import spock.lang.Specification

import java.util.concurrent.Callable
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executors
import java.util.function.BiConsumer
import java.util.function.Function

class PerfSpec extends Specification {

    def EachRequestProcessingTime = 1000

    def json = new JsonSlurper()

    String httpProcessor(int requestID) {
        println("processing ${requestID}")
        Thread.sleep(EachRequestProcessingTime)
        requestID
    }

    def "can send 100 requests and log time"() {

        when:
        def concurrencyFactor = 100

        def requestIds = []
        100.times {x -> requestIds + "API REQUEST $x"}

        def pool = Executors.newFixedThreadPool(4)

        def requests = (1..concurrencyFactor).collect {
            new Callable<String>() {

                String call() throws Exception {
                    return httpProcessor(it)
                }
            }
        }

        def futures = requests.collect { pool.submit(it) }

        sleep(100)

        futures.collect {
            println(it.isDone())
        }

        then:
        println("hello")

    }


    def "can send 100 requests and log time and wait until all complete"() {

        when:
        def concurrencyFactor = 100
        def numberOfLogicalCores =  Runtime.getRuntime().availableProcessors()

        def requestIds = []
        100.times {x -> requestIds + "API REQUEST $x"}

        def pool = Executors.newFixedThreadPool(4)

        def requests = (1..concurrencyFactor).collect {
            new Callable<String>() {

                String call() throws Exception {
                    return httpProcessor(it)
                }
            }
        }

        def start = System.currentTimeMillis()

        def futures = pool.invokeAll(requests)

        def end = System.currentTimeMillis()

        def took = "${end - start}"
        println("${concurrencyFactor} requests/" + took + "ms")

        then:
        println(took <= (concurrencyFactor/numberOfLogicalCores) * (2 * EachRequestProcessingTime))
        took <= ((concurrencyFactor/(numberOfLogicalCores * 2) * EachRequestProcessingTime) + 200)

    }


    def "can send 100 requests and log time and wait until all complete, and test the result"() {

        when:
        def actorSystem = ActorSystem.create()
        def actorExecutionHelper = ActorMaterializer.create(actorSystem)

        def result = Http.get(actorSystem)
            .singleRequest(HttpRequest.create("http://jsonplaceholder.typicode.com/posts"), actorExecutionHelper)

        then:

//        sleep(5000)

//        result.whenComplete(new BiConsumer<HttpResponse, Throwable>() {
//            @Override
//            void accept(HttpResponse httpResponse, Throwable throwable) {
//                println("http response " + httpResponse.status())
//                println("iiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiii")
//            }
//        })

        def r = result.toCompletableFuture().get()

        println(r.status())
        println("ghhhvkhgghfhgfhgfghfhghhfhffh")

        def concurrencyFactor = 100
        def numberOfLogicalCores =  Runtime.getRuntime().availableProcessors()

        def requestIds = []
        100.times {x -> requestIds + "API REQUEST $x"}

        def pool = Executors.newFixedThreadPool(4)

        def requests = (1..concurrencyFactor).collect {
            new Callable<String>() {

                String call() throws Exception {
                    return httpProcessor(it)
                }
            }
        }

        def start = System.currentTimeMillis()

        def futures = pool.invokeAll(requests)
        def end = System.currentTimeMillis()

        def took = "${end - start}"
        println("${concurrencyFactor} requests/" + took + "ms")

        println(futures.collect{f -> f.get()})

        then:
        println(took <= (concurrencyFactor/numberOfLogicalCores) * (2 * EachRequestProcessingTime))
        took <= ((concurrencyFactor/(numberOfLogicalCores * 2) * EachRequestProcessingTime) + 200)

    }

}
