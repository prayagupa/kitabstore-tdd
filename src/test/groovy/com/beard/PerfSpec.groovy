package com.beard

import akka.actor.ActorSystem
import akka.http.javadsl.Http
import akka.http.javadsl.model.HttpRequest
import akka.stream.ActorMaterializer
import groovy.json.JsonSlurper
import spock.lang.Specification

import java.util.concurrent.Callable
import java.util.concurrent.Executors

class PerfSpec extends Specification {

    def EachRequestProcessingTime = 1000

    def json = new JsonSlurper()

    def actorSystem = ActorSystem.create()
    def actorExecutionHelper = ActorMaterializer.create(actorSystem)

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
        took <= ((concurrencyFactor/(numberOfLogicalCores * 2) * EachRequestProcessingTime) + 400)

    }


    def "can send 100 requests and log time and wait until all complete, and test the result"() {

        given:
        def concurrencyFactor = 5
        def numberOfLogicalCores =  Runtime.getRuntime().availableProcessors()

        def requestIds = []
        100.times {x -> requestIds + "API REQUEST $x"}

        def pool = Executors.newFixedThreadPool(4)

        def requests = (1..concurrencyFactor).collect {
            new Callable<String>() {

                String call() throws Exception {
                    httpProcess(it)
                }
            }
        }

        when:
        def start = System.currentTimeMillis()
        def futures = pool.invokeAll(requests)
        def end = System.currentTimeMillis()

        then:
        def took = "${end - start}"
        println("${concurrencyFactor} requests/" + took + "ms")

        futures.collect{f -> f.get()} == ["200"] * 5

        def expected = 600
        println(took <= expected)
        assert took < expected

    }

    String httpProcessor(int requestID) {
        println("processing ${requestID}")
        Thread.sleep(EachRequestProcessingTime)
        requestID
    }

    def process(def request, def endpoint) {

        println("Processing $request")

        def result = Http.get(actorSystem)
                .singleRequest(HttpRequest.create(endpoint), actorExecutionHelper)

        result.toCompletableFuture().get()
    }

    String httpProcess(def request) {
        println("Starting $request")
        def http = new URL("http://jsonplaceholder.typicode.com/posts").openConnection() as HttpURLConnection

        //http.getInputStream().getText("UTF-8")
        http.getResponseCode()
    }
}
