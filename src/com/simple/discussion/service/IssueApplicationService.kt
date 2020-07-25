package com.simple.discussion.service

import com.simple.discussion.model.Issue
import com.simple.discussion.repository.IIssueRepository
import io.ktor.application.call
import io.ktor.http.HttpHeaders.Location
import io.ktor.http.HttpStatusCode
import io.ktor.request.receive
import io.ktor.response.header
import io.ktor.response.respond
import io.ktor.routing.*
import org.koin.core.KoinComponent
import org.koin.core.inject

object IssueApplicationService: KoinComponent {
    private val repository: IIssueRepository by inject()

    fun add(issue: Issue) = repository.add(issue)

    fun get() = repository.get()

    fun update(issue: Issue) = repository.update(issue)

    fun delete(issueId: Int) = repository.delete(issueId)
}

fun Routing.issueRoute() {
    route("/issues") {
        post {
            val postedIssue = call.receive<Issue>()
            val savedIssue = IssueApplicationService.add(postedIssue)

            call.response.header(Location, "/issues/${savedIssue.id}")
            call.respond(HttpStatusCode.Created, savedIssue)
        }

        get {
            val issues = IssueApplicationService.get()

            call.respond(HttpStatusCode.OK, issues)
        }

        put("{id}") {
            val issueId = call.parameters["id"]?.toInt()

            issueId?.let {
                val postedIssue = call.receive<Issue>()
                    .copy(id = it)
                IssueApplicationService.update(postedIssue)
            }

            call.respond(HttpStatusCode.NoContent)
        }

        delete("{id}") {
            val issueId = call.parameters["id"]?.toInt()
            issueId?.let {
                IssueApplicationService.delete(it)
            }
            call.respond(HttpStatusCode.NoContent)
        }
    }
}
