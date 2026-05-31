package com.example.redbooklite.util

import android.widget.ImageView

object ImageLoader {
    fun loadCover(iv: ImageView, path: String) {
        // 这里使用默认占位，如果你集成 Glide 或 Picasso 可以直接加载 path
        iv.setImageResource(android.R.color.darker_gray)
    }
}