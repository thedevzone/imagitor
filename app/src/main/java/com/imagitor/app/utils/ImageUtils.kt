package com.imagitor.app.utils

import android.content.Context
import android.content.Intent
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore

object ImageUtils {

    fun rotate(rotation: Float): Float {
        return when (rotation) {
            0f -> 90f
            90f -> 180f
            180f -> 270f
            270f -> 0f
            else -> 0f
        }
    }

    fun rotateImage(context: Context, imageUri: Uri, angle: Float): Uri {
        val bitmap = if (Build.VERSION.SDK_INT < 28) {
            MediaStore.Images.Media.getBitmap(context.contentResolver, imageUri)
        } else {
            val source = ImageDecoder.createSource(context.contentResolver, imageUri)
            ImageDecoder.decodeBitmap(source)
        }
        val rotatedBitmap = bitmap.rotatedBy(angle)
        return rotatedBitmap.toUri(context)
    }

    fun getCropIntent(image: Uri): Intent {
        return Intent("com.android.camera.action.CROP").apply {
            setDataAndType(image, "image/*")
            putExtra("crop", "true")
            putExtra("aspectX", 1)
            putExtra("aspectY", 1)
            putExtra("outputX", 256)
            putExtra("outputY", 256)
            putExtra("return-data", true)
        }
    }
}