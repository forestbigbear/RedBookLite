package com.example.redbooklite.util

import android.content.Context
import android.net.Uri
import java.io.File
import java.io.FileOutputStream

object FileHelper {

    fun copyImageToAppDir(context: Context, uri: Uri): String? {
        return try {
            val dir = File(context.filesDir, "covers")
            if (!dir.exists()) {
                dir.mkdirs()
            }
            val file = File(dir, "cover_${System.currentTimeMillis()}.jpg")
            context.contentResolver.openInputStream(uri)?.use { input ->
                FileOutputStream(file).use { output ->
                    input.copyTo(output)
                }
            }
            file.absolutePath
        } catch (e: Exception) {
            null
        }
    }
}