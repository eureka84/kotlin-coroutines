package com.eureka.coroutines.flow

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.system.measureTimeMillis


private fun foo(): Sequence<Int> = sequence { // sequence builder
    for (i in 1..3) {
        Thread.sleep(100) // pretend we are computing it
        println("Yield value $i")
        yield(i) // yield next value
    }
}

fun main() {
    measureTimeMillis {
        runBlocking {
            launch {
                for (k in 1..3) {
                    println("I'm not blocked $k")
                    delay(100)
                }
            }
            foo().forEach { el -> println(el) }
        }
    }.let { duration -> println(duration) }
}