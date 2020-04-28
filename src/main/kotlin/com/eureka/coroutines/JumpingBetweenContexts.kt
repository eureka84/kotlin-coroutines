package com.eureka.coroutines

import kotlinx.coroutines.*
import java.util.concurrent.Executors
import java.util.concurrent.ThreadFactory

fun main(): Unit = runBlocking {
    executorCoroutineDispatcher("CTX1").use { ctx1 ->
        executorCoroutineDispatcher("CTX2").use { ctx2 ->
            runBlocking(ctx1) {
                log("Started in ctx1")
                withContext(ctx2) {
                    log("Working in ctx2")
                }
                log("Back to ctx1")
            }
        }
    }
}

private fun executorCoroutineDispatcher(name: String) =
    Executors.newSingleThreadExecutor(namedThreadFactory(name)).asCoroutineDispatcher()

private fun namedThreadFactory(name: String): ThreadFactory = ThreadFactory { r -> Thread(r, name) }
