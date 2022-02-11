package com.lucasdonato.avenue_code_test.presentation.base.presenter

import androidx.lifecycle.ViewModel
import com.lucasdonato.avenue_code_test.controller.executor.getCoroutineScope
import com.lucasdonato.avenue_code_test.controller.executor.ExecutorCoroutineScope

abstract class BaseViewModel : ViewModel(), ExecutorCoroutineScope by getCoroutineScope()