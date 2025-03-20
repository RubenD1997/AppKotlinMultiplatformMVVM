package org.example.project.core

import io.ktor.client.HttpClient
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.example.project.core.Constants.BASE_URL


object HttpClientProvider {

    fun create(): HttpClient {
        return HttpClient {
            install(ContentNegotiation) {
                json(Json { ignoreUnknownKeys = true })
            }
            install(DefaultRequest) {
                url(BASE_URL)
                contentType(ContentType.Application.Json)
            }
        }
    }
}