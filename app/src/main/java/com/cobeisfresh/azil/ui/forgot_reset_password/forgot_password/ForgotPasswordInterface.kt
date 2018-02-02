package com.cobeisfresh.azil.ui.forgot_reset_password.forgot_password

import com.cobeisfresh.azil.presentation.base.BasePresenter

/**
 * Created by Zerina Salitrezic on 07/08/2017.
 */

interface ForgotPasswordInterface {

    interface View {

        fun showProgressBar()

        fun hideProgressBar()

        fun goToResetPasswordActivity(email: String)

        fun showUnknownUserError()

        fun showNoInternetError()

        fun showServerError()

        fun goBack()

        fun disableSendButton()

        fun enableSendButton()

        fun setEmailError()

        fun removeEmailError()

        fun setEmailBlueLine()

        fun setEmailGreenLine()

        fun setEmailRedLine()

        fun clearFocusFromEmail()

        fun hideKeyboard()

        fun hideErrorLayout()

        fun requestFocusOnRootLayout()
    }

    interface Presenter : BasePresenter<View> {

        fun sendButtonClicked(hasInternet: Boolean)

        fun iconBackClicked()

        fun emailChanged(email: String)

        fun onEmailChangeFocus(hasFocus: Boolean)

        fun touchRootLayout()

        fun clickedDoneOnEmail()

        fun unSubscribe()
    }
}