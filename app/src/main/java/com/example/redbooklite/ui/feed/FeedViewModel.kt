package com.example.redbooklite.ui.feed

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.redbooklite.data.repository.NoteRepository
import com.example.redbooklite.model.Note
import kotlinx.coroutines.launch

class FeedViewModel(
    private val repository: NoteRepository
) : ViewModel() {

    private val notesSource: LiveData<List<Note>> = repository.getFeedNotes().asLiveData()
    private val isRefreshing = MutableLiveData(false)
    private val errorMessage = MutableLiveData<String?>(null)
    private val selectedCategory = MutableLiveData(CATEGORY_ALL)
    private val searchKeyword = MutableLiveData("")

    private val _uiState = MediatorLiveData<FeedUiState>()
    val uiState: LiveData<FeedUiState> = _uiState

    init {
        _uiState.addSource(notesSource) { updateState() }
        _uiState.addSource(isRefreshing) { updateState() }
        _uiState.addSource(errorMessage) { updateState() }
        _uiState.addSource(selectedCategory) { updateState() }
        _uiState.addSource(searchKeyword) { updateState() }
        updateState()
        refreshFeed()
    }

    fun refreshFeed() {
        if (isRefreshing.value == true) return

        isRefreshing.value = true
        errorMessage.value = null
        viewModelScope.launch {
            val result = repository.refreshFeed()
            if (result.isFailure) {
                errorMessage.value = "网络请求失败，请检查网络后重试"
            }
            isRefreshing.value = false
        }
    }

    fun selectCategory(category: String) {
        selectedCategory.value = category
    }

    fun updateSearchKeyword(keyword: String) {
        searchKeyword.value = keyword.trim()
    }

    private fun updateState() {
        val allNotes = notesSource.value.orEmpty()
        val filteredNotes = filterNotes(allNotes)
        val refreshing = isRefreshing.value == true
        val error = if (allNotes.isEmpty()) errorMessage.value else null
        val isFirstLoading = refreshing && allNotes.isEmpty()

        _uiState.value = FeedUiState(
            notes = filteredNotes,
            isRefreshing = refreshing,
            showLoading = isFirstLoading,
            showEmpty = !refreshing && error == null && filteredNotes.isEmpty(),
            errorMessage = error,
            selectedCategory = selectedCategory.value ?: CATEGORY_ALL,
            searchKeyword = searchKeyword.value.orEmpty()
        )
    }

    private fun filterNotes(notes: List<Note>): List<Note> {
        val category = selectedCategory.value ?: CATEGORY_ALL
        val keyword = searchKeyword.value.orEmpty()

        return notes.filter { note ->
            val categoryMatched = category == CATEGORY_ALL || note.category == category
            val keywordMatched = keyword.isBlank() || note.title.contains(keyword, ignoreCase = true)
            categoryMatched && keywordMatched
        }
    }

    companion object {
        const val CATEGORY_ALL = "all"
        const val CATEGORY_TRAVEL = "travel"
        const val CATEGORY_FOOD = "food"
        const val CATEGORY_FASHION = "fashion"
    }
}

data class FeedUiState(
    val notes: List<Note> = emptyList(),
    val isRefreshing: Boolean = false,
    val showLoading: Boolean = false,
    val showEmpty: Boolean = false,
    val errorMessage: String? = null,
    val selectedCategory: String = FeedViewModel.CATEGORY_ALL,
    val searchKeyword: String = ""
)

class FeedViewModelFactory(private val repository: NoteRepository) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FeedViewModel::class.java)) {
            return FeedViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
