package com.cobeisfresh.azil.ui.edit_profile

import com.cobeisfresh.azil.data.response.UserResponse
import com.cobeisfresh.azil.presentation.base.BasePresenter

/**
 * Created by Zerina Salitrezic on 07/08/2017.
 */

interface EditProfileInterface {

    interface View {

        fun showProfileImage(avatar: String)

        fun startGallery()

        fun startCamera()

        fun showNoInternetError()

        fun showServerError()

        fun goBack(userResponse: UserResponse)

        fun showImageChooserDialog()

        fun showFullName(fullNameText: String)

        fun setFullNameBlueLine()

        fun removeFullNameError()

        fun setFullNameGreenLine()

        fun setFullNameRedLine()

        fun setFullNameError()

        fun enableSave()

        fun disableSave()

        fun showInvalidImageFormatError()

        fun showUploadImageError()

        fun clearFocusFromFullName()

        fun hideKeyboard()

        fun hideErrorLayout()

        fun requestFocusOnRootLayout()

        fun showProgressBar()

        fun hideProgressBar()
    }

    interface Presenter : BasePresenter<View> {

        fun fullNameChanged(fullName: String)

        fun onFullNameChangeFocus(hasFocus: Boolean)

        fun profileImageClicked()

        fun iconBackClicked()

        fun setUserResponse(response: UserResponse)

        fun galleryChosen()

        fun cameraChosen()

        fun saveClicked(hasInternet: Boolean)

        fun setImagePath(path: String)

        fun imageCaptured()

        fun touchRootLayout()

        fun clickedDoneOnFullName()

        fun unSubscribe()
    }
}