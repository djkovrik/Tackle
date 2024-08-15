package com.sedsoftware.tackle.network.mappers

import com.sedsoftware.tackle.domain.model.Account
import com.sedsoftware.tackle.domain.model.CredentialAccountSource
import com.sedsoftware.tackle.domain.model.type.CredentialPrivacy
import com.sedsoftware.tackle.network.response.AccountResponse
import com.sedsoftware.tackle.network.response.type.CredentialPrivacyRemote.DIRECT
import com.sedsoftware.tackle.network.response.type.CredentialPrivacyRemote.PRIVATE
import com.sedsoftware.tackle.network.response.type.CredentialPrivacyRemote.PUBLIC
import com.sedsoftware.tackle.network.response.type.CredentialPrivacyRemote.UNKNOWN
import com.sedsoftware.tackle.network.response.type.CredentialPrivacyRemote.UNLISTED
import com.sedsoftware.tackle.utils.extension.toLocalDate
import com.sedsoftware.tackle.utils.extension.toLocalDateTime

internal object AccountMapper {

    fun map(from: AccountResponse): Account =
        Account(
            id = from.id,
            username = from.username,
            acct = from.acct,
            displayName = from.displayName,
            note = from.note,
            url = from.url,
            uri = from.uri,
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
                        PUBLIC -> CredentialPrivacy.PUBLIC
                        UNLISTED -> CredentialPrivacy.UNLISTED
                        PRIVATE -> CredentialPrivacy.PRIVATE
                        DIRECT -> CredentialPrivacy.DIRECT
                        UNKNOWN -> CredentialPrivacy.UNKNOWN
                    },
                    sensitive = credentials.sensitive,
                    language = credentials.language,
                    followRequestsCount = credentials.followRequestsCount,
                )
            },
            role = from.role?.let { RoleMapper.map(it) }
        )
}
