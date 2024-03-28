package com.sedsoftware.tackle.auth.model

data class InstanceInfo(
    val name: String,
    val description: String,
    val logoUrl: String,
    val users: Long,
) {
    companion object {
        fun empty(): InstanceInfo = InstanceInfo(
            name = "",
            description = "",
            logoUrl = "",
            users = 0L
        )
    }
}
