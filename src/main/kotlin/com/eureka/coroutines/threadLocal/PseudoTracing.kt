package com.eureka.coroutines.threadLocal

import kotlinx.coroutines.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.Future


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

        application(applicationExecutorService)
    }
}

private fun application(executorService: ExecutorService): Job {
    val appFwCoroutineContext = executorService.asCoroutineDispatcher() +
            RequestContext.threadLocal.asContextElement(RequestContext.getRequestInfo())

    return runBlocking(appFwCoroutineContext) {
        launch { log("From 1st coroutine, requestInfo: ${RequestContext.getRequestInfo()}") }
        launch { log("From 2nd coroutine, requestInfo: ${RequestContext.getRequestInfo()}") }
        launch { log("From 3rd coroutine, requestInfo: ${RequestContext.getRequestInfo()}") }
    }
}

private fun log(message: String) {
    logger.info(message)
}

private val logger: Logger = LoggerFactory.getLogger("PseudoTracing")

