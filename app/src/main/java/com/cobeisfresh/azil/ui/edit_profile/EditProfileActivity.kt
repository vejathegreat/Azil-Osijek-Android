package com.cobeisfresh.azil.ui.edit_profile

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import com.cobeisfresh.azil.App
import com.cobeisfresh.azil.R
import com.cobeisfresh.azil.analytics.firebase.events.ScreenEvent
import com.cobeisfresh.azil.common.constants.USER_MODEL
import com.cobeisfresh.azil.common.extensions.*
import com.cobeisfresh.azil.common.utils.getRealImagePath
import com.cobeisfresh.azil.common.utils.hasInternet
import com.cobeisfresh.azil.common.utils.loadImage
import com.cobeisfresh.azil.data.response.UserResponse
import com.cobeisfresh.azil.ui.base.BasePicturesActivity
import com.cobeisfresh.azil.ui.base.REQUEST_CODE_CAMERA
import com.cobeisfresh.azil.ui.base.REQUEST_CODE_GALLERY
import kotlinx.android.synthetic.main.activity_edit_profile.*
import kotlinx.android.synthetic.main.error_layout.*
import javax.inject.Inject


/**
 * Created by Zerina Salitrezic on 07/08/2017.
 */

class EditProfileActivity : BasePicturesActivity(), EditProfileInterface.View {

    @Inject
    lateinit var presenter: EditProfileInterface.Presenter

    companion object {
        private const val USER = "user"

        fun getLaunchIntent(from: Context, userResponse: UserResponse): Intent {
            val intent = Intent(from, EditProfileActivity::class.java)
            intent.putExtra(USER, userResponse)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        App.get().getComponent().inject(this)
        setContentView(R.layout.activity_edit_profile)
        presenter.setView(this)
        initUi()
        sendScreenTrackingEvent(ScreenEvent.ScreenName.EDIT_PROFILE)
    }

    override fun initUi() {
        setDefaultLineColor()
        setListeners()
        getExtras()
    }

    private fun setDefaultLineColor() = fullName.changeLineColor(color(R.color.platinum))

    private fun getExtras() {
        val intent = intent
        intent.getSerializableExtra(USER)?.let {
            val userResponse = it as UserResponse
            presenter.setUserResponse(userResponse)
        }
    }

    private fun setListeners() {
        fullName.onFocusChange { presenter.onFullNameChangeFocus(it) }
        profileImageLayout.onClick { presenter.profileImageClicked() }
        iconBack.onClick { presenter.iconBackClicked() }
        save.onClick { presenter.saveClicked(hasInternet()) }
        fullName.onTextChange { presenter.fullNameChanged(it) }
        fullName.onActionDoneClicked { presenter.clickedDoneOnFullName() }
        rootLayout.onTouch { presenter.touchRootLayout() }
    }

    override fun hideErrorLayout() {
        if (errorLayout.isVisible()) {
            errorLayout.fadeOut()
        }
    }

    override fun hideKeyboard() = rootLayout.hideKeyboard()

    override fun requestFocusOnRootLayout() {
        rootLayout.requestFocus()
    }

    override fun clearFocusFromFullName() = fullName.clearFocus()

    override fun showProgressBar() = progressBar.visible()

    override fun hideProgressBar() = progressBar.gone()

    override fun showFullName(fullNameText: String) {
        fullName.setText(fullNameText)
    }

    override fun showProfileImage(avatar: String) = loadImage(profileImage, avatar)

    override fun setFullNameError() = fullName.setInputError(getString(R.string.edit_profile_first_last_name_invalid_error))

    override fun removeFullNameError() = fullName.removeInputError()

    override fun setFullNameBlueLine() = fullName.changeLineColor(color(R.color.turquoise))

    override fun setFullNameGreenLine() = fullName.changeLineColor(color(R.color.green))

    override fun setFullNameRedLine() = fullName.changeLineColor(color(R.color.red))

    override fun enableSave() = save.enable()

    override fun disableSave() = save.disable()

    override fun showNoInternetError() {
        errorText.text = getString(R.string.error_no_internet)
        showErrorLayout()
    }

    override fun showServerError() {
        errorText.text = getString(R.string.server_error)
        showErrorLayout()
    }

    override fun showInvalidImageFormatError() {
        errorText.text = getString(R.string.edit_profile_image_invalid_format_error)
        showErrorLayout()
    }

    override fun showUploadImageError() {
        errorText.text = getString(R.string.edit_profile_image_upload_error)
        showErrorLayout()
    }

    private fun showErrorLayout() {
        errorLayout.fadeIn()
        errorLayout.bringToFront()
    }

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

    override fun onBackPressed() = presenter.iconBackClicked()

    override fun goBack(userResponse: UserResponse) {
        val returnIntent = Intent()
        returnIntent.putExtra(USER_MODEL, userResponse)
        setResult(Activity.RESULT_OK, returnIntent)
        finish()
    }

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