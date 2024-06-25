package com.sedsoftware.tackle.network.model

data class Instance(
    val domain: String,
    val title: String,
    val version: String,
    val sourceUrl: String,
    val description: String,
    val activePerMonth: Long,
    val thumbnailUrl: String,
    val languages: List<String>,
    val rules: List<Rule>,
) {

    companion object {
        fun empty(): Instance =
            Instance("", "", "", "", "", 0L, "", emptyList(), emptyList())
    }
}
