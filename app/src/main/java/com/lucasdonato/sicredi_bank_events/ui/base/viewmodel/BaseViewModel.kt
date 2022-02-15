package com.lucasdonato.sicredi_bank_events.ui.base.viewmodel

import androidx.lifecycle.ViewModel
import com.lucasdonato.sicredi_bank_events.model.executor.getCoroutineScope
import com.lucasdonato.sicredi_bank_events.model.executor.ExecutorCoroutineScope

abstract class BaseViewModel : ViewModel(), ExecutorCoroutineScope by getCoroutineScope()