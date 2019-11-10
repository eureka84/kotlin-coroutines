package com.eureka.coroutines

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

fun main(args: Array<String>) {
    testRunFunction()
}

fun testRunFunction() {

    GlobalScope.launch {
        println("In start : ${getThreadName()}")
        Thread.sleep(200)
        println("In ended : ${getThreadName()}")
    }

    run {
        println("Out start: ${getThreadName()}")
        Thread.sleep(300)
        println("Out ended: ${getThreadName()}")
    }
}

fun getThreadName(): String = Thread.currentThread().name