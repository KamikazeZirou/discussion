package com.simple.discussion.test

import com.google.common.truth.Truth.assertThat
import com.simple.discussion.database.IDatabase
import com.simple.discussion.di.issueModule
import com.simple.discussion.model.Comment
import com.simple.discussion.model.Issue
import com.simple.discussion.module
import com.simple.discussion.test.util.*
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.withTestApplication
import kotlinx.serialization.ImplicitReflectionSerializer
import kotlinx.serialization.UnstableDefault
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

    @Test
    fun testPostComment() = withTestApplication({ module(testing = true) }) {
        val (_, issue) = postIssue(
            Issue(
                title = "test issue",
                description = "test description",
                labels = listOf()
            )
        )

        // Commentを投稿する
        val (postCommentCall, postedComment) = postComment(issue.id,
            Comment(description = "test comment")
        )
        with(postCommentCall) {
            assertThat(response.status()).isEqualTo(HttpStatusCode.Created)
            assertThat(response.headers[HttpHeaders.Location]).isEqualTo("/issues/${issue.id}/comments/${postedComment.id}")
            assertThat(postedComment.id).isGreaterThan(0)
            assertThat(postedComment.description).isEqualTo("test comment")
        }

        // 実際に投稿できているか確認する
        val (getCommentCall, comments) = getComments(issue.id)
        with(getCommentCall) {
            assertThat(response.status()).isEqualTo(HttpStatusCode.OK)
            assertThat(comments[0].id).isEqualTo(postedComment.id)
            assertThat(comments[0].description).isEqualTo("test comment")
        }
    }

    @Test
    fun testGetComment() = withTestApplication({ module(testing = true) }) {
        val (_, issue) = postIssue(
            Issue(
                title = "test issue",
                description = "test description",
                labels = listOf()
            )
        )

        // Commentを投稿する
        val (_, postedComment1) = postComment(issue.id,
            Comment(description = "test comment1")
        )
        val (_, postedComment2) = postComment(issue.id,
            Comment(description = "test comment2")
        )

        // 実際に投稿できているか確認する
        val (getCommentCall, comments) = getComments(issue.id)
        with(getCommentCall) {
            assertThat(response.status()).isEqualTo(HttpStatusCode.OK)
            assertThat(comments[0].id).isEqualTo(postedComment1.id)
            assertThat(comments[0].description).isEqualTo("test comment1")
            assertThat(comments[1].id).isEqualTo(postedComment2.id)
            assertThat(comments[1].description).isEqualTo("test comment2")
        }
    }

    @Test
    fun testPutComment() = withTestApplication({ module(testing = true) }) {
        val (_, issue) = postIssue(
            Issue(
                title = "test issue",
                description = "test description",
                labels = listOf()
            )
        )

        // Commentを投稿する
        val (_, postedComment) = postComment(issue.id,
            Comment(description = "test comment")
        )

        // コメントを更新する
        val putCommentCall = putComment(issue.id, postedComment.copy(
            description = "test comment2"
        ))
        with(putCommentCall) {
            assertThat(response.status()).isEqualTo(HttpStatusCode.NoContent)
        }

        // 実際に更新できたか確認する
        val (getCommentCall, comments) = getComments(issue.id)
        with(getCommentCall) {
            assertThat(response.status()).isEqualTo(HttpStatusCode.OK)
            assertThat(comments[0].id).isEqualTo(postedComment.id)
            assertThat(comments[0].description).isEqualTo("test comment2")
        }
    }

    @Test
    fun testDeleteComment() = withTestApplication({ module(testing = true) }) {
        val (_, issue) = postIssue(
            Issue(
                title = "test issue",
                description = "test description",
                labels = listOf()
            )
        )

        // Commentを投稿する
        val (_, postedComment) = postComment(issue.id,
            Comment(description = "test comment")
        )

        // コメントを削除する
        val deleteCommentCall = deleteComment(issue.id, postedComment.id)
        with(deleteCommentCall) {
            assertThat(response.status()).isEqualTo(HttpStatusCode.NoContent)
        }

        // 実際に削除できたか確認する
        val (getCommentCall, comments) = getComments(issue.id)
        with(getCommentCall) {
            assertThat(response.status()).isEqualTo(HttpStatusCode.OK)
            assertThat(comments).isEmpty()
        }
    }
}