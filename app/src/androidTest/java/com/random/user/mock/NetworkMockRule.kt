package com.random.user.mock

import androidx.test.platform.app.InstrumentationRegistry
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import org.junit.rules.TestWatcher
import org.junit.runner.Description

class NetworkMockRule : TestWatcher() {

    private val mockWebServer = MockWebServer()
    private val port = 1234

    override fun starting(description: Description?) {
        super.starting(description)

        // Port must be constant to avoid connection issues when running consecutive tests
        mockWebServer.start(port)
        mockWebServer.dispatcher = getDispatcher()
    }

    private fun getDispatcher(): Dispatcher {
        return object : Dispatcher() {
            override fun dispatch(request: RecordedRequest): MockResponse {
                val context = InstrumentationRegistry.getInstrumentation().targetContext

                if (request.path.contains("results")) {
                    val response = context.assets.open("users.json").bufferedReader().use {
                        it.readText()
                    }
                    return MockResponse().setResponseCode(200).setBody(response)
                }
                return MockResponse().setResponseCode(404)
            }
        }
    }

    override fun finished(description: Description?) {
        super.finished(description)
        mockWebServer.shutdown()
    }
}