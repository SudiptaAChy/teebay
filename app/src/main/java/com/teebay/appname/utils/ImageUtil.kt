package com.teebay.appname.utils

import android.graphics.Bitmap
import android.net.Uri
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.internal.platform.PlatformRegistry.applicationContext
import java.io.File
import java.io.FileOutputStream

object ImageUtil {
    fun bitmapToMultipart(bitmap: Bitmap, partName: String = "product_image"): MultipartBody.Part {
        val context = applicationContext
        val file = File.createTempFile("upload_", ".jpg", context?.cacheDir)
        val out = FileOutputStream(file)

        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)
        out.flush()
        out.close()

        val mimeType = "image/jpeg"
        val requestFile = file.asRequestBody(mimeType.toMediaTypeOrNull())
        return MultipartBody.Part.createFormData(partName, file.name, requestFile)
    }

    fun uriToMultiPart(uri: Uri, partName: String = "product_image"): MultipartBody.Part? {
        val context = applicationContext
        val contentResolver = context?.contentResolver
        val mimeType = contentResolver?.getType(uri) ?: "image/*"

        val inputStream = contentResolver?.openInputStream(uri)
        val tempFile = File.createTempFile("upload_", ".jpg", context?.cacheDir)
        inputStream?.use { input ->
            tempFile.outputStream().use { output ->
                input.copyTo(output)
            }
        }

        val requestBody = tempFile.asRequestBody(mimeType.toMediaTypeOrNull())
        return MultipartBody.Part.createFormData(partName, tempFile.name, requestBody)
    }
}

