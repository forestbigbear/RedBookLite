package com.example.redbooklite.ui.detail

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.lifecycle.ViewModelProvider
import com.example.redbooklite.R
import com.example.redbooklite.RedBookApp
import com.example.redbooklite.util.ImageLoader

class NoteDetailActivity : AppCompatActivity() {

    private lateinit var viewModel: DetailViewModel
    private lateinit var ivCover: ImageView
    private lateinit var tvTitle: TextView
    private lateinit var tvAuthor: TextView
    private lateinit var tvContent: TextView
    private lateinit var tvLikeCount: TextView
    private lateinit var btnLike: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, true)
        setContentView(R.layout.activity_note_detail)

        val noteId = intent.getLongExtra(EXTRA_NOTE_ID, -1L)
        if (noteId < 0) {
            finish()
            return
        }

        ivCover = findViewById(R.id.ivCover)
        tvTitle = findViewById(R.id.tvTitle)
        tvAuthor = findViewById(R.id.tvAuthor)
        tvContent = findViewById(R.id.tvContent)
        tvLikeCount = findViewById(R.id.tvLikeCount)
        btnLike = findViewById(R.id.btnLike)

        findViewById<View>(R.id.btnBack).setOnClickListener { finish() }

        val app = application as RedBookApp
        viewModel = ViewModelProvider(
            this,
            DetailViewModelFactory(app.repository, noteId)
        )[DetailViewModel::class.java]

        viewModel.note.observe(this) { note ->
            if (note == null) {
                finish()
                return@observe
            }
            ImageLoader.loadCover(ivCover, note.coverPath)
            tvTitle.text = note.title
            tvAuthor.text = note.authorName
            tvContent.text = note.content
            tvLikeCount.text = getString(R.string.like_format, note.likeCount)
        }

        btnLike.setOnClickListener {
            viewModel.likeNote()
        }

        viewModel.loadNote()
    }

    companion object {
        const val EXTRA_NOTE_ID = "extra_note_id"
    }
}