package com.sedsoftware.tackle.network.mapper

import com.sedsoftware.tackle.domain.model.TranslatedAttachment
import com.sedsoftware.tackle.domain.model.TranslatedPoll
import com.sedsoftware.tackle.domain.model.TranslatedPollOption
import com.sedsoftware.tackle.domain.model.Translation
import com.sedsoftware.tackle.network.response.TranslationResponse
import com.sedsoftware.tackle.utils.StringUtils

internal object TranslationMapper {

    fun map(from: TranslationResponse): Translation {
        val translationPlainText: String = StringUtils.decodeHtml(from.content)
        return Translation(
            content = translationPlainText,
            spoilerText = from.spoilerText,
            poll = from.poll?.let {
                TranslatedPoll(
                    id = it.id,
                    options = it.options.map { option -> TranslatedPollOption(option.title) }
                )
            },
            attachments = from.attachments.map { TranslatedAttachment(it.id, it.description) },
            sourceLanguage = from.sourceLanguage,
            language = from.language,
            provider = from.provider,
        )
    }
}
