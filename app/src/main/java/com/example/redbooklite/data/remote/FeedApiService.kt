package com.example.redbooklite.data.remote

import retrofit2.http.GET

interface FeedApiService {
    @GET("redbooklite/feed.json")
    suspend fun getFeedNotes(): List<NoteDto>
}
