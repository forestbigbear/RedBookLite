package com.example.redbooklite.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import com.example.redbooklite.data.repository.NoteRepository
import com.example.redbooklite.model.Note

class ProfileViewModel(repository: NoteRepository) : ViewModel() {

    val notes: LiveData<List<Note>> = repository.getMyNotes().asLiveData()
}

class ProfileViewModelFactory(private val repository: NoteRepository) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
            return ProfileViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}