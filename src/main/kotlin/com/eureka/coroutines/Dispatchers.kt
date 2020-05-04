package com.eureka.coroutines

import kotlinx.coroutines.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


fun main(): Unit = runBlocking<Unit> {

    val newFixedThreadPool: ExecutorService = Executors.newFixedThreadPool(2)

    launch(newFixedThreadPool.asCoroutineDispatcher()) {
        println("From mine context     : I'm working in thread ${Thread.currentThread().name}")
    }

    launch { // context of the parent, com.eureka.coroutines.flow.main runBlocking coroutine
        println("com.eureka.coroutines.flow.main runBlocking      : I'm working in thread ${Thread.currentThread().name}")
    }
    launch(Dispatchers.Unconfined) { // not confined -- will work with com.eureka.coroutines.flow.main thread
        println("Unconfined            : I'm working in thread ${Thread.currentThread().name}")
    }
    launch(Dispatchers.Default) { // will get dispatched to DefaultDispatcher
        println("Default               : I'm working in thread ${Thread.currentThread().name}")
    }
    launch(newSingleThreadContext("MyOwnThread")) { // will get its own new thread
        println("newSingleThreadContext: I'm working in thread ${Thread.currentThread().name}")
    }

    newFixedThreadPool.shutdown()

}