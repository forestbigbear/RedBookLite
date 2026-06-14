package com.example.redbooklite.data.repository

import android.content.Context
import com.example.redbooklite.data.local.LikePreferenceStore
import com.example.redbooklite.data.local.NoteLocalStore
import com.example.redbooklite.data.local.SeedData
import com.example.redbooklite.data.remote.FeedApiService
import com.example.redbooklite.data.remote.NetworkModule
import com.example.redbooklite.data.remote.NoteDtoMapper
import com.example.redbooklite.model.Note
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class NoteRepository(
    context: Context,
    private val feedApiService: FeedApiService = NetworkModule.feedApiService
) {

    private val localStore = NoteLocalStore()
    private val appContext = context.applicationContext
    private val likePreferenceStore = LikePreferenceStore(appContext)

    suspend fun initDataIfNeeded() {
        if (localStore.isEmpty()) {
            localStore.saveAll(applyPersistedLikes(SeedData.buildNotes(appContext)))
        }
    }

    fun getFeedNotes(): Flow<List<Note>> {
        return localStore.notesFlow.map { list ->
            list.sortedByDescending { it.createdAt }
        }
    }

    suspend fun refreshFeed(): Result<Unit> {
        return runCatching {
            val remoteNotes = feedApiService.getFeedNotes()
                .mapIndexed { index, dto ->
                    NoteDtoMapper.map(dto, fallbackId = 10_000L + index)
                }
                .filter { it.title.isNotBlank() && it.coverPath.isNotBlank() }

            if (remoteNotes.isNotEmpty()) {
                val localPublishedNotes = localStore.notesFlow.value.filter { it.isMine }
                localStore.saveAll(applyPersistedLikes(localPublishedNotes + remoteNotes))
            }
        }
    }

    suspend fun getNoteById(noteId: Long): Note? {
        return localStore.getById(noteId)?.let { applyPersistedLike(it) }
    }

    suspend fun likeNote(noteId: Long) {
        val note = localStore.getById(noteId) ?: return
        val newLikeCount = note.likeCount + 1
        likePreferenceStore.saveLikeCount(noteId, newLikeCount)

        localStore.update(
            note.copy(
                likeCount = newLikeCount
            )
        )
    }

    fun getMyNotes(): Flow<List<Note>> {
        return localStore.notesFlow.map { list ->
            list.filter { it.isMine }
        }
    }

    fun getNotesByAuthor(authorId: String, authorName: String): Flow<List<Note>> {
        return localStore.notesFlow.map { list ->
            list.filter { note ->
                if (authorId.isNotBlank()) {
                    note.authorId == authorId
                } else {
                    note.authorName == authorName
                }
            }
                .sortedByDescending { it.createdAt }
        }
    }

    suspend fun publishNote(
        title: String,
        content: String,
        coverPath: String,
        coverAspectRatio: Float
    ) {

        val note = Note(
            id = localStore.generateNextId(),

            title = title,

            content = content,

            coverPath = coverPath,

            coverAspectRatio = coverAspectRatio,

            authorName = "我",

            likeCount = 0,

            isMine = true,

            createdAt = System.currentTimeMillis(),

            category = "travel",

            authorId = "me"
        )

        localStore.add(note)
    }

    private fun applyPersistedLikes(notes: List<Note>): List<Note> {
        return notes.map { applyPersistedLike(it) }
    }

    private fun applyPersistedLike(note: Note): Note {
        val persistedLikeCount = likePreferenceStore.getLikeCount(note.id) ?: return note
        return note.copy(likeCount = persistedLikeCount)
    }
}
