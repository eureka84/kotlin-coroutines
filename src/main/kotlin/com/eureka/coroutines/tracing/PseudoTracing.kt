package com.eureka.coroutines.tracing

import com.eureka.coroutines.log
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.concurrent.Executors
import kotlin.coroutines.CoroutineContext


fun main() {
    val tomcatExecutorService = Executors.newFixedThreadPool(10, NamedThreadFactory("TOM"))
    val applicationExecutorService = Executors.newFixedThreadPool(10, NamedThreadFactory("APP"))

    (1..100).forEach { reqNum ->
        tomcatExecutorService.submit {
            val threadName = Thread.currentThread().name

            log("From Thread $threadName")

            val requestInfo = RequestInfo(traceId = "Request $reqNum from thread $threadName")
            val dispatcher = RequestContextDispatcher(
                applicationExecutorService.asCoroutineDispatcher(),
                requestInfo
            )

            launchCoroutines(dispatcher) // simulo mie chiamate concorrenti con coroutines
        }
    }

    tomcatExecutorService.shutdown()
    applicationExecutorService.shutdown()
}

private fun launchCoroutines(context: CoroutineContext) {
    runBlocking(context) {
        launch { log("From 1st coroutine, requestInfo: ${RequestContext.getRequestInfo()}") }
        launch { log("From 2nd coroutine, requestInfo: ${RequestContext.getRequestInfo()}") }
        launch { log("From 3rd coroutine, requestInfo: ${RequestContext.getRequestInfo()}") }
    }
}

