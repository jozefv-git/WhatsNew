package com.jozefv.whatsnew

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.HttpClientEngineConfig
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.MockEngineConfig
import io.ktor.client.request.HttpRequestData
import io.ktor.client.request.HttpResponseData
import io.ktor.util.InternalAPI
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

class TestKtorEngine(
    private val mockEngineConfig: MockEngineConfig,
    override val dispatcher: CoroutineDispatcher
) : HttpClientEngine {
    val mockEngine = MockEngine(mockEngineConfig)

    // All asynchronous code from our mockEngine will be run on our own test dispatcher
    override val coroutineContext: CoroutineContext
        get() = mockEngine.coroutineContext.plus(dispatcher)

    override val config: HttpClientEngineConfig
        get() = mockEngineConfig

    // Called when we want to execute a certain request
    // httpRequestData with which we want to execute the request with
    @InternalAPI
    override suspend fun execute(data: HttpRequestData): HttpResponseData {
        // for every single call we made with our mockEngine - switch our context to context with
        // corresponding test dispatcher
        return withContext(coroutineContext) {
            mockEngine.execute(data)
        }
    }

    override fun close() {
        mockEngine.close()
    }
}