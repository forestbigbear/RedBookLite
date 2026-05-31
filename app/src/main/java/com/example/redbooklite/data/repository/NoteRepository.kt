package com.example.redbooklite.data.repository

import android.content.Context
import com.example.redbooklite.model.Note
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class NoteRepository(context: Context) {

    // 模拟获取笔记列表
    fun getFeedNotes(): Flow<List<Note>> = flow {
        emit(
            listOf(
                Note(
                    id = 1,
                    title = "今日穿搭分享",
                    content = "夏日清爽风",
                    coverPath = "",
                    coverAspectRatio = 0.75f,
                    authorName = "Alice",
                    likeCount = 102,
                    isMine = false,
                    createdAt = System.currentTimeMillis()
                ),
                Note(
                    id = 2,
                    title = "咖啡店探店",
                    content = "发现一家超棒咖啡馆",
                    coverPath = "",
                    coverAspectRatio = 1.25f,
                    authorName = "Bob",
                    likeCount = 88,
                    isMine = false,
                    createdAt = System.currentTimeMillis()
                )
            )
        )
    }
}