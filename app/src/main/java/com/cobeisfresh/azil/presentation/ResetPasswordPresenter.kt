package com.cobeisfresh.azil.presentation

import com.cobeisfresh.azil.common.constants.ERROR_CODE_EXPIRED_RECOVERY_CODE
import com.cobeisfresh.azil.common.constants.ERROR_CODE_INVALID_PASSWORD
import com.cobeisfresh.azil.common.constants.ERROR_CODE_WRONG_RECOVERY_CODE
import com.cobeisfresh.azil.common.extensions.isValidCode
import com.cobeisfresh.azil.common.extensions.isValidPassword
import com.cobeisfresh.azil.data.models.ResetPasswordData
import com.cobeisfresh.azil.data.models.isResetPasswordFormValid
import com.cobeisfresh.azil.data.response.BaseResponse
import com.cobeisfresh.azil.network_interaction.UserInteractor
import com.cobeisfresh.azil.ui.forgot_reset_password.reset_password.ResetPasswordInterface
import com.jakewharton.retrofit2.adapter.rxjava2.HttpException
import io.reactivex.SingleObserver
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

/**
 * Created by Zerina Salitrezic on 18/08/2017.
 */

class ResetPasswordPresenter(private val userInteractor: UserInteractor) : ResetPasswordInterface.Presenter {

    private lateinit var view: ResetPasswordInterface.View
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()
    internal var resetPasswordData: ResetPasswordData = ResetPasswordData()

    override fun setView(view: ResetPasswordInterface.View) {
        this.view = view
    }

    override fun setEmail(email: String) {
        resetPasswordData.email = email
    }

    override fun codeChanged(code: String) {
        resetPasswordData.code = code
        view.removeCodeError()
        checkIsFormValid()
    }

    override fun newPasswordChanged(newPassword: String) {
        resetPasswordData.password = newPassword
        view.removeNewPasswordError()
        checkIsFormValid()
    }

    override fun retypedNewPasswordChanged(retypeNewPassword: String) {
        resetPasswordData.retypedPassword = retypeNewPassword
        view.removeRetypedNewPasswordError()
        checkIsFormValid()
    }

    internal fun checkIsFormValid() {
        if (resetPasswordData.isResetPasswordFormValid()) {
            view.enableReset()
        } else {
            view.disableReset()
        }
    }

    override fun onCodeChangeFocus(hasFocus: Boolean) {
        if (!hasFocus) {
            checkCodeValidation()
        } else {
            view.setCodeBlueLine()
        }
    }

    override fun onNewPasswordChangeFocus(hasFocus: Boolean) {
        if (!hasFocus) {
            checkNewPasswordValidation()
            checkRetypedNewPasswordValidation()
        } else {
            view.setNewPasswordBlueLine()
        }
    }

    override fun onRetypedNewPasswordChangeFocus(hasFocus: Boolean) {
        if (!hasFocus) {
            checkRetypedNewPasswordValidation()
        } else {
            view.setRetypedNewPasswordBlueLine()
        }
    }

    private fun checkCodeValidation() {
        if (resetPasswordData.code.isValidCode()) {
            view.setCodeGreenLine()
            view.removeCodeError()
        } else {
            view.setCodeRedLine()
            view.setCodeError()
        }
    }

    private fun checkNewPasswordValidation() {
        if (resetPasswordData.password.isValidPassword()) {
            view.setNewPasswordGreenLine()
            view.removeNewPasswordError()
        } else {
            view.setNewPasswordRedLine()
            view.setNewPasswordError()
        }
    }

    private fun checkRetypedNewPasswordValidation() {
        if (resetPasswordData.password == resetPasswordData.retypedPassword) {
            view.setRetypedNewPasswordGreenLine()
            view.removeRetypedNewPasswordError()
        } else {
            view.setRetypedNewPasswordRedLine()
            view.setPasswordsNotMatchError()
        }
    }

    override fun resetPasswordClicked(hasInternet: Boolean) {
        if (!hasInternet) {
            view.showNoInternetError()
        } else {
            view.showProgressBar()
            view.disableReset()
            userInteractor.resetPassword(resetPasswordData, getResetPasswordObserver())
        }
    }

    internal fun getResetPasswordObserver(): SingleObserver<BaseResponse> {
        return object : SingleObserver<BaseResponse> {
            override fun onSubscribe(d: Disposable?) {
                compositeDisposable.add(d)
            }

            override fun onSuccess(response: BaseResponse) {
                view.hideProgressBar()
                view.enableReset()
                view.goToLoginActivity()
            }

            override fun onError(e: Throwable?) {
                view.hideProgressBar()
                view.enableReset()
                if (e is HttpException) {
                    handleError(e.code())
                }
            }
        }
    }

    private fun handleError(code: Int) = when (code) {
        ERROR_CODE_INVALID_PASSWORD -> view.setNewPasswordError()
        ERROR_CODE_WRONG_RECOVERY_CODE -> view.showCodeWrongError()
        ERROR_CODE_EXPIRED_RECOVERY_CODE -> view.showCodeExpiredError()
        else -> view.showServerError()
    }

    override fun iconBackClicked() = view.goBack()

    override fun touchRootLayout() {
        view.clearFocusFromInputFields()
        view.hideKeyboard()
        view.hideErrorLayout()
    }

    override fun clickedDoneOnRetypedPassword() {
        view.requestFocusOnRootLayout()
        view.hideKeyboard()
    }

    override fun unSubscribe() = compositeDisposable.clear()
}