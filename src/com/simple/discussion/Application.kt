package com.simple.discussion

import com.simple.discussion.dao.IssueEntity
import com.simple.discussion.dao.IssueTable
import com.simple.discussion.model.Issue
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.ContentNegotiation
import io.ktor.http.ContentType
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.response.respondText
import io.ktor.routing.Routing
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.routing
import io.ktor.serialization.json
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {
    // DB_CLOSE_DELAY=-1は、コネクションを全て閉じても、DBをシャットダウンしないという意味
    // この指定がないとリクエストのたびに、Dataが消えてしまう
    Database.connect("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1", driver = "org.h2.Driver")

    install(ContentNegotiation) {
        json(
            contentType = ContentType.Application.Json
        )
    }

    routing {
        root()

        post("/issues") {
            val postedIssue = call.receive<Issue>()

            val savedIssue = transaction {
                SchemaUtils.create(IssueTable)
                IssueEntity.new {
                    title = postedIssue.title
                    description = postedIssue.description
                }.toModel()
            }

            call.respond(savedIssue)
        }

        get("/issues") {
            val issues = transaction {
                IssueEntity.all().map { it.toModel()}
            }

            call.respond(issues)
        }
    }
}

fun Routing.root() {
    get("/") {
        call.respondText("Hello, World!", ContentType.Text.Html)
    }
    get("/health_check") {
        call.respondText("OK", ContentType.Text.Html)
    }
}
