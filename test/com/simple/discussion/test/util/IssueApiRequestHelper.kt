package com.simple.discussion.test.util

import com.simple.discussion.model.Issue
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.server.testing.TestApplicationEngine
import io.ktor.server.testing.handleRequest
import io.ktor.server.testing.setBody
import kotlinx.serialization.ImplicitReflectionSerializer
import kotlinx.serialization.UnstableDefault
import kotlinx.serialization.json.Json
import kotlinx.serialization.stringify

@UnstableDefault
@OptIn(ImplicitReflectionSerializer::class)
fun TestApplicationEngine.postIssue(issue: Issue): Issue {
    // Issueを投稿する
    val call = handleRequest(HttpMethod.Post, "/issues") {
        addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
        setBody(Json.stringify(issue))
    }
    return Json.parse(Issue.serializer(), call.response.content!!)
}