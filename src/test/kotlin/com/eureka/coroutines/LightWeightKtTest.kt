package com.eureka.coroutines

import kotlinx.coroutines.runBlocking
import org.junit.Test

class LightWeightKtTest {

    @Test
    fun exercise_threads() {
        threads()
    }

    @Test
    fun exercise_coroutines() = runBlocking {
        coroutines()
    }
}