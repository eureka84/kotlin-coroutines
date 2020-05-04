package com.eureka.coroutines.flow

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeoutOrNull

private fun foo(): Flow<Int> = flow {
    for (i in 1..3) {
        Thread.sleep(100)
//        delay(100)
        println("Emitting $i")
        emit(i)
    }
}

fun main() = runBlocking {
    withTimeoutOrNull(250) { // Timeout after 250ms
        foo().collect { value -> println(value) }
    }
    println("Done")
}