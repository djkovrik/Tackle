package com.sedsoftware.tackle.utils.test

open class StubWithException {

    var shouldThrowException: Boolean = false

    fun <T> asResponse(validResponse: T): T = if (!shouldThrowException) validResponse else error("Test exception")
}
