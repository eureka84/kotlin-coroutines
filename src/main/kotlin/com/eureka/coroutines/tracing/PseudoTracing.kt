package com.eureka.coroutines.tracing

import com.eureka.coroutines.log
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.Future
import kotlin.coroutines.CoroutineContext


fun main() {
    val tomcatExecutorService = Executors.newFixedThreadPool(10, NamedThreadFactory("TOM"))
    val applicationExecutorService = Executors.newFixedThreadPool(10, NamedThreadFactory("APP"))

    val futures = tomcat(tomcatExecutorService, applicationExecutorService)

    futures.forEach { f -> f.get() }

    tomcatExecutorService.shutdown()
    applicationExecutorService.shutdown()
}

private fun tomcat(
    tomcatExecutorService: ExecutorService,
    applicationExecutorService: ExecutorService
): List<Future<*>> = (1..100).map { reqNum ->
    tomcatExecutorService.submit {
        val threadName = Thread.currentThread().name

        log("From Thread $threadName")

        RequestContext.setRequestInfo(RequestInfo(traceId = "Request $reqNum from thread $threadName"))

        application(newDispatcher(applicationExecutorService))
    }
}

private fun application(context: CoroutineContext) = runBlocking(context) {
    launch { log("From 1st coroutine, requestInfo: ${RequestContext.getRequestInfo()}") }
    launch { log("From 2nd coroutine, requestInfo: ${RequestContext.getRequestInfo()}") }
    launch { log("From 3rd coroutine, requestInfo: ${RequestContext.getRequestInfo()}") }
}

private fun newDispatcher(
    applicationExecutorService: ExecutorService
): RequestContextAware {
    return RequestContextAware(
        applicationExecutorService.asCoroutineDispatcher(),
        RequestContext.getRequestInfo()!!
    )
}

