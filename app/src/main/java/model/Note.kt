package com.example.redbooklite.model

data class Note(
    val id: Long,
    val title: String,
    val content: String,
    val coverPath: String,
    val coverAspectRatio: Float,
    val authorName: String,
    val likeCount: Int,
    val isMine: Boolean,
    val createdAt: Long,
    val category: String = "",
    val authorAvatarUrl: String = "",
    val authorId: String = ""
)
