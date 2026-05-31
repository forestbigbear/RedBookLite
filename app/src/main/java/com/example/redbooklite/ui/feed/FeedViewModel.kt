package com.example.redbooklite.ui.feed

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.redbooklite.data.repository.NoteRepository
import com.example.redbooklite.model.Note

class FeedViewModel(repository: NoteRepository) : ViewModel() {
    val notes: LiveData<List<Note>> = repository.getFeedNotes().asLiveData()
}