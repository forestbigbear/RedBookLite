package com.example.redbooklite.util

import android.content.Context
import android.widget.ImageView
import kotlin.math.max

object CoverLayoutHelper {

    /** 根据列宽和宽高比（宽/高）设置封面高度，形成瀑布流错落效果 */
    fun applyStaggeredCoverHeight(imageView: ImageView, aspectRatio: Float) {
        val safeRatio = max(0.55f, aspectRatio)
        val columnWidth = getColumnWidthPx(imageView.context)
        val coverHeight = (columnWidth / safeRatio).toInt()

        val params = imageView.layoutParams
        params.height = coverHeight
        imageView.layoutParams = params
    }

    private fun getColumnWidthPx(context: Context): Int {
        val dm = context.resources.displayMetrics
        val density = dm.density
        // RecyclerView 左右 padding + 卡片左右 margin，再除以 2 列
        // RecyclerView padding + 卡片左右 margin（封面已无卡片内边距）
        val spacing = ((4f + 4f + 4f + 4f) * density).toInt()
        return (dm.widthPixels - spacing) / 2
    }
}