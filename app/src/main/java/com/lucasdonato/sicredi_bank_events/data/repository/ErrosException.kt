package com.lucasdonato.sicredi_bank_events.data.repository

import java.lang.RuntimeException

class ErrorException(var errorCode: ErrorCode, var errorMessage: String? = "") : RuntimeException()


