package com.sedsoftware.tackle.auth.model

internal data class ObtainedCredentials(
    val domain: String,
    val clientId: String,
    val clientSecret: String,
)
