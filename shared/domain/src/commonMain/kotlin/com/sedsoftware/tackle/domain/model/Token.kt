package com.sedsoftware.tackle.domain.model

data class Token(
    val accessToken: String,
    val tokenType: String,
    val scope: String,
    val createdAt: Long,
)
