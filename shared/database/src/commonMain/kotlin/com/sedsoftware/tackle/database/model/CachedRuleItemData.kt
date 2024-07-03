package com.sedsoftware.tackle.database.model

import kotlinx.serialization.Serializable

@Serializable
internal class CachedRuleItemData(
    val id: String,
    val text: String,
    val hint: String,
)
