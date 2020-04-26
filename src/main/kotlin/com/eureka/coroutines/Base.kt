package com.eureka.coroutines

import kotlinx.coroutines.*

fun main() =
    runBlocking { // this: CoroutineScope
        doSomething()

        coroutineScope { // Creates a coroutine scope
            launch {
                delay(500L)
                println("Task from nested launch")
            }

            delay(100L)
            println("Task from coroutine scope") // This line will be printed before the nested launch
        }

        println("Coroutine scope is over") // This line is not printed until the nested launch completes
    }

private fun CoroutineScope.doSomething() {
    launch {
        delay(200L)
        println("Task from runBlocking")
    }
}