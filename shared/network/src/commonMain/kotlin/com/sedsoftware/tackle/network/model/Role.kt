package com.sedsoftware.tackle.network.model

data class Role(
    val id: String,
    val name: String,
    val color: String,
    val permissions: String,
    val highlighted: Boolean,
)
