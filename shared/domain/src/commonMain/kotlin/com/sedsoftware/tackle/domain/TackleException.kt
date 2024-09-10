package com.sedsoftware.tackle.domain

sealed class TackleException(
    override val message: String? = null,
    override val cause: Throwable? = null,
    val action: Action = Action.NONE,
) : Exception(message, cause) {

    data object MissedRegistrationData : TackleException()

    class NetworkException(
        cause: Throwable,
    ) : TackleException(
        cause = cause,
        action = Action.SHOW_MESSAGE,
    )

    class RemoteServerException(
        val description: String?,
        val code: Int?,
        message: String?,
    ) : TackleException(
        message = message,
        action = if (code == HTTP_CODE_UNAUTHORIZED) {
            Action.LOGOUT
        } else {
            Action.SHOW_MESSAGE
        },
    )

    class SerializationException(
        cause: Throwable,
    ) : TackleException(
        cause = cause,
        action = Action.SHOW_MESSAGE,
    )

    data object FileSizeExceeded : TackleException(action = Action.SHOW_MESSAGE)

    data object FileTypeNotSupported : TackleException(action = Action.SHOW_MESSAGE)

    data object FileNotAvailable : TackleException(action = Action.SHOW_MESSAGE)

    class AttachmentsLimitExceeded(val limit: Int) : TackleException(action = Action.SHOW_MESSAGE)

    data object ScheduleDateTooShort : TackleException(action = Action.SHOW_MESSAGE)

    class Unknown(
        cause: Throwable,
    ) : TackleException(
        cause = cause,
        action = Action.SHOW_MESSAGE,
    )

    enum class Action {
        SHOW_MESSAGE,
        LOGOUT,
        NONE;
    }

    private companion object {
        const val HTTP_CODE_UNAUTHORIZED = 401
    }
}
