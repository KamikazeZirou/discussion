package com.simple.discussion.exposed.dao

import com.simple.discussion.model.Comment
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

object CommentTable: IntIdTable() {
    val description = text("description")
    val issueId = integer("issue_id").references(IssueTable.id)
}

class CommentEntity(id: EntityID<Int>): IntEntity(id) {
    companion object: IntEntityClass<CommentEntity>(CommentTable)

    var description by CommentTable.description
    var issueId by  CommentTable.issueId

    fun toModel(): Comment = Comment(id.value, description)
}