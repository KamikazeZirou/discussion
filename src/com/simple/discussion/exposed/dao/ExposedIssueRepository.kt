package com.simple.discussion.exposed.dao

import com.simple.discussion.model.Issue
import com.simple.discussion.repository.IIssueRepository
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

class ExposedIssueRepository : IIssueRepository {
    override fun add(issue: Issue) = transaction {
        SchemaUtils.create(IssueTable)
        IssueEntity.new {
            title = issue.title
            description = issue.description
        }.toModel()
    }

    override fun get(): List<Issue> = transaction {
        IssueEntity.all().map { it.toModel() }
    }

    override fun update(issue: Issue): Issue = transaction {
        IssueEntity[issue.id].apply {
            title = issue.title
            description = issue.description
            flush()
        }.toModel()
    }

    override fun delete(issueId: Int) = transaction {
        IssueEntity[issueId].delete()
    }
}