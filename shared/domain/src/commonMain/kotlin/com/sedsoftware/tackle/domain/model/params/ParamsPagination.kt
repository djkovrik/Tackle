package com.sedsoftware.tackle.domain.model.params

data class ParamsPagination(
    val maxId: String? = null,
    val sinceId: String? = null,
    val minId: String? = null,
    val limit: Int? = null,
)
