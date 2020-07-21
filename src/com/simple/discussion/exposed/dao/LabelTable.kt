package com.simple.discussion.exposed.dao

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

object LabelTable: IntIdTable() {
    val value = varchar("value", 64)
    val issue = reference("issue", IssueTable)
}

class LabelEntity(id: EntityID<Int>): IntEntity(id) {
    companion object: IntEntityClass<LabelEntity>(LabelTable)

    var value by LabelTable.value
    var issue by IssueEntity referencedOn LabelTable.issue
}