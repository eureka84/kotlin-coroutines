package com.eureka.coroutines

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

fun main() {
    threads()
}

fun threads() {
    repeat(1_000_000) {
        val thread = DelayThread()
        thread.start()
    }
}

class DelayThread : Thread() {
    override fun run() {
        sleep(1000L)
        print(".")
    }
}

fun CoroutineScope.coroutines() {
    repeat(1_000_000) { // launch a lot of coroutines
        launch {
            delay(1000L)
            print(".")
        }
    }
}