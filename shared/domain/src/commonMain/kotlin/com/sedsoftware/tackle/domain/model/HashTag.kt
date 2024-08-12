package com.sedsoftware.tackle.domain.model

data class HashTag(
    val name: String,
    val url: String,
    val history: List<HashTagHistory>,
    val following: Boolean,
    val trendable: Boolean,
    val usable: Boolean,
    val requiresReview: Boolean,
)

data class HashTagHistory(
    val day: String,
    val uses: String,
    val accounts: String,
)
