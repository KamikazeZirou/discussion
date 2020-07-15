package com.simple.discussion.repository

import com.simple.discussion.model.Issue

interface IIssueRepository {
    fun add(issue: Issue): Issue
    fun get(): List<Issue>
    fun update(issue: Issue): Issue
    fun delete(issueId: Int)
}