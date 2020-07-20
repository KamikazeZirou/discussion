package com.simple.discussion.exposed.dao

import com.simple.discussion.model.Issue
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

object IssueTable: IntIdTable() {
    val title = varchar("name", 255)
    val description = text("description")
}

class IssueEntity(id: EntityID<Int>): IntEntity(id) {
    companion object: IntEntityClass<IssueEntity>(IssueTable)

    var title by IssueTable.title
    var description by IssueTable.description
    private val labels by LabelEntity referrersOn LabelTable.issue

    fun toModel(): Issue = Issue(
        id.value,
        title,
        description,
        labels.map { it.value }
    )
}