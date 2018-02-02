package com.cobeisfresh.azil.presentation

import com.cobeisfresh.azil.ERROR_CODE_BAD_REQUEST
import com.cobeisfresh.azil.TOKEN
import com.cobeisfresh.azil.mockHttpResponse
import com.cobeisfresh.azil.mockLoginResponse
import com.cobeisfresh.azil.network_interaction.UserInteractor
import com.cobeisfresh.azil.shared_prefs.SharedPrefs
import com.cobeisfresh.azil.ui.welcome.WelcomeInterface
import com.cobeisfresh.azil.utils.TestDisposable
import com.nhaarman.mockito_kotlin.*
import org.junit.Before
import org.junit.Test

/**
 * Created by Zerina Salitrezic on 18/10/2017.
 */

class WelcomePresenterTest {

    private lateinit var presenter: WelcomePresenter

    private val view: WelcomeInterface.View = mock()
    private val userInteractor: UserInteractor = mock()
    private val sharedPrefs: SharedPrefs = mock()

    @Before
    fun setUp() {
        presenter = WelcomePresenter(userInteractor, sharedPrefs)
        presenter.setView(view)
    }

    @Test
    fun registerClickedShouldOpenRegisterActivity() {
        presenter.registerClicked()

        verify(view).openRegisterActivity()
        verifyNoMoreInteractions(view, userInteractor, sharedPrefs)
    }

    @Test
    fun loginClickedShouldOpenLoginActivity() {
        presenter.loginClicked()

        verify(view).openLoginActivity()
        verifyNoMoreInteractions(view, userInteractor, sharedPrefs)
    }

    @Test
    fun facebookLoginClickedNoInternet() {
        presenter.facebookLoginClicked(false)

        verify(view).showNoInternetError()
        verifyNoMoreInteractions(view, userInteractor, sharedPrefs)
    }

    @Test
    fun facebookLoginClickedShouldStartFbLogin() {
        presenter.facebookLoginClicked(true)

        verify(view).startFacebookLogin()
        verifyNoMoreInteractions(view, userInteractor, sharedPrefs)
    }

    @Test
    fun handleFacebookLoginNoInternet() {
        presenter.handleFacebookLogin(false, TOKEN)

        verify(view).showNoInternetError()
        verifyNoMoreInteractions(view, userInteractor, sharedPrefs)
    }

    @Test
    fun handleFacebookLoginShouldSendLoginUserWithFacebookRequest() {
        presenter.handleFacebookLogin(true, TOKEN)

        verify(view).showProgressBar()
        verify(userInteractor).loginUserWithFacebook(any(), any())
        verifyNoMoreInteractions(view, userInteractor, sharedPrefs)
    }

    @Test
    fun getFbLoginObserverOnSubscribe() {
        presenter.getFbLoginObserver().onSubscribe(TestDisposable())

        verifyZeroInteractions(view, userInteractor, sharedPrefs)
    }

    @Test
    fun getFbLoginObserverOnSuccess() {
        presenter.getFbLoginObserver().onSuccess(mockLoginResponse())

        verify(view).hideProgressBar()
        verify(sharedPrefs).setLoggedIn(any())
        verify(sharedPrefs).setToken(any())
        verify(view).goBackWithResult()
        verifyNoMoreInteractions(view, userInteractor, sharedPrefs)
    }

    @Test
    fun getFbLoginObserverOnError() {
        presenter.getFbLoginObserver().onError(mockHttpResponse(ERROR_CODE_BAD_REQUEST))

        verify(view).hideProgressBar()
        verify(view).showServerError()
        verifyNoMoreInteractions(view, userInteractor, sharedPrefs)
    }

    @Test
    fun skipClickedShouldGoBack() {
        presenter.skipClicked()

        verify(view).goBack()
        verifyNoMoreInteractions(view, userInteractor, sharedPrefs)
    }

    @Test
    fun errorLayoutClickedShouldHideErrorLayout() {
        presenter.errorLayoutClicked()

        verify(view).hideErrorLayout()
        verifyNoMoreInteractions(view, userInteractor, sharedPrefs)
    }

    @Test
    fun unSubscribe() {
        presenter.unSubscribe()

        verifyZeroInteractions(view, userInteractor, sharedPrefs)
    }
}