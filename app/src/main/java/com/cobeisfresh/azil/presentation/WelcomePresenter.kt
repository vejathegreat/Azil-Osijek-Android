package com.cobeisfresh.azil.presentation

import com.cobeisfresh.azil.common.constants.ERROR_CODE_BAD_REQUEST
import com.cobeisfresh.azil.data.response.LoginResponse
import com.cobeisfresh.azil.network_interaction.UserInteractor
import com.cobeisfresh.azil.shared_prefs.SharedPrefs
import com.cobeisfresh.azil.ui.welcome.WelcomeInterface
import com.jakewharton.retrofit2.adapter.rxjava2.HttpException
import io.reactivex.SingleObserver
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

/**
 * Created by Zerina Salitrezic on 04/08/2017.
 */

class WelcomePresenter(private val userInteractor: UserInteractor,
                       private val sharedPrefs: SharedPrefs) : WelcomeInterface.Presenter {

    private lateinit var view: WelcomeInterface.View
    private val compositeDisposable = CompositeDisposable()

    override fun setView(view: WelcomeInterface.View) {
        this.view = view
    }

    override fun registerClicked() = view.openRegisterActivity()

    override fun loginClicked() = view.openLoginActivity()

    override fun facebookLoginClicked(hasInternet: Boolean) {
        if (hasInternet) {
            view.startFacebookLogin()
        } else {
            view.showNoInternetError()
        }
    }

    override fun handleFacebookLogin(hasInternet: Boolean, fbToken: String) {
        if (!hasInternet) {
            view.showNoInternetError()
        } else {
            view.showProgressBar()
            userInteractor.loginUserWithFacebook(fbToken, getFbLoginObserver())
        }
    }

    internal fun getFbLoginObserver(): SingleObserver<LoginResponse> {
        return object : SingleObserver<LoginResponse> {
            override fun onSubscribe(d: Disposable?) {
                compositeDisposable.add(d)
            }

            override fun onSuccess(response: LoginResponse) {
                view.hideProgressBar()
                handleLoginResponse(response)
            }

            override fun onError(e: Throwable?) {
                view.hideProgressBar()

                if (e is HttpException) {
                    handleErrorCode((e).code())
                }
            }
        }
    }

    internal fun handleLoginResponse(response: LoginResponse) {
        sharedPrefs.setLoggedIn(true)
        sharedPrefs.setToken(response.token)
        view.goBackWithResult()
    }

    private fun handleErrorCode(code: Int) {
        if (code == ERROR_CODE_BAD_REQUEST) {
            view.showServerError()
        }
    }

    override fun skipClicked() = view.goBack()

    override fun errorLayoutClicked() = view.hideErrorLayout()

    override fun unSubscribe() = compositeDisposable.clear()
}