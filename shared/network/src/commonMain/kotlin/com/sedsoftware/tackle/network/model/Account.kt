package com.sedsoftware.tackle.network.model

import kotlinx.datetime.LocalDateTime

data class Account(
    val id: String,
    val username: String,
    val acct: String,
    val displayName: String,
    val note: String,
    val avatar: String,
    val avatarStatic: String,
    val header: String,
    val headerStatic: String,
    val locked: Boolean,
    val fields: List<Field>,
    val emojis: List<CustomEmoji>,
    val bot: Boolean,
    val group: Boolean,
    val discoverable: Boolean,
    val noIndex: Boolean,
    val moved: Account?,
    val suspended: Boolean,
    val limited: Boolean,
    val createdAt: LocalDateTime,
    val lastStatusAt: LocalDateTime,
    val statusesCount: Long,
    val followersCount: Long,
    val followingCount: Long
)

data class Field(
    val name: String,
    val value: String,
    val verifiedAt: LocalDateTime,
)
