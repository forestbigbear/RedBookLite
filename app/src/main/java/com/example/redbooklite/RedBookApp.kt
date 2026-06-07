package com.example.redbooklite

import android.app.Application
import com.example.redbooklite.data.repository.NoteRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class RedBookApp : Application(){

    val repository: NoteRepository by lazy { NoteRepository(this) }

    private val appScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onCreate() {
        super.onCreate()
        appScope.launch {
            repository.initDataIfNeeded()
        }
    }
}