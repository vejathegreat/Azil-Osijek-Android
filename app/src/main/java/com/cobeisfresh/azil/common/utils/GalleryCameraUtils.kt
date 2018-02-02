package com.cobeisfresh.azil.common.utils

import android.content.ContentUris
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.os.Build
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.support.annotation.RequiresApi
import com.cobeisfresh.azil.App
import java.io.File
import java.util.regex.Pattern

/**
 * Created by Zerina Salitrezic on 24/08/2017.
 */

private const val DOWNLOADS_AUTHORITY = "com.android.providers.downloads.documents"
private const val DOWNLOADS_FOLDER = "content://downloads/public_downloads"
private const val IMAGE_HEIGHT = 500
private const val IMAGE_WIDTH = 500

fun getBitmapFromPath(path: String): Bitmap? = try {
    val file = File(path)
    val bitmap = BitmapFactory.decodeFile(file.absolutePath)
    getResizedBitmap(bitmap, Uri.parse(path), IMAGE_HEIGHT, IMAGE_WIDTH)
} catch (e: Throwable) {
    null
}

private fun getResizedBitmap(bitmap: Bitmap, uri: Uri, maxHeight: Int, maxWidth: Int): Bitmap {
    var currentBitmap = bitmap
    val width = bitmap.width
    val height = bitmap.height
    val ratioBitmap = width.toFloat() / height.toFloat()
    val ratioMax = maxWidth.toFloat() / maxHeight.toFloat()

    var newMaxWidth = maxWidth
    var newMaxHeight = maxHeight

    if (ratioMax > 1) {
        newMaxWidth = (maxHeight.toFloat() * ratioBitmap).toInt()
    } else {
        newMaxHeight = (maxWidth.toFloat() / ratioBitmap).toInt()
    }

    currentBitmap = Bitmap.createScaledBitmap(currentBitmap, newMaxWidth, newMaxHeight, true)
    return getRotatedBitmapToOriginal(currentBitmap, uri)
}

private fun getRotatedBitmapToOriginal(bitmap: Bitmap, imageUri: Uri): Bitmap {
    return try {
        val exifInterface = ExifInterface(imageUri.path)
        val rotationType = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED)

        when (rotationType) {
            ExifInterface.ORIENTATION_ROTATE_90 -> getRotatedBitmap(bitmap, 90)
            ExifInterface.ORIENTATION_ROTATE_180 -> getRotatedBitmap(bitmap, 180)
            ExifInterface.ORIENTATION_ROTATE_270 -> getRotatedBitmap(bitmap, 270)
            else -> bitmap
        }
    } catch (e: Throwable) {
        bitmap
    }
}

private fun getRotatedBitmap(bitmap: Bitmap, angle: Int): Bitmap {
    val matrix = Matrix()
    matrix.postRotate(angle.toFloat())
    return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
}

fun getRealImagePath(uri: Uri): String = with(uri) {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
        when {
            isDownloadsDocument(uri) -> getDownloadedImagePath(this)
            else -> getGalleryImagePath(this)
        }
    } else {
        uri.toString()
    }
}

private fun getGalleryImagePath(uri: Uri): String {
    var path = ""
    val pattern = Pattern.compile("(\\d+)$")
    val matcher = pattern.matcher(uri.toString())
    if (!matcher.find()) {
        return path
    }
    val imgId = matcher.group()
    val column = arrayOf(MediaStore.Images.Media.DATA)
    val selection = MediaStore.Images.Media._ID + "=?"
    val cursor: Cursor? = App.get().contentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, column, selection, arrayOf<String>(imgId), null)
    cursor?.run {
        if (this.moveToFirst()) {
            val columnIndex = this.getColumnIndex(column[0])
            path = this.getString(columnIndex)
        }
        cursor.close()
    }
    return path
}

@RequiresApi(Build.VERSION_CODES.KITKAT)
private fun getDownloadedImagePath(uri: Uri): String {
    var path = ""
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
        val docId = DocumentsContract.getDocumentId(uri)
        val contentUri: Uri = ContentUris.withAppendedId(Uri.parse(DOWNLOADS_FOLDER), docId.toLong())
        val column = arrayOf(MediaStore.Images.Media.DATA)

        val cursor: Cursor? = App.get().contentResolver.query(contentUri, column, null, null, null)
        cursor?.run {
            if (this.moveToFirst()) {
                val columnIndex = this.getColumnIndexOrThrow(column[0])
                path = this.getString(columnIndex)
            }
            cursor.close()
        }
    }

    return path
}

fun isDownloadsDocument(uri: Uri): Boolean {
    return DOWNLOADS_AUTHORITY == uri.authority
}