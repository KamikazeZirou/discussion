package com.simple.discussion.service

import com.simple.discussion.dao.IssueEntity
import com.simple.discussion.dao.IssueTable
import com.simple.discussion.model.Issue
import io.ktor.application.call
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.Routing
import io.ktor.routing.get
import io.ktor.routing.post
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
        IssueEntity.all().map { it.toModel()}
    }
}

fun Routing.issueRoute() {
    post("/issues") {
        val postedIssue = call.receive<Issue>()
        val savedIssue = IssueApplicationService.add(postedIssue)
        call.respond(savedIssue)
    }

    get("/issues") {
        val issues = IssueApplicationService.get()
        call.respond(issues)
    }
}