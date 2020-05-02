package com.eureka.coroutines

import kotlinx.coroutines.*
import java.util.concurrent.Executors
import java.util.concurrent.ThreadFactory
import kotlin.coroutines.CoroutineContext

fun main(): Unit = runBlocking {
    executorCoroutineDispatcher("CTX1").use { ctx1: CoroutineDispatcher ->
        executorCoroutineDispatcher("CTX2").use { ctx2: CoroutineContext ->
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

fun executorCoroutineDispatcher(name: String) =
    Executors.newSingleThreadExecutor(namedThreadFactory(name)).asCoroutineDispatcher()

fun namedThreadFactory(name: String): ThreadFactory = ThreadFactory { r -> Thread(r, name) }
