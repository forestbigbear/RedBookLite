package com.example.redbooklite.data.remote

import com.google.gson.annotations.SerializedName

data class NoteDto(
    val id: Long?,
    val title: String?,
    val content: String?,
    @SerializedName("cover_url")
    val coverUrl: String?,
    @SerializedName("cover_aspect_ratio")
    val coverAspectRatio: Float?,
    val category: String?,
    val author: AuthorDto?,
    @SerializedName("like_count")
    val likeCount: Int?,
    @SerializedName("created_at")
    val createdAt: Long?
)

data class AuthorDto(
    val id: String?,
    @SerializedName("author_id")
    val authorId: String?,
    val nickname: String?,
    @SerializedName("avatar_url")
    val avatarUrl: String?
)
