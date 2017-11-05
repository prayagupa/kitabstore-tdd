package com.beard

import java.util.concurrent.Callable
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.Future

class HTTPPerf {

    ExecutorService concurrenyPool

    HTTPPerf(int concurrencyFactor) {
        concurrenyPool = Executors.newFixedThreadPool(concurrencyFactor)
    }

    Tuple2<List<Future>, Long> concurrentRequests(List<Callable> requests) {
        def start = System.currentTimeMillis()
        def results = concurrenyPool.invokeAll(requests)
        def end = System.currentTimeMillis()
        def took = end - start
        println("concurrentRequests ${requests.size()} requests/" + took + "ms")
        new Tuple2<List<Future>, Long>(results, took)
    }

    def <T> Tuple2<List<T>, Long> concurrentRequestsResults(List<Callable<T>> requests) {
        def start = System.currentTimeMillis()
        def results = concurrenyPool.invokeAll(requests).collect { it.get() }
        def end = System.currentTimeMillis()
        def took = end - start
        println("concurrentRequestsResults ${requests.size()} requests/" + took + "ms")
        new Tuple2<List<T>, Long>(results, took)
    }

}
