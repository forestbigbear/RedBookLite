package com.example.redbooklite.ui.author

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.redbooklite.R
import com.example.redbooklite.RedBookApp
import com.example.redbooklite.model.Note
import com.example.redbooklite.ui.detail.NoteDetailActivity
import com.example.redbooklite.ui.feed.NoteAdapter
import com.example.redbooklite.util.ImageLoader

class AuthorProfileActivity : AppCompatActivity(), NoteAdapter.OnNoteClickListener {

    private lateinit var viewModel: AuthorProfileViewModel
    private lateinit var adapter: NoteAdapter
    private lateinit var rvAuthorNotes: RecyclerView
    private lateinit var ivAuthorAvatar: ImageView
    private lateinit var tvAuthorName: TextView
    private lateinit var tvLoading: TextView
    private lateinit var tvEmpty: TextView
    private lateinit var tvError: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, true)
        setContentView(R.layout.activity_author_profile)

        val authorId = intent.getStringExtra(EXTRA_AUTHOR_ID).orEmpty()
        val authorName = intent.getStringExtra(EXTRA_AUTHOR_NAME).orEmpty()
        val authorAvatarUrl = intent.getStringExtra(EXTRA_AUTHOR_AVATAR_URL).orEmpty()
        if (authorId.isBlank() && authorName.isBlank()) {
            finish()
            return
        }

        findViewById<View>(R.id.btnBack).setOnClickListener { finish() }
        ivAuthorAvatar = findViewById(R.id.ivAuthorAvatar)
        tvAuthorName = findViewById(R.id.tvAuthorName)
        rvAuthorNotes = findViewById(R.id.rvAuthorNotes)
        tvLoading = findViewById(R.id.tvLoading)
        tvEmpty = findViewById(R.id.tvEmpty)
        tvError = findViewById(R.id.tvError)

        tvAuthorName.text = authorName
        if (authorAvatarUrl.isNotBlank()) {
            ImageLoader.loadCover(ivAuthorAvatar, authorAvatarUrl)
        }

        val app = application as RedBookApp
        viewModel = ViewModelProvider(
            this,
            AuthorProfileViewModelFactory(app.repository, authorId, authorName)
        )[AuthorProfileViewModel::class.java]

        adapter = NoteAdapter(this)
        val layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        layoutManager.gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_NONE
        rvAuthorNotes.layoutManager = layoutManager
        rvAuthorNotes.adapter = adapter
        rvAuthorNotes.setHasFixedSize(false)

        viewModel.uiState.observe(this) { state ->
            renderState(state)
        }
    }

    private fun renderState(state: AuthorUiState) {
        tvLoading.visibility = if (state is AuthorUiState.Loading) View.VISIBLE else View.GONE
        tvEmpty.visibility = if (state is AuthorUiState.Empty) View.VISIBLE else View.GONE
        tvError.visibility = if (state is AuthorUiState.Error) View.VISIBLE else View.GONE
        rvAuthorNotes.visibility = if (state is AuthorUiState.Success) View.VISIBLE else View.GONE

        if (state is AuthorUiState.Success) {
            adapter.submitList(state.notes)
        } else {
            adapter.submitList(emptyList())
        }

        if (state is AuthorUiState.Error) {
            tvError.text = state.message
        }
    }

    override fun onNoteClick(note: Note) {
        val intent = Intent(this, NoteDetailActivity::class.java)
        intent.putExtra(NoteDetailActivity.EXTRA_NOTE_ID, note.id)
        startActivity(intent)
    }

    override fun onAuthorClick(note: Note) = Unit

    companion object {
        const val EXTRA_AUTHOR_ID = "extra_author_id"
        const val EXTRA_AUTHOR_NAME = "extra_author_name"
        const val EXTRA_AUTHOR_AVATAR_URL = "extra_author_avatar_url"
    }
}
