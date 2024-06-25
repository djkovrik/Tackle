package com.sedsoftware.tackle.utils

data object MissedRegistrationDataException : IllegalStateException()
data object AppCreationException : Exception()
data class RemoteServerException(override val message: String, val description: String, val code: Int) : Exception(message)
