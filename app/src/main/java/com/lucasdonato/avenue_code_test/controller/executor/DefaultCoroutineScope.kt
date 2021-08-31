package com.lucasdonato.avenue_code_test.controller.executor

import com.lucasdonato.avenue_code_test.data.repository.ErrorException
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