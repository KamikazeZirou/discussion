package com.simple.discussion.exposed.database.h2

import com.simple.discussion.database.IDatabase
import org.jetbrains.exposed.sql.Database

class H2Database: IDatabase {
    private var database: Database? = null

    override fun connect() {
        database = Database.connect("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1", driver = "org.h2.Driver")
    }

    override fun cleanup() {
        database?.let {
            val e = it.connector.invoke().prepareStatement("DROP ALL OBJECTS", arrayOf())
            e.executeUpdate()
            it.connector.invoke().close()
        }
    }
}