package com.simple.discussion.di

import com.simple.discussion.database.IDatabase
import com.simple.discussion.exposed.database.h2.H2Database
import com.simple.discussion.exposed.respository.ExposedCommentRepository
import com.simple.discussion.exposed.respository.ExposedIssueRepository
import com.simple.discussion.repository.ICommentRepository
import com.simple.discussion.repository.IIssueRepository
import org.koin.dsl.bind
import org.koin.dsl.module

val issueModule = module {
    single { ExposedIssueRepository() } bind IIssueRepository::class
    single { ExposedCommentRepository() } bind ICommentRepository::class
    single { H2Database() } bind IDatabase::class
}

