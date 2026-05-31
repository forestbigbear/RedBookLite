package com.example.redbooklite

import android.app.Application
import com.example.redbooklite.data.repository.NoteRepository

class RedBookApp : Application() {
    val repository: NoteRepository by lazy { NoteRepository(this) }
}