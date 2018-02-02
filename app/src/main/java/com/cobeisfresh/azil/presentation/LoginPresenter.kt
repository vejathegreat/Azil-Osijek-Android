package com.cobeisfresh.azil.presentation

import com.cobeisfresh.azil.common.constants.ERROR_CODE_INVALID_PASSWORD
import com.cobeisfresh.azil.common.constants.ERROR_CODE_USER_NOT_REGISTERED
import com.cobeisfresh.azil.common.extensions.isValidEmail
import com.cobeisfresh.azil.common.extensions.isValidPassword
import com.cobeisfresh.azil.data.response.LoginResponse
import com.cobeisfresh.azil.network_interaction.UserInteractor
import com.cobeisfresh.azil.shared_prefs.SharedPrefs
import com.cobeisfresh.azil.ui.login.LoginInterface
import com.jakewharton.retrofit2.adapter.rxjava2.HttpException
import io.reactivex.SingleObserver
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

/**
 * Created by Zerina Salitrezic on 04/08/2017.
 */

class LoginPresenter(private val userInteractor: UserInteractor,
                     private val sharedPrefs: SharedPrefs) : LoginInterface.Presenter {

    private lateinit var view: LoginInterface.View
    private val compositeDisposable = CompositeDisposable()
    internal var email = ""
    internal var password = ""

    override fun setView(view: LoginInterface.View) {
        this.view = view
    }

    override fun emailChanged(email: String) {
        this.email = email
        view.removeEmailError()
        checkIsFormValid()
    }

    override fun passwordChanged(password: String) {
        this.password = password
        view.removePasswordError()
        checkIsFormValid()
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
        } else {
            view.setPasswordBlueLine()
        }
    }

    private fun checkEmailValidation() {
        if (email.isValidEmail()) {
            view.setEmailGreenLine()
            view.removeEmailError()
        } else {
            view.setEmailRedLine()
            view.setEmailError()
        }
    }

    private fun checkPasswordValidation() {
        if (password.isValidPassword()) {
            view.setPasswordGreenLine()
            view.removePasswordError()
        } else {
            view.setPasswordRedLine()
            view.setPasswordError()
        }
    }

    internal fun checkIsFormValid() {
        if (email.isValidEmail() && password.isValidPassword()) {
            view.enableLogin()
        } else {
            view.disableLogin()
        }
    }

    override fun loginClicked(hasInternet: Boolean) {
        if (!hasInternet) {
            view.showNoInternetError()
        } else {
            view.showProgressBar()
            view.disableLogin()
            userInteractor.loginUser(email, password, getLoginObserver())
        }
    }

    internal fun getLoginObserver(): SingleObserver<LoginResponse> {
        return object : SingleObserver<LoginResponse> {
            override fun onSubscribe(d: Disposable?) {
                compositeDisposable.add(d)
            }

            override fun onSuccess(response: LoginResponse) {
                view.hideProgressBar()
                handleLoginResponse(response)
            }

            override fun onError(e: Throwable?) {
                view.enableLogin()
                view.hideProgressBar()
                if (e is HttpException) {
                    handleError(e.code())
                }
            }
        }
    }

    private fun handleLoginResponse(response: LoginResponse) {
        sharedPrefs.setLoggedIn(true)
        sharedPrefs.setToken(response.token)
        view.goBackWithResult()
    }

    private fun handleError(code: Int) = when (code) {
        ERROR_CODE_INVALID_PASSWORD -> view.showInvalidPasswordError()
        ERROR_CODE_USER_NOT_REGISTERED -> view.showNotRegisteredUserError()
        else -> view.showServerError()
    }

    override fun forgotPasswordClicked() = view.openForgotPasswordActivity()

    override fun iconBackClicked() = view.goBack()

    override fun registerClicked() = view.openRegisterActivity()

    override fun touchRootLayout() {
        view.clearFocusFromInputFields()
        view.hideKeyboard()
        view.hideErrorLayout()
    }

    override fun clickedDoneOnPassword() {
        view.requestFocusOnRootLayout()
        view.hideKeyboard()
    }

    override fun unSubscribe() = compositeDisposable.clear()
}