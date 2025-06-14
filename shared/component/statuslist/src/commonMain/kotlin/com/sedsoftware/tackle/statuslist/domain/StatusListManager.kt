package com.sedsoftware.tackle.statuslist.domain

import com.sedsoftware.tackle.domain.model.Status
import com.sedsoftware.tackle.domain.model.params.ParamsPagination
import com.sedsoftware.tackle.domain.model.type.Timeline
import com.sedsoftware.tackle.status.StatusComponentGateways

internal class StatusListManager(
    private val timeline: Timeline,
    private val api: StatusComponentGateways.Api,
) {

    suspend fun loadStatusList(pageSize: Int, maxId: String? = null): Result<List<Status>> =
        runCatching {
            val result: List<Status> = when (timeline) {
                is Timeline.Home -> api.homeTimeline(
                    pagination = ParamsPagination(
                        limit = pageSize,
                        maxId = maxId,
                    )
                )

                is Timeline.Public -> api.publicTimeline(
                    pagination = ParamsPagination(
                        limit = pageSize,
                        maxId = maxId,
                    ),
                    content = null,
                )

                is Timeline.HashTag -> api.hashTagTimeline(
                    hashTag = timeline.hashtag,
                    pagination = ParamsPagination(
                        limit = pageSize,
                        maxId = maxId,
                    ),
                    content = null,
                )

                is Timeline.List -> api.listTimeline(
                    listId = timeline.listId,
                    pagination = ParamsPagination(
                        limit = pageSize,
                        maxId = maxId,
                    )
                )
            }

            return@runCatching result
        }
}
