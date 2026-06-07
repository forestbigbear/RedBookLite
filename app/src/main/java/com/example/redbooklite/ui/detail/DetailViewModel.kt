package com.example.redbooklite.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.redbooklite.data.repository.NoteRepository
import com.example.redbooklite.model.Note
import kotlinx.coroutines.launch

class DetailViewModel(
    private val repository: NoteRepository,
    private val noteId: Long
) : ViewModel() {

    private val _note = MutableLiveData<Note?>()
    val note: LiveData<Note?> = _note

    fun loadNote() {
        viewModelScope.launch {
            _note.value = repository.getNoteById(noteId)
        }
    }

    fun likeNote() {
        viewModelScope.launch {
            repository.likeNote(noteId)
            _note.value = repository.getNoteById(noteId)
        }
    }
}

class DetailViewModelFactory(
    private val repository: NoteRepository,
    private val noteId: Long
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetailViewModel::class.java)) {
            return DetailViewModel(repository, noteId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}