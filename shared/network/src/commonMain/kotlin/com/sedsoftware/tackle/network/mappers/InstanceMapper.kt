package com.sedsoftware.tackle.network.mappers

import com.sedsoftware.tackle.network.model.Instance
import com.sedsoftware.tackle.network.model.Rule
import com.sedsoftware.tackle.network.response.InstanceResponse

internal object InstanceMapper {

    fun map(from: InstanceResponse): Instance =
        Instance(
            domain = from.domain,
            title = from.title,
            version = from.version,
            sourceUrl = from.sourceUrl,
            description = from.description,
            activePerMonth = from.usage?.users?.activePerMonth ?: 0L,
            thumbnailUrl = from.thumbnail?.url.orEmpty(),
            languages = from.languages,
            rules = from.rules.map { Rule(it.id, it.text, it.hint) },
        )
}
