package com.eureka.coroutines

import kotlinx.coroutines.*
import kotlin.concurrent.thread

fun main() {
    threads()
}

fun threads() {
    val threads: List<Thread> = List(100_000) {
        thread {
            Thread.sleep(1000L)
            print(".")
        }
    }
    threads.forEach { t -> t.join() }
}

fun coroutines() = runBlocking {
    val jobs: List<Job> = List(100_000) { // launch a lot of coroutines
        launch {
            delay(1000L)
            print(".")
        }
    }
    jobs.forEach { j -> j.join() }
}