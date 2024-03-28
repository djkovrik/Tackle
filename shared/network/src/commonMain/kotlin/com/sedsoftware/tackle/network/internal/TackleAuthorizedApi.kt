package com.sedsoftware.tackle.network.internal

import com.sedsoftware.tackle.network.api.AuthorizedApi
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

class TackleAuthorizedApi : AuthorizedApi {

    internal val httpClient: HttpClient = HttpClient() {
        install(ContentNegotiation) {
            json(Json {
                coerceInputValues = true
                ignoreUnknownKeys = true
                useAlternativeNames = false
            })
        }
    }
}
