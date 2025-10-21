package com.sedsoftware.tackle.main.gateway

import com.sedsoftware.tackle.domain.api.AuthorizedApi
import com.sedsoftware.tackle.domain.model.Poll
import com.sedsoftware.tackle.domain.model.Status
import com.sedsoftware.tackle.domain.model.Translation
import com.sedsoftware.tackle.domain.model.params.ParamsContent
import com.sedsoftware.tackle.domain.model.params.ParamsPagination
import com.sedsoftware.tackle.main.StatusComponentGateways

internal class StatusComponentApi(
    private val authorizedApi: AuthorizedApi,
) : StatusComponentGateways.Api {

    override suspend fun delete(statusId: String, deleteMedia: Boolean): Status =
        authorizedApi.deleteStatus(statusId, deleteMedia)

    override suspend fun translate(statusId: String, lang: String): Translation =
        authorizedApi.translateStatus(statusId, lang)

    override suspend fun vote(pollId: String, choices: List<Int>): Poll =
        authorizedApi.votePoll(pollId, choices)

    override suspend fun favourite(statusId: String): Status =
        authorizedApi.favouriteStatus(statusId)

    override suspend fun unfavourite(statusId: String): Status =
        authorizedApi.unfavouriteStatus(statusId)

    override suspend fun boost(statusId: String): Status =
        authorizedApi.boostStatus(statusId)

    override suspend fun unboost(statusId: String): Status =
        authorizedApi.unboostStatus(statusId)

    override suspend fun bookmark(statusId: String): Status =
        authorizedApi.bookmarkStatus(statusId)

    override suspend fun unbookmark(statusId: String): Status =
        authorizedApi.unbookmarkStatus(statusId)

    override suspend fun pin(statusId: String): Status =
        authorizedApi.pinStatus(statusId)

    override suspend fun unpin(statusId: String): Status =
        authorizedApi.unpinStatus(statusId)

    override suspend fun mute(statusId: String): Status =
        authorizedApi.muteStatus(statusId)

    override suspend fun unmute(statusId: String): Status =
        authorizedApi.unmuteStatus(statusId)

    override suspend fun homeTimeline(pagination: ParamsPagination): List<Status> =
        authorizedApi.getHomeTimeline(
            maxId = pagination.maxId,
            sinceId = pagination.sinceId,
            minId = pagination.minId,
            limit = pagination.limit,
        )

    override suspend fun publicTimeline(pagination: ParamsPagination, content: ParamsContent?): List<Status> =
        authorizedApi.getPublicTimeline(
            local = content?.local,
            remote = content?.remote,
            onlyMedia = content?.onlyMedia,
            maxId = pagination.maxId,
            sinceId = pagination.sinceId,
            minId = pagination.minId,
            limit = pagination.limit,
        )

    override suspend fun hashTagTimeline(hashTag: String, pagination: ParamsPagination, content: ParamsContent?): List<Status> =
        authorizedApi.getHashTagTimeline(
            hashTag = hashTag,
            local = content?.local,
            remote = content?.remote,
            onlyMedia = content?.onlyMedia,
            maxId = pagination.maxId,
            sinceId = pagination.sinceId,
            minId = pagination.minId,
            limit = pagination.limit,
        )

    override suspend fun listTimeline(listId: String, pagination: ParamsPagination): List<Status> =
        authorizedApi.getListTimeline(
            listId = listId,
            maxId = pagination.maxId,
            sinceId = pagination.sinceId,
            minId = pagination.minId,
            limit = pagination.limit,
        )
}
