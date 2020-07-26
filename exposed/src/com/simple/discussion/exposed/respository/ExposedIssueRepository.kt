package com.simple.discussion.exposed.respository

import com.simple.discussion.exposed.dao.IssueEntity
import com.simple.discussion.exposed.dao.IssueTable
import com.simple.discussion.exposed.dao.LabelEntity
import com.simple.discussion.exposed.dao.LabelTable
import com.simple.discussion.model.Issue
import com.simple.discussion.repository.IIssueRepository
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.transactions.transaction

class ExposedIssueRepository : IIssueRepository {
    override fun add(issue: Issue) = transaction {
        SchemaUtils.create(LabelTable)
        SchemaUtils.create(IssueTable)

        val addedIssue = IssueEntity.new {
            title = issue.title
            description = issue.description
        }

        issue.labels.forEach {
            LabelEntity.new {
                this.value = it
                this.issue = addedIssue
            }
        }

        addedIssue.toModel()
    }

    override fun get(): List<Issue> = transaction {
        IssueEntity.all().map { it.toModel() }
    }

    override fun update(issue: Issue): Issue = transaction {
        val updatedIssue = IssueEntity[issue.id].apply {
            title = issue.title
            description = issue.description
            flush()
        }

        // 一旦全てのラベルを消す
        LabelTable.deleteWhere { LabelTable.issue.eq(updatedIssue.id) }

        // ラベルを作りなおす
        issue.labels.forEach {
            LabelEntity.new {
                this.value = it
                this.issue = updatedIssue
            }
        }

        updatedIssue.toModel()
    }

    override fun delete(issueId: Int) = transaction {
        IssueEntity[issueId].delete()
    }
}