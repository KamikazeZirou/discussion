package com.simple.discussion.test

import com.google.common.truth.Truth.assertThat
import com.simple.discussion.database.IDatabase
import com.simple.discussion.di.issueModule
import com.simple.discussion.model.Issue
import com.simple.discussion.module
import com.simple.discussion.test.util.postIssue
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

@ImplicitReflectionSerializer
@OptIn(UnstableDefault::class)
internal class IssueApiTest : KoinComponent {
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
    fun testPostIssue() = withTestApplication({ module(testing = true) }) {
        val call = handleRequest(HttpMethod.Post, "/issues") {
            addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            val issue = Issue(
                title = "test issue",
                description = "test description",
                labels = listOf("新機能")
            )
            setBody(Json.stringify(issue))
        }

        with(call) {
            assertThat(response.status()).isEqualTo(HttpStatusCode.Created)
            val issue = Json.parse(Issue.serializer(), response.content!!)
            assertThat(issue.title).isEqualTo("test issue")
            assertThat(issue.description).isEqualTo("test description")
            assertThat(issue.labels).isEqualTo(listOf("新機能"))
        }
    }

    @Test
    fun testGetIssue() = withTestApplication({ module(testing = true) }) {
        postIssue(Issue(
            title = "test issue1",
            description = "test description1",
            labels = listOf("新機能")))

        postIssue(Issue(
            title = "test issue2",
            description = "test description2",
            labels = listOf("不具合", "優先度高")
        ))

        val call = handleRequest(HttpMethod.Get, "/issues") {}

        with(call) {
            assertThat(response.status()).isEqualTo(HttpStatusCode.OK)
            val issues = Json.parseList<Issue>(response.content!!)
            assertThat(issues[0].title).isEqualTo("test issue1")
            assertThat(issues[0].description).isEqualTo("test description1")
            assertThat(issues[0].labels).isEqualTo(listOf("新機能"))
            assertThat(issues[1].title).isEqualTo("test issue2")
            assertThat(issues[1].description).isEqualTo("test description2")
            assertThat(issues[1].labels).isEqualTo(listOf("不具合", "優先度高"))
        }
    }

    @Test
    fun testPutIssue() = withTestApplication({ module(testing = true) }) {
        val postedIssue = postIssue(Issue(
            title = "test issue1",
            description = "test description1",
            labels = listOf("新機能")))

        val putCall = handleRequest(HttpMethod.Put, "/issues/${postedIssue.id}") {
            addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            val issue = postedIssue.copy(
                title = "test issue2",
                description = "test description2",
                labels = listOf()
            )
            setBody(Json.stringify(issue))
        }

        with(putCall) {
            assertThat(response.status()).isEqualTo(HttpStatusCode.NoContent)
        }

        val getCall = handleRequest(HttpMethod.Get, "/issues") {}

        with(getCall) {
            assertThat(response.status()).isEqualTo(HttpStatusCode.OK)
            val issues = Json.parseList<Issue>(response.content!!)
            assertThat(issues[0].title).isEqualTo("test issue2")
            assertThat(issues[0].description).isEqualTo("test description2")
            assertThat(issues[0].labels).isEqualTo(listOf<String>())
        }
    }

    @Test
    fun testDeleteIssue() = withTestApplication({ module(testing = true) }) {
        val postedIssue = postIssue(
            Issue(
                title = "test issue1",
                description = "test description1",
                labels = listOf("新機能")
            )
        )

        val deleteCall = handleRequest(HttpMethod.Delete, "/issues/${postedIssue.id}") {
            addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
        }

        with(deleteCall) {
            assertThat(response.status()).isEqualTo(HttpStatusCode.NoContent)
        }

        val getCall = handleRequest(HttpMethod.Get, "/issues") {}

        with(getCall) {
            assertThat(response.status()).isEqualTo(HttpStatusCode.OK)
            val issues = Json.parseList<Issue>(response.content!!)
            assertThat(issues).isEmpty()
        }
    }
}