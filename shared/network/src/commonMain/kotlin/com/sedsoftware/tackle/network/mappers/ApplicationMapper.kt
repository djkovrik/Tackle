package com.sedsoftware.tackle.network.mappers

import com.sedsoftware.tackle.network.model.Application
import com.sedsoftware.tackle.network.response.ApplicationResponse

internal object ApplicationMapper {

    fun map(from: ApplicationResponse): Application =
        Application(
            name = from.name,
            website = from.website.orEmpty(),
            clientId = from.clientId.orEmpty(),
            clientSecret = from.clientSecret.orEmpty(),
        )
}
