package com.cobeisfresh.azil.common.utils

import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import android.support.v4.content.FileProvider
import com.cobeisfresh.azil.App
import com.cobeisfresh.azil.BuildConfig
import java.io.*

/**
 * Created by Zerina Salitrezic on 24/08/2017.
 */

private const val IMAGE_NAME_PREFIX = "IMG_"
private const val JPG_TYPE = ".jpeg"
private const val PROVIDER = ".provider"

fun getMediaFileUri(): Uri? {
    val mediaStorageDir: File
    if (isExternalStorageAvailable()) {
        mediaStorageDir = App.get().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    } else {
        mediaStorageDir = App.get().cacheDir
        if (!mediaStorageDir.exists()) {
            mediaStorageDir.mkdirs()
        }
    }

    val timeStamp = getCurrentTimeStringFormat()
    val fileName = "$IMAGE_NAME_PREFIX $timeStamp"
    val fileType = JPG_TYPE
    var uri: Uri? = null
    try {
        val mediaFile = File.createTempFile(fileName, fileType, mediaStorageDir)
        onNougatAndAbove(
                onValid = { uri = FileProvider.getUriForFile(App.get(), BuildConfig.APPLICATION_ID + PROVIDER, mediaFile) },
                onInvalid = { uri = Uri.fromFile(mediaFile) })
    } catch (e: IOException) {
        e.printStackTrace()
    }

    return uri
}

private fun isExternalStorageAvailable(): Boolean {
    val state = Environment.getExternalStorageState()
    return Environment.MEDIA_MOUNTED == state
}

fun getResizedImageFile(imagePath: String): File {
    val file = File(imagePath)
    val bitmap: Bitmap? = getBitmapFromPath(imagePath)
    val outputStream: OutputStream? = try {
        BufferedOutputStream(FileOutputStream(file))
    } catch (e: FileNotFoundException) {
        e.printStackTrace()
        null
    }

    if (bitmap != null && outputStream != null) {
        outputStream.use { bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it) }
    }

    return file
}