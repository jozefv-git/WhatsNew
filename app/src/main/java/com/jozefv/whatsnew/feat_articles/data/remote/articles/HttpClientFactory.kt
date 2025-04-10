package com.jozefv.whatsnew.feat_articles.data.remote.articles

import com.jozefv.whatsnew.BuildConfig
import com.jozefv.whatsnew.feat_articles.data.remote.articles.ApiDefaults.BASE_URL
import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

object HttpClientFactory {
    fun build(engine: HttpClientEngine): HttpClient {
        return HttpClient(engine) {
            install(Logging) {
                level = LogLevel.ALL // Log everything of our request
            }
            install(ContentNegotiation) {
                // Library for parsing data
                json(
                    json = Json {
                        // name of the specific json field
                        // if the JSON response includes field which we don't have parse for, ignore it
                        ignoreUnknownKeys = true
                    }
                )
            }
            defaultRequest {
                // Request expect Json input and Json output
                contentType(ContentType.Application.Json)
                url(BASE_URL)
                header("X-ACCESS-KEY", BuildConfig.API_KEY)
            }
        }
    }
}