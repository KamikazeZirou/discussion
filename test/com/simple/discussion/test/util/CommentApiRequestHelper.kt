package com.simple.discussion.test.util

import com.simple.discussion.model.Comment
import io.ktor.application.ApplicationCall
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.server.testing.TestApplicationEngine
import io.ktor.server.testing.handleRequest
import io.ktor.server.testing.setBody
import kotlinx.serialization.ImplicitReflectionSerializer
import kotlinx.serialization.UnstableDefault
import kotlinx.serialization.json.Json
import kotlinx.serialization.parseList
import kotlinx.serialization.stringify

@UnstableDefault
@OptIn(ImplicitReflectionSerializer::class)
fun TestApplicationEngine.postComment(issueId: Int, comment: Comment): Pair<ApplicationCall, Comment> {
    val call = handleRequest(HttpMethod.Post, "/issues/$issueId/comments") {
        addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
        setBody(Json.stringify(comment))
    }
    return call to Json.parse(Comment.serializer(), call.response.content!!)
}

@UnstableDefault
@OptIn(ImplicitReflectionSerializer::class)
fun TestApplicationEngine.getComments(issueId: Int): Pair<ApplicationCall, List<Comment>> {
    val call = handleRequest(HttpMethod.Get, "/issues/$issueId/comments") {
        addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
    }
    return call to Json.parseList<Comment>(call.response.content!!)
}

@UnstableDefault
@OptIn(ImplicitReflectionSerializer::class)
fun TestApplicationEngine.putComment(issueId: Int, comment: Comment): ApplicationCall {
    return handleRequest(HttpMethod.Put, "/issues/${issueId}/comments/${comment.id}") {
        addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
        setBody(Json.stringify(comment))
    }
}

@UnstableDefault
@OptIn(ImplicitReflectionSerializer::class)
fun TestApplicationEngine.deleteComment(issueId: Int, commentId: Int): ApplicationCall {
    return handleRequest(HttpMethod.Delete, "/issues/${issueId}/comments/$commentId") {
        addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
    }
}
