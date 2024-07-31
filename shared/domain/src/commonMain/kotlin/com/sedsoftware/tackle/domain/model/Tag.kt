package com.sedsoftware.tackle.domain.model

data class Tag(
    val name: String,
    val url: String,
    val history: List<TagHistory>,
    val following: Boolean,
    val trendable: Boolean,
    val usable: Boolean,
    val requiresReview: Boolean,
)

data class TagHistory(
    val day: String,
    val uses: String,
    val accounts: String,
)
