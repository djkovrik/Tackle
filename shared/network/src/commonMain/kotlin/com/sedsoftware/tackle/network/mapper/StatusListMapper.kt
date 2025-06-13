package com.sedsoftware.tackle.network.mapper

import com.sedsoftware.tackle.domain.model.Status
import com.sedsoftware.tackle.network.response.StatusResponse

internal object StatusListMapper {

    fun map(from: List<StatusResponse>): List<Status> =
        from.map(StatusMapper::map)
}
