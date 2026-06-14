package com.example.redbooklite.ui.author

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import com.example.redbooklite.data.repository.NoteRepository
import com.example.redbooklite.model.Note

class AuthorProfileViewModel(
    repository: NoteRepository,
    authorId: String,
    authorName: String
) : ViewModel() {

    private val notesSource: LiveData<List<Note>> =
        repository.getNotesByAuthor(authorId, authorName).asLiveData()

    private val _uiState = MediatorLiveData<AuthorUiState>()
    val uiState: LiveData<AuthorUiState> = _uiState

    init {
        _uiState.value = AuthorUiState.Loading
        _uiState.addSource(notesSource) { notes ->
            _uiState.value = if (notes.isEmpty()) {
                AuthorUiState.Empty
            } else {
                AuthorUiState.Success(notes)
            }
        }
    }
}

sealed class AuthorUiState {
    data object Loading : AuthorUiState()
    data object Empty : AuthorUiState()
    data class Success(val notes: List<Note>) : AuthorUiState()
    data class Error(val message: String) : AuthorUiState()
}

class AuthorProfileViewModelFactory(
    private val repository: NoteRepository,
    private val authorId: String,
    private val authorName: String
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuthorProfileViewModel::class.java)) {
            return AuthorProfileViewModel(repository, authorId, authorName) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
