package com.sedsoftware.tackle.network.mappers

import com.sedsoftware.tackle.domain.model.Role
import com.sedsoftware.tackle.network.response.RoleResponse

internal object RoleMapper {

    fun map(from: RoleResponse): Role =
        Role(
            id = from.id,
            name = from.name,
            color = from.color,
            permissions = from.permissions,
            highlighted = from.highlighted,
        )
}
