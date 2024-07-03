package com.sedsoftware.tackle.database.mappers

import com.sedsoftware.tackle.database.InstanceInfoEntity
import com.sedsoftware.tackle.database.model.CachedConfig
import com.sedsoftware.tackle.database.model.CachedRuleItemData
import com.sedsoftware.tackle.domain.model.Instance
import com.sedsoftware.tackle.domain.model.Rule
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

internal object InstanceInfoEntityMapper {

    private val json: Json by lazy {
        Json {
            coerceInputValues = true
            ignoreUnknownKeys = true
            useAlternativeNames = false
        }
    }

    fun fromEntity(from: InstanceInfoEntity): Instance {
        val languages: List<String> = json.decodeFromString(from.languages)
        val rules: List<CachedRuleItemData> = json.decodeFromString(from.rules)
        val cachedConfig: CachedConfig = json.decodeFromString(from.config)
        val config: Instance.Config = CachedConfigMapper.fromCache(cachedConfig)

        return Instance(
            domain = from.domain,
            title = from.title,
            version = from.version,
            sourceUrl = from.sourceUrl,
            description = from.description,
            activePerMonth = from.activePerMonth.toLong(),
            thumbnailUrl = from.thumbnailUrl,
            languages = languages,
            contactEmail = from.contactEmail,
            contactAccountId = from.contactAccountId,
            rules = rules.map { Rule(id = it.id, text = it.text, hint = it.hint) },
            config = config,
        )
    }

    fun toEntity(from: Instance): InstanceInfoEntity {
        val languagesStr: String = json.encodeToString(from.languages)
        val rulesCache: List<CachedRuleItemData> = from.rules.map { CachedRuleItemData(id = it.id, text = it.text, hint = it.hint) }
        val rulesStr: String = json.encodeToString(rulesCache)
        val configCache: CachedConfig = CachedConfigMapper.toCache(from.config)
        val configStr: String = json.encodeToString(configCache)

        return InstanceInfoEntity(
            domain = from.domain,
            title = from.title,
            version = from.version,
            sourceUrl = from.sourceUrl,
            description = from.description,
            activePerMonth = from.activePerMonth.toString(),
            thumbnailUrl = from.thumbnailUrl,
            blurhash = from.blurhash,
            languages = languagesStr,
            contactEmail = from.contactEmail,
            contactAccountId = from.contactAccountId,
            rules = rulesStr,
            config = configStr,
        )
    }
}
