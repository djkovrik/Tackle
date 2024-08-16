package com.sedsoftware.tackle.network.mapper

import com.sedsoftware.tackle.domain.model.Token
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
