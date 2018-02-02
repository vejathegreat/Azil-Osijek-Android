package com.cobeisfresh.azil.presentation

import com.cobeisfresh.azil.common.constants.ERROR_CODE_USER_NOT_REGISTERED
import com.cobeisfresh.azil.common.extensions.isValidEmail
import com.cobeisfresh.azil.data.response.BaseResponse
import com.cobeisfresh.azil.network_interaction.UserInteractor
import com.cobeisfresh.azil.ui.forgot_reset_password.forgot_password.ForgotPasswordInterface
import com.jakewharton.retrofit2.adapter.rxjava2.HttpException
import io.reactivex.SingleObserver
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

/**
 * Created by Zerina Salitrezic on 18/08/2017.
 */

class ForgotPasswordPresenter(private val userInteractor: UserInteractor) : ForgotPasswordInterface.Presenter {

    private lateinit var view: ForgotPasswordInterface.View
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()
    internal var email = ""

    override fun setView(view: ForgotPasswordInterface.View) {
        this.view = view
    }

    override fun emailChanged(email: String) {
        this.email = email
        view.removeEmailError()
        checkIsFormValid()
    }

    internal fun checkIsFormValid() {
        if (email.isValidEmail()) {
            view.enableSendButton()
        } else {
            view.disableSendButton()
        }
    }

    override fun onEmailChangeFocus(hasFocus: Boolean) {
        if (!hasFocus) {
            checkEmailValidation()
        } else {
            view.setEmailBlueLine()
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

    override fun sendButtonClicked(hasInternet: Boolean) {
        if (!hasInternet) {
            view.showNoInternetError()
        } else {
            view.disableSendButton()
            view.showProgressBar()
            userInteractor.forgotPassword(email, getForgotPasswordObserver())
        }
    }

    internal fun getForgotPasswordObserver(): SingleObserver<BaseResponse> {
        return object : SingleObserver<BaseResponse> {
            override fun onSubscribe(d: Disposable?) {
                compositeDisposable.add(d)
            }

            override fun onSuccess(response: BaseResponse) {
                view.hideProgressBar()
                view.enableSendButton()
                view.goToResetPasswordActivity(email)
            }

            override fun onError(e: Throwable?) {
                view.hideProgressBar()
                view.enableSendButton()
                if (e is HttpException) {
                    handleError(e.code())
                }
            }
        }
    }

    internal fun handleError(code: Int) {
        if (code == ERROR_CODE_USER_NOT_REGISTERED) {
            view.showUnknownUserError()
        } else {
            view.showServerError()
        }
    }

    override fun iconBackClicked() = view.goBack()

    override fun touchRootLayout() {
        view.clearFocusFromEmail()
        view.hideKeyboard()
        view.hideErrorLayout()
    }

    override fun clickedDoneOnEmail() {
        view.requestFocusOnRootLayout()
        view.hideKeyboard()
    }

    override fun unSubscribe() = compositeDisposable.clear()
}