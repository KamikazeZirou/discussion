package com.simple.discussion.di

import com.simple.discussion.exposed.dao.ExposedIssueRepository
import com.simple.discussion.repository.IIssueRepository
import org.koin.dsl.bind
import org.koin.dsl.module

val myModule = module {
    single { ExposedIssueRepository() } bind IIssueRepository::class
}

