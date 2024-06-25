package com.sedsoftware.tackle.network.mappers

import com.sedsoftware.tackle.network.model.Token
import com.sedsoftware.tackle.network.response.TokenResponse

internal object TokenMapper {

    fun map(from: TokenResponse): Token =
        Token(
            accessToken = from.accessToken,
            tokenType = from.tokenType,
            scope = from.scope,
            createdAt = from.createdAt,
        )
}
