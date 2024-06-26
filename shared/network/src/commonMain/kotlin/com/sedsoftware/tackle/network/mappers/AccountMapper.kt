package com.sedsoftware.tackle.network.mappers

import com.sedsoftware.tackle.network.model.Account
import com.sedsoftware.tackle.network.model.CredentialAccountSource
import com.sedsoftware.tackle.network.model.type.CredentialPrivacy
import com.sedsoftware.tackle.network.response.AccountResponse
import com.sedsoftware.tackle.network.response.type.CredentialPrivacyRemote
import com.sedsoftware.tackle.utils.toLocalDate
import com.sedsoftware.tackle.utils.toLocalDateTime

internal object AccountMapper {

    fun map(from: AccountResponse): Account =
        Account(
            id = from.id,
            username = from.username,
            acct = from.acct,
            displayName = from.displayName,
            note = from.note,
            avatar = from.avatar,
            avatarStatic = from.avatarStatic,
            header = from.header,
            headerStatic = from.headerStatic,
            locked = from.locked,
            fields = from.fields.map(FieldMapper::map),
            emojis = CustomEmojiMapper.map(from.emojis),
            bot = from.bot,
            group = from.group,
            discoverable = from.discoverable,
            noIndex = from.noIndex,
            moved = from.moved?.let { map(it) },
            suspended = from.suspended,
            limited = from.limited,
            createdAt = from.createdAt.toLocalDateTime(),
            lastStatusAt = from.lastStatusAt.toLocalDate(),
            statusesCount = from.statusesCount,
            followersCount = from.followersCount,
            followingCount = from.followingCount,
            source = from.source?.let { credentials ->
                CredentialAccountSource(
                    note = credentials.note,
                    fields = credentials.fields.map(FieldMapper::map),
                    privacy = when (credentials.privacy) {
                        CredentialPrivacyRemote.PUBLIC -> CredentialPrivacy.PUBLIC
                        CredentialPrivacyRemote.UNLISTED -> CredentialPrivacy.UNLISTED
                        CredentialPrivacyRemote.PRIVATE -> CredentialPrivacy.PRIVATE
                        CredentialPrivacyRemote.DIRECT -> CredentialPrivacy.DIRECT
                        CredentialPrivacyRemote.UNKNOWN -> CredentialPrivacy.UNKNOWN
                    },
                    sensitive = credentials.sensitive,
                    language = credentials.language,
                    followRequestsCount = credentials.followRequestsCount,
                )
            },
            role = from.role?.let { RoleMapper.map(it) }
        )
}
