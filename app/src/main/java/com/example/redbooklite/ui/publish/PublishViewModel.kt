package com.example.redbooklite.ui.publish

import androidx.annotation.StringRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.redbooklite.R
import com.example.redbooklite.data.repository.NoteRepository
import kotlinx.coroutines.launch

class PublishViewModel(
    private val repository: NoteRepository
) : ViewModel() {

    private val _publishResult = MutableLiveData<PublishResult>()
    val publishResult: LiveData<PublishResult> = _publishResult

    fun publishNote(
        title: String,
        content: String,
        coverPath: String?,
        coverAspectRatio: Float
    ) {
        val cleanTitle = title.trim()
        val cleanContent = content.trim()

        when {
            coverPath.isNullOrBlank() -> {
                _publishResult.value = PublishResult.Error(R.string.publish_need_cover)
            }
            cleanTitle.isEmpty() -> {
                _publishResult.value = PublishResult.Error(R.string.publish_need_title)
            }
            cleanContent.isEmpty() -> {
                _publishResult.value = PublishResult.Error(R.string.publish_need_content)
            }
            else -> {
                viewModelScope.launch {
                    repository.publishNote(
                        title = cleanTitle,
                        content = cleanContent,
                        coverPath = coverPath,
                        coverAspectRatio = coverAspectRatio
                    )
                    _publishResult.value = PublishResult.Success
                }
            }
        }
    }
}

sealed class PublishResult {
    data object Success : PublishResult()
    data class Error(@StringRes val messageResId: Int) : PublishResult()
}

class PublishViewModelFactory(
    private val repository: NoteRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PublishViewModel::class.java)) {
            return PublishViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
