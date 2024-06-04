package com.sedsoftware.tackle.utils

data object MissedRegistrationDataException: IllegalStateException()
data object AppCreationException: Exception()
data class OAuthFlowException(val error: String?, val description: String?): Exception()
