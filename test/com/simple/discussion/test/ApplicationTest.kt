package com.simple.discussion.test

import com.google.common.truth.Truth.assertThat
import com.simple.discussion.database.IDatabase
import com.simple.discussion.di.issueModule
import com.simple.discussion.model.Issue
import com.simple.discussion.module
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.handleRequest
import io.ktor.server.testing.setBody
import io.ktor.server.testing.withTestApplication
import kotlinx.serialization.ImplicitReflectionSerializer
import kotlinx.serialization.UnstableDefault
import kotlinx.serialization.json.Json
import kotlinx.serialization.parseList
import kotlinx.serialization.stringify
import org.junit.After
import org.junit.Before
import org.koin.core.KoinComponent
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.core.inject
import kotlin.test.Test

@OptIn(UnstableDefault::class)
internal class ApplicationTest: KoinComponent {
    private val database: IDatabase by inject()

    @Before
    fun setUp() {
        startKoin {
            modules(issueModule)
        }
        database.connect()
    }

    @After
    fun teardown() {
        stopKoin()
        database.cleanup()
    }

    @Test
    fun testHealthCheck() = withTestApplication( { module(testing = true) }) {
        with(handleRequest(HttpMethod.Get, "/health_check")) {
            assertThat(response.status()).isEqualTo(HttpStatusCode.OK)
            assertThat(response.content).isEqualTo("OK")
        }
    }

    @ImplicitReflectionSerializer
    @Test
    fun testPostIssue() = withTestApplication( { module(testing = true) }) {

        val call = handleRequest(HttpMethod.Post, "/issues") {
            addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            val issue = Issue(title = "test issue", description = "test description")
            setBody(Json.stringify(issue))
        }

        with(call) {
            assertThat(response.status()).isEqualTo(HttpStatusCode.Created)
            val issue = Json.parse(Issue.serializer(), response.content!!)
            assertThat(issue.title).isEqualTo("test issue")
            assertThat(issue.description).isEqualTo("test description")
        }
    }

    @ImplicitReflectionSerializer
    @Test
    fun testGetIssue() = withTestApplication( { module(testing = true) }) {

        handleRequest(HttpMethod.Post, "/issues") {
            addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            val issue = Issue(title = "test issue1", description = "test description1")
            setBody(Json.stringify(issue))
        }

        handleRequest(HttpMethod.Post, "/issues") {
            addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            val issue = Issue(title = "test issue2", description = "test description2")
            setBody(Json.stringify(issue))
        }

        val call = handleRequest(HttpMethod.Get, "/issues") {
        }

        with(call) {
            assertThat(response.status()).isEqualTo(HttpStatusCode.OK)
            val issues = Json.parseList<Issue>(response.content!!)
            assertThat(issues[0].title).isEqualTo("test issue1")
            assertThat(issues[0].description).isEqualTo("test description1")
            assertThat(issues[1].title).isEqualTo("test issue2")
            assertThat(issues[1].description).isEqualTo("test description2")
        }
    }
}