package com.simple.discussion.service

import com.simple.discussion.dao.IssueEntity
import com.simple.discussion.dao.IssueTable
import com.simple.discussion.model.Issue
import io.ktor.application.call
import io.ktor.http.HttpHeaders.Location
import io.ktor.http.HttpStatusCode
import io.ktor.request.receive
import io.ktor.response.header
import io.ktor.response.respond
import io.ktor.routing.*
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

object IssueApplicationService {
    fun add(issue: Issue) = transaction {
        SchemaUtils.create(IssueTable)
        IssueEntity.new {
            title = issue.title
            description = issue.description
        }.toModel()
    }

    fun get() = transaction {
        IssueEntity.all().map { it.toModel() }
    }

    fun update(updatedIssue: Issue) = transaction {
        IssueEntity[updatedIssue.id].apply {
            title = updatedIssue.title
            description = updatedIssue.description
            flush()
        }.toModel()
    }

    fun delete(issueId: Int) = transaction {
        IssueEntity[issueId].delete()
    }
}

fun Routing.issueRoute() {
    post("/issues") {
        val postedIssue = call.receive<Issue>()
        val savedIssue = IssueApplicationService.add(postedIssue)

        call.response.header(Location, "/issues/${savedIssue.id}")
        call.respond(HttpStatusCode.Created, savedIssue)
    }

    get("/issues") {
        val issues = IssueApplicationService.get()

        call.respond(HttpStatusCode.OK, issues)
    }

    put("/issues/{id}") {
        val issueId = call.parameters["id"]?.toInt()

        issueId?.let {
            val postedIssue = call.receive<Issue>()
                .copy(id = it)
            IssueApplicationService.update(postedIssue)
        }

        call.respond(HttpStatusCode.NoContent)
    }

    delete("/issues/{id}") {
        val issueId = call.parameters["id"]?.toInt()
        issueId?.let {
            IssueApplicationService.delete(it)
        }
        call.respond(HttpStatusCode.NoContent)
    }
}
