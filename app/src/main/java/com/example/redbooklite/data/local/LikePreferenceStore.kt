package com.example.redbooklite.data.local

import android.content.Context

class LikePreferenceStore(context: Context) {

    private val preferences = context.applicationContext.getSharedPreferences(
        PREF_NAME,
        Context.MODE_PRIVATE
    )

    fun getLikeCount(noteId: Long): Int? {
        val key = keyFor(noteId)
        return if (preferences.contains(key)) {
            preferences.getInt(key, 0)
        } else {
            null
        }
    }

    fun saveLikeCount(noteId: Long, likeCount: Int) {
        preferences.edit()
            .putInt(keyFor(noteId), likeCount)
            .apply()
    }

    private fun keyFor(noteId: Long): String = "note_like_$noteId"

    private companion object {
        const val PREF_NAME = "like_preferences"
    }
}
