package com.simple.discussion.model

import kotlinx.serialization.Serializable

@Serializable
data class Issue(
    val id: Int = -1,
    val title: String,
    val description: String
)