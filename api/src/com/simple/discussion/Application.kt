package com.simple.discussion

import com.simple.discussion.database.IDatabase
import com.simple.discussion.di.issueModule
import com.simple.discussion.service.commentRoute
import com.simple.discussion.service.issueRoute
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.CORS
import io.ktor.features.ContentNegotiation
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.response.respondText
import io.ktor.routing.Routing
import io.ktor.routing.get
import io.ktor.routing.routing
import io.ktor.serialization.json
import org.koin.core.context.startKoin
import org.koin.ktor.ext.inject

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {
    // Testのときは、テストコード側から設定する
    if (!testing) {
        startKoin {
            modules(issueModule)
        }

        val database: IDatabase by inject()
        database.connect()
    }

    install(ContentNegotiation) {
        json(
            contentType = ContentType.Application.Json
        )
    }

    install(CORS) {
        methods += setOf(
            HttpMethod.Get,
            HttpMethod.Post,
            HttpMethod.Put,
            HttpMethod.Delete,
            HttpMethod.Options
        )
        header(HttpHeaders.ContentType)
        header(HttpHeaders.AccessControlAllowOrigin)
        host("localhost:8081")
        allowCredentials = true
    }

    routing {
        root()
        issueRoute()
        commentRoute()
    }
}

fun Routing.root() {
    get("/health_check") {
        call.respondText("OK", ContentType.Text.Html)
    }
}
