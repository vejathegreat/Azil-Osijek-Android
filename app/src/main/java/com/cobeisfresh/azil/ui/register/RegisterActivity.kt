package com.cobeisfresh.azil.ui.register

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import com.cobeisfresh.azil.App
import com.cobeisfresh.azil.R
import com.cobeisfresh.azil.analytics.firebase.events.ScreenEvent
import com.cobeisfresh.azil.common.constants.TERMS_AND_CONDITIONS
import com.cobeisfresh.azil.common.extensions.*
import com.cobeisfresh.azil.common.utils.getRealImagePath
import com.cobeisfresh.azil.common.utils.hasInternet
import com.cobeisfresh.azil.common.utils.loadImage
import com.cobeisfresh.azil.ui.base.BasePicturesActivity
import com.cobeisfresh.azil.ui.base.REQUEST_CODE_CAMERA
import com.cobeisfresh.azil.ui.base.REQUEST_CODE_GALLERY
import com.cobeisfresh.azil.ui.web_view.WebViewActivity
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.error_layout.*
import javax.inject.Inject

/**
 * Created by Zerina Salitrezic on 04/08/2017.
 */

class RegisterActivity : BasePicturesActivity(), RegisterInterface.View {

    @Inject
    lateinit var presenter: RegisterInterface.Presenter

    companion object {
        fun getLaunchIntent(from: Context): Intent = Intent(from, RegisterActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        App.get().getComponent().inject(this)
        setContentView(R.layout.activity_register)
        presenter.setView(this)
        initUi()
        sendScreenTrackingEvent(ScreenEvent.ScreenName.REGISTRATION)
    }

    override fun initUi() {
        setDefaultLineColors()
        setListeners()
    }

    private fun setDefaultLineColors() {
        name.changeLineColor(color(R.color.platinum))
        email.changeLineColor(color(R.color.platinum))
        password.changeLineColor(color(R.color.platinum))
        retypePassword.changeLineColor(color(R.color.platinum))
    }

    private fun setListeners() {
        name.onFocusChange { presenter.onNameChangeFocus(it) }
        email.onFocusChange { presenter.onEmailChangeFocus(it) }
        password.onFocusChange { presenter.onPasswordChangeFocus(it) }
        retypePassword.onFocusChange { presenter.onRetypedPasswordChangeFocus(it) }
        profileImage.onClick { presenter.profileImageClicked() }
        register.onClick { presenter.registerClicked(hasInternet()) }
        termsAndConditions.onClick { presenter.termsAndConditionsClicked() }
        iconBack.onClick { presenter.iconBackClicked() }
        rootLayout.onTouch { presenter.touchRootLayout() }
        retypePassword.onActionDoneClicked { presenter.clickedDoneOnRetypedPassword() }
        name.onTextChange { presenter.nameChanged(it) }
        email.onTextChange { presenter.emailChanged(it) }
        password.onTextChange { presenter.passwordChanged(it) }
        retypePassword.onTextChange { presenter.retypedPasswordChanged(it) }
    }

    override fun clearFocusFromInputFields() {
        name.clearFocus()
        email.clearFocus()
        password.clearFocus()
        retypePassword.clearFocus()
    }

    override fun hideKeyboard() = rootLayout.hideKeyboard()

    override fun requestFocusOnRootLayout() {
        rootLayout.requestFocus()
    }

    override fun hideErrorLayout() {
        if (errorLayout.isVisible()) {
            errorLayout.fadeOut()
        }
    }

    override fun showProgressBar() = progressBar.visible()

    override fun hideProgressBar() = progressBar.gone()

    override fun showNoInternetError() {
        errorText.text = getString(R.string.error_no_internet)
        showErrorLayout()
    }

    override fun showServerError() {
        errorText.text = getString(R.string.server_error)
        showErrorLayout()
    }

    override fun showInvalidImageFormatError() {
        errorText.text = getString(R.string.registration_image_invalid_format_error)
        showErrorLayout()
    }

    override fun showUploadImageError() {
        errorText.text = getString(R.string.registration_image_upload_error)
        showErrorLayout()
    }

    override fun showUserExistsError() {
        errorText.text = getString(R.string.registration_user_already_registered_error)
        showErrorLayout()
    }

    private fun showErrorLayout() {
        errorLayout.fadeIn()
        errorLayout.bringToFront()
    }

    override fun setNameError() = name.setInputError(getString(R.string.registration_first_last_name_invalid_error))

    override fun setEmailError() = email.setInputError(getString(R.string.registration_email_invalid_error))

    override fun setPasswordError() = password.setInputError(getString(R.string.registration_password_invalid_error))

    override fun setRetypedPasswordError() = retypePassword.setInputError(getString(R.string.registration_password_invalid_error))

    override fun setPasswordsNotSameError() = retypePassword.setInputError(getString(R.string.registration_passwords_dont_match_error))

    override fun removeNameError() = name.removeInputError()

    override fun removeEmailError() = email.removeInputError()

    override fun removePasswordError() = password.removeInputError()

    override fun removePasswordsNotSameError() = retypePassword.removeInputError()

    override fun setNameGreenLine() = name.changeLineColor(color(R.color.green))

    override fun setEmailGreenLine() = email.changeLineColor(color(R.color.green))

    override fun setPasswordGreenLine() = password.changeLineColor(color(R.color.green))

    override fun setRetypedPasswordGreenLine() = retypePassword.changeLineColor(color(R.color.green))

    override fun setNameRedLine() = name.changeLineColor(color(R.color.red))

    override fun setEmailRedLine() = email.changeLineColor(color(R.color.red))

    override fun setPasswordRedLine() = password.changeLineColor(color(R.color.red))

    override fun setRetypedPasswordRedLine() = retypePassword.changeLineColor(color(R.color.red))

    override fun setNameBlueLine() = name.changeLineColor(color(R.color.turquoise))

    override fun setEmailBlueLine() = email.changeLineColor(color(R.color.turquoise))

    override fun setPasswordBlueLine() = password.changeLineColor(color(R.color.turquoise))

    override fun setRetypedPasswordBlueLine() = retypePassword.changeLineColor(color(R.color.turquoise))

    override fun enableRegister() = register.enable()

    override fun disableRegister() = register.disable()

    override fun showImageChooserDialog() {
        showChangeImageDialog(cameraAction = { presenter.cameraChosen() },
                galleryAction = { presenter.galleryChosen() })
    }

    override fun startGallery() {
        startGallery(onGalleryUri = {
            val path = getRealImagePath(it)
            presenter.setImagePath(path)
        })
    }

    override fun startCamera() {
        startCamera(onCameraUri = {
            presenter.setImagePath(it.path)
        })
    }

    override fun loadImage(imagePath: String) = loadImage(profileImage, imagePath)

    override fun goBack() = finish()

    override fun goBackWithResult() {
        val returnIntent = Intent()
        setResult(Activity.RESULT_OK, returnIntent)
        finish()
    }

    override fun goToTermsAndConditions() = startActivity(WebViewActivity.getLaunchIntent(this, TERMS_AND_CONDITIONS))

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            when (requestCode) {
                REQUEST_CODE_GALLERY -> startGallery()
                REQUEST_CODE_CAMERA -> startCamera()
            }
        }
    }

    override fun onPause() {
        presenter.unSubscribe()
        super.onPause()
    }
}