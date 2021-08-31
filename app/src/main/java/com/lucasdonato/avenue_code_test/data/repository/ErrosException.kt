package com.lucasdonato.avenue_code_test.data.repository

import java.lang.RuntimeException

class ErrorException(var errorCode: ErrorCode, var errorMessage: String? = "") : RuntimeException()


