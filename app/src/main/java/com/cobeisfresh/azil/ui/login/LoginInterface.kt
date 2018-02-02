package com.cobeisfresh.azil.ui.login

import com.cobeisfresh.azil.presentation.base.BasePresenter

/**
 * Created by Zerina Salitrezic on 04/08/2017.
 */

interface LoginInterface {

    interface View {

        fun showProgressBar()

        fun hideProgressBar()

        fun showNoInternetError()

        fun showServerError()

        fun showInvalidPasswordError()

        fun showNotRegisteredUserError()

        fun openMainActivity()

        fun openForgotPasswordActivity()

        fun goBack()

        fun setEmailGreenLine()

        fun setPasswordGreenLine()

        fun setEmailRedLine()

        fun setPasswordRedLine()

        fun setEmailBlueLine()

        fun setPasswordBlueLine()

        fun setEmailError()

        fun setPasswordError()

        fun enableLogin()

        fun disableLogin()

        fun openRegisterActivity()

        fun removeEmailError()

        fun removePasswordError()

        fun goBackWithResult()

        fun clearFocusFromInputFields()

        fun hideKeyboard()

        fun hideErrorLayout()

        fun requestFocusOnRootLayout()
    }

    interface Presenter : BasePresenter<View> {

        fun onEmailChangeFocus(hasFocus: Boolean)

        fun onPasswordChangeFocus(hasFocus: Boolean)

        fun emailChanged(email: String)

        fun passwordChanged(password: String)

        fun loginClicked(hasInternet: Boolean)

        fun forgotPasswordClicked()

        fun registerClicked()

        fun iconBackClicked()

        fun touchRootLayout()

        fun clickedDoneOnPassword()

        fun unSubscribe()
    }
}