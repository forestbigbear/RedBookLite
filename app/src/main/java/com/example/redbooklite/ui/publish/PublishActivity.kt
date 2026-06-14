package com.example.redbooklite.ui.publish

import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.lifecycle.ViewModelProvider
import com.example.redbooklite.R
import com.example.redbooklite.RedBookApp
import com.example.redbooklite.util.FileHelper
import com.example.redbooklite.util.ImageLoader
import com.example.redbooklite.util.ImageSizeHelper

class PublishActivity : AppCompatActivity() {

    private var coverPath: String? = null
    private var coverAspectRatio: Float = 0.85f
    private lateinit var viewModel: PublishViewModel

    private lateinit var ivCoverPreview: ImageView
    private lateinit var etTitle: EditText
    private lateinit var etContent: EditText

    private val pickImageLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri == null) {
            return@registerForActivityResult
        }
        val path = FileHelper.copyImageToAppDir(this, uri)
        if (path == null) {
            Toast.makeText(this, R.string.publish_need_cover, Toast.LENGTH_SHORT).show()
            return@registerForActivityResult
        }
        coverPath = path
        coverAspectRatio = ImageSizeHelper.readAspectRatio(path)
        ImageLoader.loadCover(ivCoverPreview, path)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, true)
        setContentView(R.layout.activity_publish)

        ivCoverPreview = findViewById(R.id.ivCoverPreview)
        etTitle = findViewById(R.id.etTitle)
        etContent = findViewById(R.id.etContent)
        val btnSelectCover: Button = findViewById(R.id.btnSelectCover)
        val btnPublish: Button = findViewById(R.id.btnPublish)
        val repository = (application as RedBookApp).repository
        viewModel = ViewModelProvider(
            this,
            PublishViewModelFactory(repository)
        )[PublishViewModel::class.java]

        findViewById<View>(R.id.btnBack).setOnClickListener { finish() }

        btnSelectCover.setOnClickListener {
            pickImageLauncher.launch("image/*")
        }

        btnPublish.setOnClickListener {
            publishNote()
        }

        viewModel.publishResult.observe(this) { result ->
            when (result) {
                is PublishResult.Error -> {
                    Toast.makeText(this, result.messageResId, Toast.LENGTH_SHORT).show()
                }
                PublishResult.Success -> {
                    Toast.makeText(this, R.string.publish_success, Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
        }
    }

    private fun publishNote() {
        viewModel.publishNote(
            title = etTitle.text.toString(),
            content = etContent.text.toString(),
            coverPath = coverPath,
            coverAspectRatio = coverAspectRatio
        )
    }
}
