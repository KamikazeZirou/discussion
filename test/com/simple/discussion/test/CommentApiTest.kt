package com.simple.discussion.test

import com.google.common.truth.Truth.assertThat
import com.simple.discussion.database.IDatabase
import com.simple.discussion.di.issueModule
import com.simple.discussion.model.Comment
import com.simple.discussion.model.Issue
import com.simple.discussion.module
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.server.testing.TestApplicationEngine
import io.ktor.server.testing.handleRequest
import io.ktor.server.testing.setBody
import io.ktor.server.testing.withTestApplication
import kotlinx.serialization.ImplicitReflectionSerializer
import kotlinx.serialization.UnstableDefault
import kotlinx.serialization.json.Json
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
internal class CommentApiTest : KoinComponent {
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

    private fun TestApplicationEngine.postIssue(): Issue {
        // Issueを投稿する
        val postIssueCall = handleRequest(HttpMethod.Post, "/issues") {
            addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            val issue = Issue(
                title = "test issue",
                description = "test description",
                labels = listOf()
            )
            setBody(Json.stringify(issue))
        }
        return Json.parse(Issue.serializer(), postIssueCall.response.content!!)
    }

    @Test
    fun testPostComment() = withTestApplication({ module(testing = true) }) {
        val issue = postIssue()

        // Commentを投稿する
        val postCommentCall = handleRequest(HttpMethod.Post, "/issues/${issue.id}/comments") {
            addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            val comment = Comment(description = "test comment")
            setBody(Json.stringify(comment))
        }

        // Commentを投稿できたか確認する
        with(postCommentCall) {
            assertThat(response.status()).isEqualTo(io.ktor.http.HttpStatusCode.Created)
            val comment = Json.parse(Comment.serializer(), response.content!!)
            assertThat(comment.id).isGreaterThan(0)
            assertThat(comment.description).isEqualTo("test comment")
        }
    }
}