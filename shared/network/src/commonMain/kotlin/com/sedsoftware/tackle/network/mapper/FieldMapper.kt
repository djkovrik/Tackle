package com.sedsoftware.tackle.network.mapper

import com.sedsoftware.tackle.domain.model.Field
import com.sedsoftware.tackle.network.response.FieldResponse
import com.sedsoftware.tackle.utils.extension.toLocalDateTime

internal object FieldMapper {

    fun map(from: FieldResponse): Field =
        Field(
            name = from.name,
            value = from.value,
            verifiedAt = from.verifiedAt.toLocalDateTime()
        )
}
