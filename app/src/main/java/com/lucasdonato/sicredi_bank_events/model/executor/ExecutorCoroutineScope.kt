package com.lucasdonato.sicredi_bank_events.model.executor

import com.lucasdonato.sicredi_bank_events.model.data.model.entities.error.ErrorException
import kotlinx.coroutines.Deferred

interface ExecutorCoroutineScope {
    fun cancelJobs()
    fun runCoroutine(run: suspend () -> Unit): ConcurrencyContinuation = ConcurrencyContinuation(run)
    suspend fun <T> runAsync(run: suspend () -> T): Deferred<T>
    infix fun ConcurrencyContinuation.onError(onError: (ErrorException) -> Unit)
}

inline class ConcurrencyContinuation(val action: suspend () -> Unit)