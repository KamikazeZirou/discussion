package com.simple.discussion.test.util

import com.simple.discussion.model.Issue
import io.ktor.application.ApplicationCall
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.server.testing.TestApplicationCall
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
fun TestApplicationEngine.postIssue(issue: Issue): Pair<ApplicationCall, Issue> {
    val call = handleRequest(HttpMethod.Post, "/issues") {
        addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
        setBody(Json.stringify(issue))
    }
    return call to Json.parse(Issue.serializer(), call.response.content!!)
}

@UnstableDefault
@OptIn(ImplicitReflectionSerializer::class)
fun TestApplicationEngine.getIssues(): Pair<TestApplicationCall, List<Issue>> {
    val call = handleRequest(HttpMethod.Get, "/issues") {}
    return call to Json.parseList(call.response.content!!)
}

@UnstableDefault
@OptIn(ImplicitReflectionSerializer::class)
fun TestApplicationEngine.putIssue(issue: Issue): ApplicationCall {
    return handleRequest(HttpMethod.Put, "/issues/${issue.id}") {
        addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
        setBody(Json.stringify(issue))
    }
}

@UnstableDefault
@OptIn(ImplicitReflectionSerializer::class)
fun TestApplicationEngine.deleteIssue(issueId: Int): ApplicationCall {
    return handleRequest(HttpMethod.Delete, "/issues/${issueId}") {
        addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
    }
}
