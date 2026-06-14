package com.example.redbooklite.util

import android.widget.ImageView
import coil.load
import java.io.File

object ImageLoader {
    fun loadCover(imageView: ImageView, coverPath: String) {
        if (coverPath.startsWith("drawable://")) {
            val resId = coverPath.removePrefix("drawable://").toInt()
            imageView.setImageResource(resId)
        } else if (coverPath.startsWith("http://") || coverPath.startsWith("https://")) {
            imageView.load(coverPath) {
                crossfade(true)
            }
        } else {
            imageView.load(File(coverPath)) {
                crossfade(true)
            }
        }
    }
}
