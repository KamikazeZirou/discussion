package com.simple.discussion.service

import com.simple.discussion.model.Comment
import com.simple.discussion.repository.ICommentRepository
import io.ktor.application.call
import io.ktor.http.HttpHeaders.Location
import io.ktor.http.HttpStatusCode
import io.ktor.request.receive
import io.ktor.response.header
import io.ktor.response.respond
import io.ktor.routing.Routing
import io.ktor.routing.post
import io.ktor.routing.route
import org.koin.core.KoinComponent
import org.koin.core.inject

object CommentApplicationService : KoinComponent {
    private val repository: ICommentRepository by inject()

    fun add(issueId: Int, comment: Comment): Comment {
        return repository.add(issueId, comment)
    }
}

fun Routing.commentRoute() {
    route("/issues/{id}/comments") {
        post {
            val issueId = call.parameters["id"]?.toIntOrNull()
            val postedComment = call.receive<Comment>()

            // TODO issueId取れない場合はエラーを返す
            val savedComment = CommentApplicationService.add(issueId!!, postedComment)

            call.response.header(Location, "/issues/${savedComment.id}")
            call.respond(HttpStatusCode.Created, savedComment)
        }
    }
}
