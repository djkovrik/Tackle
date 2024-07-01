package com.sedsoftware.tackle.utils

sealed class TackleException(
    override val message: String? = null,
    override val cause: Throwable? = null,
    val skipUi: Boolean = false,
) : Exception(message, cause) {

    data object MissedRegistrationData : TackleException(skipUi = true)

    class NetworkException(cause: Throwable) : TackleException(cause = cause)

    class RemoteServerException(
        message: String?,
        val description: String?,
        val code: Int?,
    ) : TackleException(message = message)

    class SerializationException(cause: Throwable) : TackleException(cause = cause)

    class Unknown(cause: Throwable) : TackleException(cause = cause)
}
