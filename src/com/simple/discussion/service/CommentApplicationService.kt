package com.simple.discussion.service

import com.simple.discussion.model.Comment
import com.simple.discussion.repository.ICommentRepository
import io.ktor.application.call
import io.ktor.http.HttpHeaders.Location
import io.ktor.http.HttpStatusCode
import io.ktor.request.receive
import io.ktor.response.header
import io.ktor.response.respond
import io.ktor.routing.*
import org.koin.core.KoinComponent
import org.koin.core.inject

object CommentApplicationService : KoinComponent {
    private val repository: ICommentRepository by inject()

    fun add(issueId: Int, comment: Comment): Comment {
        return repository.add(issueId, comment)
    }

    fun update(comment: Comment): Comment {
        return repository.update(comment)
    }

    fun get(issueId: Int): List<Comment> {
        return repository.get(issueId)
    }

    fun delete(commentId: Int) {
        return repository.delete(commentId)
    }
}

fun Routing.commentRoute() {
    route("/issues/{issue-id}/comments") {
        post {
            val issueId = call.parameters["issue-id"]?.toIntOrNull()
            val postedComment = call.receive<Comment>()

            // TODO Parameterチェック

            val savedComment = CommentApplicationService.add(issueId!!, postedComment)

            call.response.header(Location, "/issues/$issueId/comments/${savedComment.id}")
            call.respond(HttpStatusCode.Created, savedComment)
        }

        get {
            val issueId = call.parameters["issue-id"]?.toIntOrNull()
            val comments = CommentApplicationService.get(issueId!!)
            call.respond(HttpStatusCode.OK, comments)
        }

        put("{id}") {
            val issueId = call.parameters["issue-id"]?.toIntOrNull()
            val commentId = call.parameters["id"]?.toIntOrNull()
            val putComment = call.receive<Comment>().copy(
                id = commentId!!
            )

            // TODO Parameterチェック

            val savedComment = CommentApplicationService.update(putComment)
            call.response.header(Location, "/issues/$issueId/comments/${savedComment.id}")
            call.respond(HttpStatusCode.NoContent)
        }

        delete("{id}") {
            val commentId = call.parameters["id"]?.toIntOrNull()
            // TODO Parameterチェック
            CommentApplicationService.delete(commentId!!)
            call.respond(HttpStatusCode.NoContent)
        }
    }
}
