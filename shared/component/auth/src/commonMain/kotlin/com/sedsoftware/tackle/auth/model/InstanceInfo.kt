package com.sedsoftware.tackle.auth.model

data class InstanceInfo(
    val domain: String,
    val name: String,
    val description: String,
    val logoUrl: String,
    val users: Long,
) {
    companion object {
        fun empty(): InstanceInfo = InstanceInfo(
            domain = "",
            name = "",
            description = "",
            logoUrl = "",
            users = 0L
        )
    }
}
