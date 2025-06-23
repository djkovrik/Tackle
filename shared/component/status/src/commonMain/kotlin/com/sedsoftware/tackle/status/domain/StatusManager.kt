package com.sedsoftware.tackle.status.domain

import com.sedsoftware.tackle.domain.model.Status
import com.sedsoftware.tackle.domain.model.Translation
import com.sedsoftware.tackle.status.StatusComponentGateways
import com.sedsoftware.tackle.status.model.StatusContextAction
import com.sedsoftware.tackle.utils.extension.toNormalizedUrl

internal class StatusManager(
    private val api: StatusComponentGateways.Api,
    private val tools: StatusComponentGateways.Tools,
) {
    suspend fun delete(statusId: String, deleteMedia: Boolean): Result<Boolean> = runCatching {
        val response = api.delete(statusId, deleteMedia)
        return@runCatching response.id == statusId
    }

    suspend fun translate(statusId: String): Result<Translation> = runCatching {
        val currentLocale = tools.getCurrentLocale()
        val response = api.translate(statusId, currentLocale.languageCode)
        return@runCatching response
    }

    suspend fun vote(pollId: String, choices: List<Int>): Result<Boolean> = runCatching {
        val response = api.vote(pollId, choices)
        return@runCatching response.id == pollId
    }

    suspend fun favourite(statusId: String, favourite: Boolean): Result<Status> = runCatching {
        val response: Status = if (favourite) {
            api.favourite(statusId)
        } else {
            api.unfavourite(statusId)
        }

        return@runCatching response
    }

    suspend fun boost(statusId: String, boost: Boolean): Result<Status> = runCatching {
        val response = if (boost) {
            api.boost(statusId)
        } else {
            api.unboost(statusId)
        }

        return@runCatching response
    }

    suspend fun bookmark(statusId: String, bookmark: Boolean): Result<Status> = runCatching {
        val response = if (bookmark) {
            api.bookmark(statusId)
        } else {
            api.unbookmark(statusId)
        }

        return@runCatching response
    }

    suspend fun pin(statusId: String, pin: Boolean): Result<Status> = runCatching {
        val response = if (pin) {
            api.pin(statusId)
        } else {
            api.unpin(statusId)
        }

        return@runCatching response
    }

    suspend fun mute(statusId: String, mute: Boolean): Result<Status> = runCatching {
        val response = if (mute) {
            api.mute(statusId)
        } else {
            api.unmute(statusId)
        }

        return@runCatching response
    }

    fun share(title: String, url: String): Result<Unit> = runCatching {
        tools.shareUrl(title, url)
    }

    fun openUrl(url: String): Result<Unit> = runCatching {
        val normalizedUrl = url.toNormalizedUrl()
        tools.openUrl(normalizedUrl)
    }

    fun buildContextActions(status: Status, isOwn: Boolean, translated: Boolean): Result<List<StatusContextAction>> = runCatching {
        val currentLocale = tools.getCurrentLocale()

        return@runCatching buildList {
            if (translated) {
                add(StatusContextAction.SHOW_ORIGINAL)
            } else if (status.language != currentLocale.languageCode) {
                add(StatusContextAction.TRANSLATE)
            }

            if (isOwn) {
                add(StatusContextAction.DELETE)

                if (status.pinned) {
                    add(StatusContextAction.UNPIN)
                } else {
                    add(StatusContextAction.PIN)
                }
            }

            if (status.bookmarked) {
                add(StatusContextAction.UNBOOKMARK)
            } else {
                add(StatusContextAction.BOOKMARK)
            }

            if (status.muted) {
                add(StatusContextAction.UNMUTE)
            } else {
                add(StatusContextAction.MUTE)
            }
        }
    }
}
