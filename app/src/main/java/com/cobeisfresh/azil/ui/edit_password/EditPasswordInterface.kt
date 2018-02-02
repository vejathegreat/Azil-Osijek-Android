package com.cobeisfresh.azil.ui.edit_password

import com.cobeisfresh.azil.presentation.base.BasePresenter

/**
 * Created by Zerina Salitrezic on 18/08/2017.
 */

interface EditPasswordInterface {

    interface View {

        fun showProgressBar()

        fun hideProgressBar()

        fun showPasswordChangedSuccess()

        fun setPasswordsNotSameError()

        fun setOldPasswordError()

        fun setNewPasswordError()

        fun setRetypedPasswordError()

        fun removeOldPasswordError()

        fun removeNewPasswordError()

        fun removeRetypedPasswordError()

        fun setOldPasswordGreenLine()

        fun setNewPasswordGreenLine()

        fun setRetypedPasswordGreenLine()

        fun setOldPasswordBlueLine()

        fun setNewPasswordBlueLine()

        fun setRetypedPasswordBlueLine()

        fun setOldPasswordRedLine()

        fun setNewPasswordRedLine()

        fun setRetypedPasswordRedLine()

        fun showPasswordInvalidError()

        fun showPasswordsMismatchedError()

        fun showNoInternetError()

        fun showServerError()

        fun disableChange()

        fun enableChange()

        fun goBack()

        fun clearFocusFromInputFields()

        fun hideKeyboard()

        fun hideErrorLayout()

        fun requestFocusOnRootLayout()
    }

    interface Presenter : BasePresenter<View> {

        fun oldPasswordChanged(oldPassword: String)

        fun newPasswordChanged(newPassword: String)

        fun retypedNewPasswordChanged(retypeNewPassword: String)

        fun onOldPasswordChangeFocus(hasFocus: Boolean)

        fun onNewPasswordChangeFocus(hasFocus: Boolean)

        fun onRetypedNewPasswordChangeFocus(hasFocus: Boolean)

        fun changePasswordClicked(hasInternet: Boolean)

        fun iconBackClicked()

        fun touchRootLayout()

        fun clickedDoneOnRetypePassword()

        fun unSubscribe()
    }
}