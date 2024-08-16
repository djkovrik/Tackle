package com.sedsoftware.tackle.domain.model

import com.sedsoftware.tackle.domain.model.type.CredentialPrivacy
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime

data class Account(
    val id: String,
    val username: String,
    val acct: String,
    val displayName: String,
    val note: String,
    val url: String,
    val uri: String,
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
    val lastStatusAt: LocalDate,
    val statusesCount: Long,
    val followersCount: Long,
    val followingCount: Long,
    val source: CredentialAccountSource?,
    val role: Role?,
)

data class Field(
    val name: String,
    val value: String,
    val verifiedAt: LocalDateTime,
)

data class CredentialAccountSource(
    val note: String,
    val fields: List<Field>,
    val privacy: CredentialPrivacy,
    val sensitive: Boolean,
    val language: String,
    val followRequestsCount: Long,
)
