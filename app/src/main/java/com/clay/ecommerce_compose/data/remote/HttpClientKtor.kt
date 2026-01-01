package com.clay.ecommerce_compose.data.remote

import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

object HttpClientKtor {

    val client = HttpClient(OkHttp) {

        install(ContentNegotiation) {
            json(
                Json {
                    ignoreUnknownKeys=true
                    isLenient=true
                    encodeDefaults=true
                }
            )
        }

        expectSuccess=true

    }

}
