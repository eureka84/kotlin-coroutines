package com.eureka.coroutines.tracing

import kotlinx.coroutines.CoroutineDispatcher
import kotlin.coroutines.AbstractCoroutineContextElement
import kotlin.coroutines.Continuation
import kotlin.coroutines.ContinuationInterceptor
import kotlin.coroutines.CoroutineContext

// inspired by https://github.com/Kotlin/kotlinx.coroutines/issues/119
class RequestContextDispatcher(
    private val context: CoroutineDispatcher,
    private val requestInfo: RequestInfo
) : AbstractCoroutineContextElement(ContinuationInterceptor), ContinuationInterceptor {

    override fun <T> interceptContinuation(continuation: Continuation<T>): Continuation<T> {
        val next = context[ContinuationInterceptor]

        val wrappedContinuation = Wrapper(continuation)

        return when (next) {
            null -> wrappedContinuation
            else -> next.interceptContinuation(wrappedContinuation)
        }
    }

    private inner class Wrapper<T>(private val continuation: Continuation<T>) : Continuation<T> {
        private inline fun wrap(block: () -> Unit) {
            try {
                RequestContext.setRequestInfo(requestInfo)
                block()
            } finally {
                RequestContext.clear()
            }
        }

        override val context: CoroutineContext get() = continuation.context
        override fun resumeWith(result: Result<T>) = wrap {
            continuation.resumeWith(result)
        }
    }
}

object RequestContext {
    private val threadLocal = InheritableThreadLocal<RequestInfo>()
    fun getRequestInfo(): RequestInfo? = threadLocal.get()
    fun setRequestInfo(requestInfo: RequestInfo?) {
        threadLocal.set(requestInfo)
    }
    fun clear() {
        threadLocal.set(null)
    }
}

data class RequestInfo(val traceId: String)
