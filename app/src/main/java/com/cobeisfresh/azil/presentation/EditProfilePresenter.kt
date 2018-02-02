package com.cobeisfresh.azil.presentation

import com.cobeisfresh.azil.common.constants.ERROR_CODE_IMAGE_UPLOAD
import com.cobeisfresh.azil.common.constants.ERROR_CODE_WRONG_TYPE_FILE
import com.cobeisfresh.azil.common.extensions.isValidName
import com.cobeisfresh.azil.common.utils.getResizedImageFile
import com.cobeisfresh.azil.common.utils.validateString
import com.cobeisfresh.azil.data.request.UserRequest
import com.cobeisfresh.azil.data.response.UserResponse
import com.cobeisfresh.azil.network_interaction.UserInteractor
import com.cobeisfresh.azil.ui.edit_profile.EditProfileInterface
import com.jakewharton.retrofit2.adapter.rxjava2.HttpException
import io.reactivex.SingleObserver
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import java.io.File

/**
 * Created by Zerina Salitrezic on 07/08/2017.
 */

class EditProfilePresenter(private val userInteractor: UserInteractor) : EditProfileInterface.Presenter {

    private lateinit var view: EditProfileInterface.View
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()
    internal var userResponse: UserResponse = UserResponse()
    internal var imagePath: String = ""
    internal var fullName: String = ""

    override fun setView(view: EditProfileInterface.View) {
        this.view = view
    }

    override fun setUserResponse(response: UserResponse) = with(response) {
        userResponse = this
        showImage(profileImage)
        showFullName(name)
    }

    private fun showImage(avatar: String) = validateString(value = avatar, onValid = { view.showProfileImage(it) })

    private fun showFullName(firstLastName: String) {
        validateString(value = firstLastName, onValid = { view.showFullName(it) })
        checkFullNameValidation()
    }

    override fun fullNameChanged(fullName: String) {
        this.fullName = fullName
        view.removeFullNameError()
        checkIsFormValid()
    }

    internal fun checkIsFormValid() {
        if (fullName.isValidName()) {
            view.enableSave()
        } else {
            view.disableSave()
        }
    }

    override fun onFullNameChangeFocus(hasFocus: Boolean) {
        if (!hasFocus) {
            checkFullNameValidation()
        } else {
            view.setFullNameBlueLine()
        }
    }

    private fun checkFullNameValidation() {
        if (fullName.isValidName()) {
            view.setFullNameGreenLine()
            view.removeFullNameError()
        } else {
            view.setFullNameRedLine()
            view.setFullNameError()
        }
    }

    override fun profileImageClicked() = view.showImageChooserDialog()

    override fun galleryChosen() = view.startGallery()

    override fun cameraChosen() = view.startCamera()

    override fun saveClicked(hasInternet: Boolean) {
        if (!hasInternet) {
            view.showNoInternetError()
        } else {
            var file: File? = null
            var userRequest: UserRequest? = null
            view.showProgressBar()
            view.disableSave()
            validateString(value = imagePath, onValid = { file = getResizedImageFile(imagePath) })
            validateString(value = fullName, onValid = { userRequest = UserRequest(fullName) })
            userInteractor.setMe(userRequest, file, getMeObserver())
        }
    }

    internal fun getMeObserver(): SingleObserver<UserResponse> {
        return object : SingleObserver<UserResponse> {
            override fun onSubscribe(d: Disposable?) {
                compositeDisposable.add(d)
            }

            override fun onSuccess(response: UserResponse) {
                view.hideProgressBar()
                userResponse.profileImage = response.profileImage
                userResponse.name = response.name
                view.goBack(userResponse)
            }

            override fun onError(e: Throwable?) {
                view.hideProgressBar()
                view.enableSave()
                if (e is HttpException) {
                    handleError(e.code())
                }
            }
        }
    }

    internal fun handleError(code: Int) = when (code) {
        ERROR_CODE_WRONG_TYPE_FILE -> view.showInvalidImageFormatError()
        ERROR_CODE_IMAGE_UPLOAD -> view.showUploadImageError()
        else -> view.showServerError()
    }

    override fun setImagePath(path: String) {
        validateString(value = path, onValid = {
            imagePath = it
            showImage(imagePath)
        })
    }

    override fun iconBackClicked() = view.goBack(userResponse)

    override fun imageCaptured() = showImage(imagePath)

    override fun touchRootLayout() {
        view.clearFocusFromFullName()
        view.hideKeyboard()
        view.hideErrorLayout()
    }

    override fun clickedDoneOnFullName() {
        view.requestFocusOnRootLayout()
        view.hideKeyboard()
    }

    override fun unSubscribe() = compositeDisposable.clear()
}