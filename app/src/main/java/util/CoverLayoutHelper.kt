package com.example.redbooklite.util

import android.widget.ImageView

object CoverLayoutHelper {
    fun applyStaggeredCoverHeight(iv: ImageView, aspectRatio: Float) {
        if (aspectRatio > 0) {
            val width = iv.layoutParams.width
            iv.layoutParams.height = (width / aspectRatio).toInt()
        }
    }
}