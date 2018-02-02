package com.cobeisfresh.azil.presentation

import com.cobeisfresh.azil.common.constants.ERROR_CODE_INVALID_PASSWORD
import com.cobeisfresh.azil.common.constants.ERROR_CODE_PASSWORDS_MISMATCHED
import com.cobeisfresh.azil.common.extensions.isValidPassword
import com.cobeisfresh.azil.data.models.ChangePasswordData
import com.cobeisfresh.azil.data.models.isChangePasswordFormValid
import com.cobeisfresh.azil.data.response.BaseResponse
import com.cobeisfresh.azil.network_interaction.UserInteractor
import com.cobeisfresh.azil.ui.edit_password.EditPasswordInterface
import com.jakewharton.retrofit2.adapter.rxjava2.HttpException
import io.reactivex.SingleObserver
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

/**
 * Created by Zerina Salitrezic on 18/08/2017.
 */

class EditPasswordPresenter(private val userInteractor: UserInteractor) : EditPasswordInterface.Presenter {

    private lateinit var view: EditPasswordInterface.View
    private val compositeDisposable = CompositeDisposable()
    internal var changePasswordData = ChangePasswordData()

    override fun setView(view: EditPasswordInterface.View) {
        this.view = view
    }

    override fun oldPasswordChanged(oldPassword: String) {
        changePasswordData.oldPassword = oldPassword
        view.removeOldPasswordError()
        checkIsFormValid()
    }

    override fun newPasswordChanged(newPassword: String) {
        changePasswordData.newPassword = newPassword
        view.removeNewPasswordError()
        checkIsFormValid()
    }

    override fun retypedNewPasswordChanged(retypeNewPassword: String) {
        changePasswordData.retypedNewPassword = retypeNewPassword
        view.removeRetypedPasswordError()
        checkIsFormValid()
    }

    internal fun checkIsFormValid() {
        if (changePasswordData.isChangePasswordFormValid()) {
            view.enableChange()
        } else {
            view.disableChange()
        }
    }

    override fun onOldPasswordChangeFocus(hasFocus: Boolean) {
        if (!hasFocus) {
            checkOldPasswordValidation()
        } else {
            view.setOldPasswordBlueLine()
        }
    }

    override fun onNewPasswordChangeFocus(hasFocus: Boolean) {
        if (!hasFocus) {
            checkNewPasswordValidation()
            checkRetypedPasswordValidation()
        } else {
            view.setNewPasswordBlueLine()
        }
    }

    override fun onRetypedNewPasswordChangeFocus(hasFocus: Boolean) {
        if (!hasFocus) {
            checkRetypedPasswordValidation()
        } else {
            view.setRetypedPasswordBlueLine()
        }
    }

    private fun checkOldPasswordValidation() {
        if (changePasswordData.oldPassword.isValidPassword()) {
            view.setOldPasswordGreenLine()
            view.removeOldPasswordError()
        } else {
            view.setOldPasswordRedLine()
            view.setOldPasswordError()
        }
    }

    private fun checkNewPasswordValidation() {
        if (changePasswordData.newPassword.isValidPassword()) {
            view.setNewPasswordGreenLine()
            view.removeNewPasswordError()
        } else {
            view.setNewPasswordRedLine()
            view.setNewPasswordError()
        }
    }

    private fun checkRetypedPasswordValidation() {
        if (changePasswordData.newPassword == changePasswordData.retypedNewPassword) {
            view.setRetypedPasswordGreenLine()
            view.removeRetypedPasswordError()
        } else {
            view.setRetypedPasswordRedLine()
            view.setPasswordsNotSameError()
        }
    }

    override fun changePasswordClicked(hasInternet: Boolean) {
        if (!hasInternet) {
            view.showNoInternetError()
        } else {
            view.showProgressBar()
            view.disableChange()
            userInteractor.changePassword(changePasswordData, getChangePasswordObserver())
        }
    }

    internal fun getChangePasswordObserver(): SingleObserver<BaseResponse> {
        return object : SingleObserver<BaseResponse> {
            override fun onSubscribe(d: Disposable?) {
                compositeDisposable.add(d)
            }

            override fun onSuccess(response: BaseResponse) {
                view.hideProgressBar()
                view.showPasswordChangedSuccess()
            }

            override fun onError(e: Throwable?) {
                view.hideProgressBar()
                view.enableChange()
                if (e is HttpException) {
                    handleError(e.code())
                }
            }
        }
    }

    internal fun handleError(code: Int) = when (code) {
        ERROR_CODE_INVALID_PASSWORD -> view.showPasswordInvalidError()
        ERROR_CODE_PASSWORDS_MISMATCHED -> view.showPasswordsMismatchedError()
        else -> view.showServerError()
    }

    override fun iconBackClicked() = view.goBack()

    override fun touchRootLayout() {
        view.clearFocusFromInputFields()
        view.hideKeyboard()
        view.hideErrorLayout()
    }

    override fun clickedDoneOnRetypePassword() {
        view.requestFocusOnRootLayout()
        view.hideKeyboard()
    }

    override fun unSubscribe() = compositeDisposable.clear()
}