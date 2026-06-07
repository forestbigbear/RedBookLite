package com.example.redbooklite.util

import com.example.redbooklite.R

object AvatarHelper {

    private val avatarResIds = intArrayOf(
        R.drawable.seed_avatar_1,
        R.drawable.seed_avatar_2,
        R.drawable.seed_avatar_3,
        R.drawable.seed_avatar_4,
        R.drawable.seed_avatar_5,
        R.drawable.seed_avatar_6
    )

    /** 按笔记 id 稳定映射到 6 张头像之一 */
    fun getAvatarResId(noteId: Long): Int {
        val index = (noteId % avatarResIds.size).toInt()
        return avatarResIds[index]
    }
}