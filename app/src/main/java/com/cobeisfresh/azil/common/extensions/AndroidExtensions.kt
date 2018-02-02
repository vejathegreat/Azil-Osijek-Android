package com.cobeisfresh.azil.common.extensions

import android.content.Context
import android.support.annotation.ColorRes
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import com.cobeisfresh.azil.R
import com.cobeisfresh.azil.data.models.DogModel
import java.util.*

/**
 * Created by Zerina Salitrezic on 31/07/2017.
 */

private fun AlertDialog.Builder.display() = create().show()

fun Context.showInfoDialogWithCancel(title: String, message: String, okAction: () -> Unit) =
        AlertDialog.Builder(this)
                .setCancelable(false)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(R.string.ok_button, { _, _ -> okAction() })
                .setNegativeButton(R.string.cancel_button) { dialog, _ -> dialog.dismiss() }
                .display()

fun Context.showInfoDialog(message: String, okAction: () -> Unit = {}) =
        AlertDialog.Builder(this)
                .setCancelable(false)
                .setMessage(message)
                .setPositiveButton(R.string.ok_button, { _, _ -> okAction() })
                .display()

fun Context.showYesNoDialog(message: String, yesAction: () -> Unit = {}) =
        AlertDialog.Builder(this)
                .setCancelable(false)
                .setMessage(message)
                .setPositiveButton(R.string.yes, { _, _ -> yesAction() })
                .setNegativeButton(R.string.no, { dialog, _ -> dialog.dismiss() })
                .display()

fun Context.showChangeImageDialog(cameraAction: () -> Unit, galleryAction: () -> Unit) =
        AlertDialog.Builder(this)
                .setCancelable(false)
                .setMessage(R.string.choose_change_image)
                .setNegativeButton(R.string.edit_profile_gallery_camera_selection_alert_camera_option, { _, _ -> cameraAction() })
                .setPositiveButton(R.string.edit_profile_gallery_camera_selection_alert_gallery_option, { _, _ -> galleryAction() })
                .setNeutralButton(R.string.cancel_button, null)
                .display()

fun Context.showPermissionsDialog(message: String, okAction: (Int) -> Unit, permissionKey: Int) =
        AlertDialog.Builder(this)
                .setCancelable(false)
                .setMessage(message)
                .setPositiveButton(R.string.ok_button) { _, _ -> okAction(permissionKey) }
                .setNegativeButton(R.string.cancel_button, null)
                .display()

fun Context.showDeleteDogDialog(dog: DogModel, okAction: (DogModel) -> Unit) =
        AlertDialog.Builder(this)
                .setCancelable(false)
                .setTitle(getString(R.string.mypets_remove_pet_from_favorites_alert_title))
                .setMessage(String.format(Locale.getDefault(), getString(R.string.mypets_remove_pet_from_favorites_alert_message), dog.name))
                .setPositiveButton(R.string.ok_button, { _, _ -> okAction(dog) })
                .setNegativeButton(R.string.cancel_button) { dialog, _ -> dialog.dismiss() }
                .display()

fun Context.color(@ColorRes id: Int): Int = ContextCompat.getColor(this, id)