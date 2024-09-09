package com.sedsoftware.tackle.utils.test

abstract class BaseStub {

    var responseWithException: Boolean = false

    fun <T> asResponse(response: T): T = if (!responseWithException) response else error("Test exception")

}
