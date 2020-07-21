package com.simple.discussion.model

import kotlinx.serialization.Serializable

@Serializable
data class Comment(
    val id: Int = -1,
    val description: String
)