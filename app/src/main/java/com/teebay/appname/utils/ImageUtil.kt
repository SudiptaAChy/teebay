package com.teebay.appname.utils

import android.graphics.Bitmap
import android.net.Uri
import android.webkit.MimeTypeMap
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.internal.platform.PlatformRegistry.applicationContext
import java.io.File
import java.io.FileOutputStream

object ImageUtil {
    fun uriToMultipart(uri: Uri, partName: String = "file"): MultipartBody.Part? {
        val context = applicationContext
        val contentResolver = context?.contentResolver
        val inputStream = contentResolver?.openInputStream(uri) ?: return null

        val mimeType = contentResolver.getType(uri) ?: return null
        val extension = MimeTypeMap.getSingleton()
            .getExtensionFromMimeType(mimeType) ?: "jpg"

        val file = File.createTempFile("upload_", ".$extension", context.cacheDir)
        file.outputStream().use { outputStream ->
            inputStream.copyTo(outputStream)
        }

        val requestBody = file.asRequestBody(mimeType.toMediaTypeOrNull())
        return MultipartBody.Part.createFormData(partName, file.name, requestBody)
    }

    fun bitmapToMultipart(bitmap: Bitmap, partName: String = "file", format: Bitmap.CompressFormat = Bitmap.CompressFormat.PNG): MultipartBody.Part {
        val (extension, mimeType) = when (format) {
            Bitmap.CompressFormat.JPEG -> "jpg" to "image/jpeg"
            Bitmap.CompressFormat.WEBP -> "webp" to "image/webp"
            else -> "png" to "image/png"
        }
        val context = applicationContext
        val file = File.createTempFile("upload_", ".$extension", context?.cacheDir)
        val outputStream = FileOutputStream(file)
        bitmap.compress(format, 100, outputStream)
        outputStream.flush()
        outputStream.close()

        val requestBody = file.asRequestBody(mimeType.toMediaTypeOrNull())
        return MultipartBody.Part.createFormData(partName, file.name, requestBody)
    }
}

