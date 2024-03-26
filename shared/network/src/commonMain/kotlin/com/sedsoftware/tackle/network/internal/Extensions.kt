package com.sedsoftware.tackle.network.internal

import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.http.HttpHeaders

@Throws(Exception::class)
internal suspend inline fun <reified T> TackleUnauthorizedApi.doGet(url: String): T =
    httpClient
        .get(url) {
            headers {
                append(HttpHeaders.Accept, "*/*")
                append(HttpHeaders.UserAgent, Constants.USER_AGENT)
            }
        }
        .body()
