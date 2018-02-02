package com.cobeisfresh.azil.ui.base

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.support.v4.content.ContextCompat
import com.cobeisfresh.azil.R
import com.cobeisfresh.azil.common.extensions.showPermissionsDialog
import com.cobeisfresh.azil.common.utils.getMediaFileUri

/**
 * Created by Zerina Salitrezic on 23/01/2018.
 */

const val REQUEST_CODE_GALLERY = 1
const val REQUEST_CODE_CAMERA = 2
const val CONTENT_TYPE = "image/jpeg"
const val CAMERA_STORAGE_PERMISSION = 11
const val CAMERA_PERMISSION = 12
const val STORAGE_PERMISSION = 13

abstract class BasePicturesActivity : BaseActivity() {

    private var uri: Uri? = null
    private var onUri: (Uri) -> Unit = {}

    override fun initUi() {}

    protected fun startGallery(onGalleryUri: (Uri) -> Unit) {
        this.onUri = onGalleryUri
        val storagePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        if (storagePermission == PackageManager.PERMISSION_GRANTED) {
            val galleryIntent: Intent
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                galleryIntent = Intent(Intent.ACTION_OPEN_DOCUMENT)
                galleryIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
                galleryIntent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION)
            } else {
                galleryIntent = Intent(Intent.ACTION_GET_CONTENT)
            }
            galleryIntent.putExtra(Intent.EXTRA_LOCAL_ONLY, true)
            galleryIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            galleryIntent.type = CONTENT_TYPE
            startActivityForResult(galleryIntent, REQUEST_CODE_GALLERY)
        } else {
            showPermissionsDialog(message = getString(R.string.gallery_access), okAction = { onPermissionsOkClick(it) }, permissionKey = STORAGE_PERMISSION)
        }
    }

    private fun onPermissionsOkClick(permissionKey: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            when (permissionKey) {
                CAMERA_STORAGE_PERMISSION -> requestPermissions(arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE), REQUEST_CODE_CAMERA)
                CAMERA_PERMISSION -> requestPermissions(arrayOf(Manifest.permission.CAMERA), REQUEST_CODE_CAMERA)
                STORAGE_PERMISSION -> requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), REQUEST_CODE_GALLERY)
            }
        }
    }

    @SuppressLint("NewApi")
    protected fun startCamera(onCameraUri: (Uri) -> Unit) {
        this.onUri = onCameraUri
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (cameraIntent.resolveActivity(packageManager) != null) {
            val cameraPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
            val storagePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)

            val denied = PackageManager.PERMISSION_DENIED
            val granted = PackageManager.PERMISSION_GRANTED
            val isMinMarshmallow = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M

            when {
                cameraPermission == granted && storagePermission == granted -> {
                    uri = getMediaFileUri()
                    uri?.run {
                        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, this)
                        startActivityForResult(cameraIntent, REQUEST_CODE_CAMERA)
                    }
                }

                cameraPermission == denied && storagePermission == denied && isMinMarshmallow -> showPermissionsDialog(message = getString(R.string.camera_gallery_access),
                        okAction = { onPermissionsOkClick(it) }, permissionKey = CAMERA_STORAGE_PERMISSION)
                cameraPermission == denied && isMinMarshmallow -> showPermissionsDialog(message = getString(R.string.camera_access),
                        okAction = { onPermissionsOkClick(it) }, permissionKey = CAMERA_PERMISSION)
                storagePermission == denied && isMinMarshmallow -> showPermissionsDialog(message = getString(R.string.gallery_access),
                        okAction = { onPermissionsOkClick(it) }, permissionKey = STORAGE_PERMISSION)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_CODE_GALLERY -> data?.data?.run {
                    val pickedImageUri: Uri = this
                    onUri(pickedImageUri)
                }
                REQUEST_CODE_CAMERA -> uri?.run { onUri(this) }
            }
        }
    }
}