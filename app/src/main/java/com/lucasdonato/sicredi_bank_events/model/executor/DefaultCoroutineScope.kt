package com.lucasdonato.sicredi_bank_events.model.executor

import com.lucasdonato.sicredi_bank_events.model.data.model.entities.error.ErrorException
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class DefaultCoroutineScope : ExecutorCoroutineScope, CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Default + Job()

    override fun cancelJobs() {
        coroutineContext.cancel()
    }

    override infix fun ConcurrencyContinuation.onError(onError: (ErrorException) -> Unit) {
        initCoroutine(this.action, onError)
    }

    override suspend fun <T> runAsync(run: suspend () -> T) = async { run() }

    private fun initCoroutine(run: suspend () -> Unit, onError: (ErrorException) -> Unit = {}) =
        launch {
            try {
                run()
            } catch (e: ErrorException) {
                onError(e)
            }
        }
}

fun getCoroutineScope(): DefaultCoroutineScope = DefaultCoroutineScope()