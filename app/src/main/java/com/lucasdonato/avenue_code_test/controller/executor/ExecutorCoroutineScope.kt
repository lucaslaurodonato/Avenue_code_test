package com.lucasdonato.avenue_code_test.controller.executor

import com.lucasdonato.avenue_code_test.data.repository.ErrorException
import kotlinx.coroutines.Deferred

interface ExecutorCoroutineScope {
    fun cancelJobs()
    fun runCoroutine(run: suspend () -> Unit): ConcurrencyContinuation = ConcurrencyContinuation(run)
    suspend fun <T> runAsync(run: suspend () -> T): Deferred<T>
    infix fun ConcurrencyContinuation.onError(onError: (ErrorException) -> Unit)
}

inline class ConcurrencyContinuation(val action: suspend () -> Unit)