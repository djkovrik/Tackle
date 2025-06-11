package com.sedsoftware.tackle.status

import com.sedsoftware.tackle.domain.model.AppLocale
import com.sedsoftware.tackle.domain.model.Poll
import com.sedsoftware.tackle.domain.model.Status
import com.sedsoftware.tackle.domain.model.Translation

interface StatusComponentGateways {
    interface Api {
        suspend fun delete(statusId: String, deleteMedia: Boolean): Status
        suspend fun translate(statusId: String, lang: String) : Translation
        suspend fun vote(pollId: String, choices: List<Int>): Poll
        suspend fun favourite(statusId: String): Status
        suspend fun unfavourite(statusId: String): Status
        suspend fun boost(statusId: String): Status
        suspend fun unboost(statusId: String): Status
        suspend fun bookmark(statusId: String): Status
        suspend fun unbookmark(statusId: String): Status
        suspend fun pin(statusId: String): Status
        suspend fun unpin(statusId: String): Status
        suspend fun mute(statusId: String): Status
        suspend fun unmute(statusId: String): Status
    }

    interface Tools {
        fun getCurrentLocale(): AppLocale
        fun openUrl(url: String)
        fun shareUrl(title: String, url: String)
    }
}
