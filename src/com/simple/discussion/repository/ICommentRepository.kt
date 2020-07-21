package com.simple.discussion.repository

import com.simple.discussion.model.Comment

interface ICommentRepository {
    fun add(issueId: Int, comment: Comment): Comment
}