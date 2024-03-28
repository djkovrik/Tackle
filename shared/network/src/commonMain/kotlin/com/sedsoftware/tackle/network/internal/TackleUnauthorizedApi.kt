package com.sedsoftware.tackle.network.internal

import com.sedsoftware.tackle.network.api.UnauthorizedApi
import com.sedsoftware.tackle.network.response.InstanceDetails
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

internal class TackleUnauthorizedApi : UnauthorizedApi {

    internal val httpClient: HttpClient = HttpClient() {
        install(ContentNegotiation) {
            json(Json {
                coerceInputValues = true
                ignoreUnknownKeys = true
                useAlternativeNames = false
            })
        }
    }

    override suspend fun getServerInfo(url: String): InstanceDetails =
        doGet<InstanceDetails>("$url/api/v2/instance")
}
