package com.cobeisfresh.azil.ui.register

import com.cobeisfresh.azil.presentation.base.BasePresenter

/**
 * Created by Zerina Salitrezic on 04/08/2017.
 */

interface RegisterInterface {

    interface View {

        fun showProgressBar()

        fun hideProgressBar()

        fun showNoInternetError()

        fun showServerError()

        fun showUserExistsError()

        fun setNameError()

        fun setEmailError()

        fun setPasswordError()

        fun setRetypedPasswordError()

        fun removeNameError()

        fun removeEmailError()

        fun removePasswordError()

        fun removePasswordsNotSameError()

        fun setNameGreenLine()

        fun setEmailGreenLine()

        fun setPasswordGreenLine()

        fun setRetypedPasswordGreenLine()

        fun setNameRedLine()

        fun setEmailRedLine()

        fun setPasswordRedLine()

        fun setRetypedPasswordRedLine()

        fun setNameBlueLine()

        fun setEmailBlueLine()

        fun setPasswordBlueLine()

        fun setRetypedPasswordBlueLine()

        fun enableRegister()

        fun disableRegister()

        fun goBack()

        fun showImageChooserDialog()

        fun startGallery()

        fun startCamera()

        fun loadImage(imagePath: String)

        fun setPasswordsNotSameError()

        fun showInvalidImageFormatError()

        fun showUploadImageError()

        fun goBackWithResult()

        fun goToTermsAndConditions()

        fun clearFocusFromInputFields()

        fun hideKeyboard()

        fun hideErrorLayout()

        fun requestFocusOnRootLayout()
    }

    interface Presenter : BasePresenter<View> {

        fun registerClicked(hasInternet: Boolean)

        fun termsAndConditionsClicked()

        fun touchRootLayout()

        fun clickedDoneOnRetypedPassword()

        fun nameChanged(name: String)

        fun emailChanged(email: String)

        fun passwordChanged(password: String)

        fun retypedPasswordChanged(retypePassword: String)

        fun iconBackClicked()

        fun onNameChangeFocus(hasFocus: Boolean)

        fun onEmailChangeFocus(hasFocus: Boolean)

        fun onPasswordChangeFocus(hasFocus: Boolean)

        fun onRetypedPasswordChangeFocus(hasFocus: Boolean)

        fun profileImageClicked()

        fun cameraChosen()

        fun galleryChosen()

        fun setImagePath(path: String)

        fun unSubscribe()

        fun imageCaptured()
    }
}