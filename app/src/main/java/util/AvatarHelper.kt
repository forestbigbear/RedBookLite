package com.example.redbooklite.util

import com.example.redbooklite.R

object AvatarHelper {
    fun getAvatarResId(id: Long): Int {
        // 根据 id 返回默认头像，这里统一返回一个占位
        return R.drawable.bg_avatar_circle
    }
}