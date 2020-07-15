package com.simple.discussion

import com.simple.discussion.di.issueModule
import com.simple.discussion.service.issueRoute
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.ContentNegotiation
import io.ktor.http.ContentType
import io.ktor.response.respondText
import io.ktor.routing.Routing
import io.ktor.routing.get
import io.ktor.routing.routing
import io.ktor.serialization.json
import org.jetbrains.exposed.sql.Database
import org.koin.core.context.startKoin

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {

    if (!testing) {
        // DB_CLOSE_DELAY=-1は、コネクションを全て閉じても、DBをシャットダウンしないという意味
        // この指定がないとリクエストのたびに、Dataが消えてしまう
        Database.connect("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1", driver = "org.h2.Driver")
    }
    startKoin {
        modules(issueModule)
    }

    install(ContentNegotiation) {
        json(
            contentType = ContentType.Application.Json
        )
    }

    routing {
        root()
        issueRoute()
    }
}

fun Routing.root() {
    get("/health_check") {
        call.respondText("OK", ContentType.Text.Html)
    }
}
