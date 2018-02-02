package com.cobeisfresh.azil.ui.welcome

import com.cobeisfresh.azil.presentation.base.BasePresenter

/**
 * Created by Zerina Salitrezic on 04/08/2017.
 */

interface WelcomeInterface {

    interface View {

        fun openRegisterActivity()

        fun openLoginActivity()

        fun startFacebookLogin()

        fun showNoInternetError()

        fun showProgressBar()

        fun hideProgressBar()

        fun showServerError()

        fun goBackWithResult()

        fun goBack()

        fun hideErrorLayout()
    }

    interface Presenter : BasePresenter<View> {

        fun registerClicked()

        fun loginClicked()

        fun facebookLoginClicked(hasInternet: Boolean)

        fun handleFacebookLogin(hasInternet: Boolean, fbToken: String)

        fun errorLayoutClicked()

        fun skipClicked()

        fun unSubscribe()
    }
}