package com.example.redbooklite.data.remote

import com.example.redbooklite.model.Note

object NoteDtoMapper {
    fun map(dto: NoteDto, fallbackId: Long): Note {
        return Note(
            id = dto.id ?: fallbackId,
            title = dto.title.orEmpty(),
            content = dto.content.orEmpty(),
            coverPath = dto.coverUrl.orEmpty(),
            coverAspectRatio = dto.coverAspectRatio ?: 0.85f,
            authorName = dto.author?.nickname.orEmpty(),
            likeCount = dto.likeCount ?: 0,
            isMine = false,
            createdAt = dto.createdAt ?: System.currentTimeMillis(),
            category = dto.category.orEmpty(),
            authorAvatarUrl = dto.author?.avatarUrl.orEmpty(),
            authorId = dto.author?.id ?: dto.author?.authorId ?: dto.author?.nickname.orEmpty()
        )
    }
}
