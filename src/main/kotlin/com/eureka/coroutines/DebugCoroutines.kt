package com.eureka.coroutines

import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import org.slf4j.Logger
import org.slf4j.LoggerFactory

private val logger: Logger = LoggerFactory.getLogger("DebugCoroutines")

fun main() = runBlocking {
    val a = async {
        log("I'm computing a piece of the answer")
        6
    }
    val b = async (CoroutineName("TEST")) {
        log("I'm computing another piece of the answer")
        7
    }
    log("The answer is ${a.await() * b.await()}")
}

private fun log(message: String) {
    logger.info(message)
}
