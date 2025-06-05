package com.sedsoftware.tackle.network.mapper

import com.sedsoftware.tackle.domain.model.TranslatedAttachment
import com.sedsoftware.tackle.domain.model.TranslatedPoll
import com.sedsoftware.tackle.domain.model.TranslatedPollOption
import com.sedsoftware.tackle.domain.model.Translation
import com.sedsoftware.tackle.network.response.TranslationResponse

internal object TranslationMapper {

    fun map(from: TranslationResponse): Translation =
        Translation(
            content = from.content,
            spoilerText = from.spoilerText,
            poll = from.poll?.let {
                TranslatedPoll(
                    id = it.id,
                    options = it.options.map { option -> TranslatedPollOption(option.title) }
                )
            },
            attachments = from.attachments.map { TranslatedAttachment(it.id, it.description) },
            language = from.language,
            provider = from.provider,
        )
}
