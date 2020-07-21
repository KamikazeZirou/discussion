package com.simple.discussion.exposed.respository

import com.simple.discussion.exposed.dao.CommentEntity
import com.simple.discussion.exposed.dao.CommentTable
import com.simple.discussion.model.Comment
import com.simple.discussion.repository.ICommentRepository
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

class ExposedCommentRepository : ICommentRepository {
    override fun add(issueId: Int, comment: Comment): Comment = transaction {
        SchemaUtils.create(CommentTable)

        CommentEntity.new {
            description = comment.description
            this.issueId = issueId
        }.toModel()
    }
}