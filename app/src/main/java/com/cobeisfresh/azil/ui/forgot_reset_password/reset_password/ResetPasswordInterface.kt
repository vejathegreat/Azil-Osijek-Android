package com.cobeisfresh.azil.ui.forgot_reset_password.reset_password

import com.cobeisfresh.azil.presentation.base.BasePresenter

/**
 * Created by Zerina Salitrezic on 18/08/2017.
 */

interface ResetPasswordInterface {

    interface View {

        fun showProgressBar()

        fun hideProgressBar()

        fun goToLoginActivity()

        fun setPasswordsNotMatchError()

        fun setCodeError()

        fun setNewPasswordError()

        fun setRetypedNewPasswordError()

        fun showNoInternetError()

        fun showServerError()

        fun showCodeExpiredError()

        fun showCodeWrongError()

        fun showPasswordInvalidError()

        fun enableReset()

        fun disableReset()

        fun removeCodeError()

        fun removeNewPasswordError()

        fun removeRetypedNewPasswordError()

        fun setCodeGreenLine()

        fun setNewPasswordGreenLine()

        fun setRetypedNewPasswordGreenLine()

        fun setCodeRedLine()

        fun setNewPasswordRedLine()

        fun setRetypedNewPasswordRedLine()

        fun setCodeBlueLine()

        fun setNewPasswordBlueLine()

        fun setRetypedNewPasswordBlueLine()

        fun goBack()

        fun clearFocusFromInputFields()

        fun hideKeyboard()

        fun hideErrorLayout()

        fun requestFocusOnRootLayout()
    }

    interface Presenter : BasePresenter<View> {

        fun setEmail(email: String)

        fun codeChanged(code: String)

        fun newPasswordChanged(newPassword: String)

        fun retypedNewPasswordChanged(retypeNewPassword: String)

        fun resetPasswordClicked(hasInternet: Boolean)

        fun iconBackClicked()

        fun onCodeChangeFocus(hasFocus: Boolean)

        fun onNewPasswordChangeFocus(hasFocus: Boolean)

        fun onRetypedNewPasswordChangeFocus(hasFocus: Boolean)

        fun touchRootLayout()

        fun clickedDoneOnRetypedPassword()

        fun unSubscribe()
    }
}