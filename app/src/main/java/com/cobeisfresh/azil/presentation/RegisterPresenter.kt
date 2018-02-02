package com.cobeisfresh.azil.presentation

import com.cobeisfresh.azil.common.constants.ERROR_CODE_IMAGE_UPLOAD
import com.cobeisfresh.azil.common.constants.ERROR_CODE_USER_ALREADY_EXISTS
import com.cobeisfresh.azil.common.constants.ERROR_CODE_WRONG_TYPE_FILE
import com.cobeisfresh.azil.common.extensions.isValid
import com.cobeisfresh.azil.common.extensions.isValidEmail
import com.cobeisfresh.azil.common.extensions.isValidName
import com.cobeisfresh.azil.common.extensions.isValidPassword
import com.cobeisfresh.azil.common.utils.getResizedImageFile
import com.cobeisfresh.azil.common.utils.validateString
import com.cobeisfresh.azil.data.models.UserRegistrationData
import com.cobeisfresh.azil.data.models.isRegisterFormValid
import com.cobeisfresh.azil.data.response.LoginResponse
import com.cobeisfresh.azil.network_interaction.UserInteractor
import com.cobeisfresh.azil.shared_prefs.SharedPrefs
import com.cobeisfresh.azil.ui.register.RegisterInterface
import com.jakewharton.retrofit2.adapter.rxjava2.HttpException
import io.reactivex.SingleObserver
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import java.io.File

/**
 * Created by Zerina Salitrezic on 04/08/2017.
 */

class RegisterPresenter(private val userInteractor: UserInteractor,
                        private val sharedPrefs: SharedPrefs) : RegisterInterface.Presenter {

    private lateinit var view: RegisterInterface.View
    private val compositeDisposable = CompositeDisposable()
    internal var userData: UserRegistrationData = UserRegistrationData()
    internal var imagePath = ""

    override fun setView(view: RegisterInterface.View) {
        this.view = view
    }

    override fun nameChanged(name: String) {
        userData.name = name
        view.removeNameError()
        checkIsFormValid()
    }

    override fun emailChanged(email: String) {
        userData.email = email
        view.removeEmailError()
        checkIsFormValid()
    }

    override fun passwordChanged(password: String) {
        userData.password = password
        view.removePasswordError()
        checkIsFormValid()
    }

    override fun retypedPasswordChanged(retypePassword: String) {
        userData.repeatedPassword = retypePassword
        view.removePasswordsNotSameError()
        checkIsFormValid()
    }

    internal fun checkIsFormValid() {
        if (userData.isRegisterFormValid()) {
            view.enableRegister()
        } else {
            view.disableRegister()
        }
    }

    override fun onNameChangeFocus(hasFocus: Boolean) {
        if (!hasFocus) {
            checkNameValidation()
        } else {
            view.setNameBlueLine()
        }
    }

    override fun onEmailChangeFocus(hasFocus: Boolean) {
        if (!hasFocus) {
            checkEmailValidation()
        } else {
            view.setEmailBlueLine()
        }
    }

    override fun onPasswordChangeFocus(hasFocus: Boolean) {
        if (!hasFocus) {
            checkPasswordValidation()
            checkRetypedPasswordValidation()
        } else {
            view.setPasswordBlueLine()
        }
    }

    override fun onRetypedPasswordChangeFocus(hasFocus: Boolean) {
        if (!hasFocus) {
            checkRetypedPasswordValidation()
        } else {
            view.setRetypedPasswordBlueLine()
        }
    }

    private fun checkNameValidation() {
        if (userData.name.isValidName()) {
            view.setNameGreenLine()
            view.removeNameError()
        } else {
            view.setNameRedLine()
            view.setNameError()
        }
    }

    private fun checkEmailValidation() {
        if (userData.email.isValidEmail()) {
            view.setEmailGreenLine()
            view.removeEmailError()
        } else {
            view.setEmailRedLine()
            view.setEmailError()
        }
    }

    private fun checkPasswordValidation() {
        if (userData.password.isValidPassword()) {
            view.setPasswordGreenLine()
            view.removePasswordError()
        } else {
            view.setPasswordRedLine()
            view.setPasswordError()
        }
    }

    private fun checkRetypedPasswordValidation() {
        if (userData.password == userData.repeatedPassword) {
            view.setRetypedPasswordGreenLine()
            view.removePasswordsNotSameError()
        } else {
            view.setRetypedPasswordRedLine()
            view.setPasswordsNotSameError()
        }
    }

    override fun registerClicked(hasInternet: Boolean) {
        if (!hasInternet) {
            view.showNoInternetError()
        } else {
            var file: File? = null
            if (imagePath.isValid()) {
                file = getResizedImageFile(imagePath)
            }

            view.showProgressBar()
            view.disableRegister()
            userInteractor.registerUser(userData, file, getRegisterObserver())
        }
    }

    internal fun getRegisterObserver(): SingleObserver<LoginResponse> {
        return object : SingleObserver<LoginResponse> {
            override fun onSubscribe(d: Disposable?) {
                compositeDisposable.add(d)
            }

            override fun onSuccess(response: LoginResponse) {
                view.hideProgressBar()
                handleResponse(response)
            }

            override fun onError(e: Throwable?) {
                view.enableRegister()
                view.hideProgressBar()
                if (e is HttpException) {
                    handleError(e.code())
                }
            }
        }
    }

    private fun handleError(code: Int) = when (code) {
        ERROR_CODE_USER_ALREADY_EXISTS -> view.showUserExistsError()
        ERROR_CODE_WRONG_TYPE_FILE -> view.showInvalidImageFormatError()
        ERROR_CODE_IMAGE_UPLOAD -> view.showUploadImageError()
        else -> view.showServerError()
    }

    private fun handleResponse(response: LoginResponse) {
        sharedPrefs.setLoggedIn(true)
        sharedPrefs.setToken(response.token)
        view.goBackWithResult()
    }

    override fun termsAndConditionsClicked() = view.goToTermsAndConditions()

    override fun iconBackClicked() = view.goBack()

    override fun profileImageClicked() = view.showImageChooserDialog()

    override fun cameraChosen() = view.startCamera()

    override fun galleryChosen() = view.startGallery()

    override fun setImagePath(path: String) {
        validateString(value = path, onValid = {
            imagePath = path
            view.loadImage(imagePath)
        })
    }

    override fun touchRootLayout() {
        handleLosingInputFieldsFocus()
        view.hideErrorLayout()
    }

    override fun clickedDoneOnRetypedPassword() = handleLosingInputFieldsFocus()

    private fun handleLosingInputFieldsFocus() {
        view.clearFocusFromInputFields()
        view.requestFocusOnRootLayout()
        view.hideKeyboard()
    }

    override fun imageCaptured() = view.loadImage(imagePath)

    override fun unSubscribe() = compositeDisposable.clear()
}