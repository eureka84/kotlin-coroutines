package com.eureka.coroutines.threadLocal

object RequestContext {
    val threadLocal = InheritableThreadLocal<RequestInfo>()
    fun getRequestInfo(): RequestInfo? = threadLocal.get()
    fun setRequestInfo(requestInfo: RequestInfo?) {
        threadLocal.set(requestInfo)
    }
}

data class RequestInfo(val traceId: String)
