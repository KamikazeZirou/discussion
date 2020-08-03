package com.simple.discussion.exposed.respository

import com.simple.discussion.exposed.dao.CommentEntity
import com.simple.discussion.exposed.dao.CommentTable
import com.simple.discussion.model.Comment
import com.simple.discussion.repository.ICommentRepository
import org.jetbrains.exposed.sql.transactions.transaction

class ExposedCommentRepository : ICommentRepository {
    override fun add(issueId: Int, comment: Comment): Comment = transaction {
        CommentEntity.new {
            description = comment.description
            this.issueId = issueId
        }.toModel()
    }

    override fun update(comment: Comment): Comment = transaction {
        CommentEntity[comment.id].apply {
            description = comment.description
            flush()
        }.toModel()
    }

    override fun get(issueId: Int): List<Comment> = transaction {
        CommentEntity.find { CommentTable.issueId eq issueId }
            .map { it.toModel() }
    }

    override fun delete(commentId: Int) = transaction {
        CommentEntity[commentId].delete()
    }
}