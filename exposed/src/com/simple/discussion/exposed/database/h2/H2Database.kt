package com.simple.discussion.exposed.database.h2

import com.simple.discussion.database.IDatabase
import com.simple.discussion.exposed.dao.CommentTable
import com.simple.discussion.exposed.dao.IssueTable
import com.simple.discussion.exposed.dao.LabelTable
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

class H2Database: IDatabase {
    private var database: Database? = null

    override fun connect() {
        database = Database.connect("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1", driver = "org.h2.Driver")

        transaction {
            SchemaUtils.create(LabelTable)
            SchemaUtils.create(IssueTable)
            SchemaUtils.create(CommentTable)
        }
    }

    override fun cleanup() {
        database?.let {
            val e = it.connector.invoke().prepareStatement("DROP ALL OBJECTS", arrayOf())
            e.executeUpdate()
            it.connector.invoke().close()
        }
    }
}