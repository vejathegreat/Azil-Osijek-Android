package com.cobeisfresh.azil.presentation

import com.cobeisfresh.azil.*
import com.cobeisfresh.azil.network_interaction.UserInteractor
import com.cobeisfresh.azil.shared_prefs.SharedPrefs
import com.cobeisfresh.azil.ui.login.LoginInterface
import com.cobeisfresh.azil.utils.TestDisposable
import com.nhaarman.mockito_kotlin.*
import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Test

/**
 * Created by Zerina Salitrezic on 19/10/2017.
 */

class LoginPresenterTest {

    private lateinit var presenter: LoginPresenter

    private val view: LoginInterface.View = mock()
    private val userInteractor: UserInteractor = mock()
    private val sharedPrefs: SharedPrefs = mock()

    @Before
    fun setUp() {
        presenter = LoginPresenter(userInteractor, sharedPrefs)
        presenter.setView(view)
    }

    @Test
    fun emailChangedShouldSetEmail() {
        presenter.emailChanged(EMAIL)

        assertEquals(presenter.email, EMAIL)
    }

    @Test
    fun passwordChangedShouldSetPassword() {
        presenter.passwordChanged(PASSWORD)

        assertEquals(presenter.password, PASSWORD)
    }

    @Test
    fun emailChangedShouldRemoveEmailErrorDisableLogin() {
        presenter.emailChanged(EMAIL)

        verify(view).removeEmailError()
        verify(view).disableLogin()
        verifyNoMoreInteractions(view, userInteractor, sharedPrefs)
    }

    @Test
    fun passwordChangedShouldRemovePasswordErrorDisableLogin() {
        presenter.passwordChanged(PASSWORD)

        verify(view).removePasswordError()
        verify(view).disableLogin()
        verifyNoMoreInteractions(view, userInteractor, sharedPrefs)
    }

    @Test
    fun onEmailChangeFocusNoFocusValidEmail() {
        presenter.email = EMAIL
        presenter.onEmailChangeFocus(false)

        verify(view).setEmailGreenLine()
        verify(view).removeEmailError()
        verifyNoMoreInteractions(view, userInteractor, sharedPrefs)
    }

    @Test
    fun onEmailChangeFocusNoFocusInvalidEmail() {
        presenter.onEmailChangeFocus(false)

        verify(view).setEmailRedLine()
        verify(view).setEmailError()
        verifyNoMoreInteractions(view, userInteractor, sharedPrefs)
    }

    @Test
    fun onEmailChangeFocusHasFocus() {
        presenter.onEmailChangeFocus(true)

        verify(view).setEmailBlueLine()
        verifyNoMoreInteractions(view, userInteractor, sharedPrefs)
    }

    @Test
    fun onPasswordChangeFocusNoFocusValidPassword() {
        presenter.password = PASSWORD
        presenter.onPasswordChangeFocus(false)

        verify(view).setPasswordGreenLine()
        verify(view).removePasswordError()
        verifyNoMoreInteractions(view, userInteractor, sharedPrefs)
    }

    @Test
    fun onPasswordChangeFocusNoFocusInvalidPassword() {
        presenter.onPasswordChangeFocus(false)

        verify(view).setPasswordRedLine()
        verify(view).setPasswordError()
        verifyNoMoreInteractions(view, userInteractor, sharedPrefs)
    }

    @Test
    fun onPasswordChangeFocusHasFocus() {
        presenter.onPasswordChangeFocus(true)

        verify(view).setPasswordBlueLine()
        verifyNoMoreInteractions(view, userInteractor, sharedPrefs)
    }

    @Test
    fun checkIsFormValidWhenEmailPasswordValid() {
        presenter.email = EMAIL
        presenter.password = PASSWORD
        presenter.checkIsFormValid()

        verify(view).enableLogin()
        verifyNoMoreInteractions(view, userInteractor, sharedPrefs)
    }

    @Test
    fun checkIsFormValidWhenValidEmailInvalidPassword() {
        presenter.email = EMAIL
        presenter.checkIsFormValid()

        verify(view).disableLogin()
        verifyNoMoreInteractions(view, userInteractor, sharedPrefs)
    }

    @Test
    fun checkIsFormValidWhenValidPasswordInvalidEmail() {
        presenter.password = PASSWORD
        presenter.checkIsFormValid()

        verify(view).disableLogin()
        verifyNoMoreInteractions(view, userInteractor, sharedPrefs)
    }

    @Test
    fun loginClickedNoInternet() {
        presenter.loginClicked(false)

        verify(view).showNoInternetError()
        verifyNoMoreInteractions(view, userInteractor, sharedPrefs)
    }

    @Test
    fun loginClickedShouldSendLoginUserRequest() {
        presenter.loginClicked(true)

        verify(view).showProgressBar()
        verify(view).disableLogin()
        verify(userInteractor).loginUser(any(), any(), any())
        verifyNoMoreInteractions(view, userInteractor, sharedPrefs)
    }

    @Test
    fun getLoginObserverOnSubscribe() {
        presenter.getLoginObserver().onSubscribe(TestDisposable())

        verifyZeroInteractions(view, userInteractor, sharedPrefs)
    }

    @Test
    fun getLoginObserverOnSuccess() {
        presenter.getLoginObserver().onSuccess(mockLoginResponse())

        verify(view).hideProgressBar()
        verify(sharedPrefs).setLoggedIn(any())
        verify(sharedPrefs).setToken(any())
        verify(view).goBackWithResult()
        verifyNoMoreInteractions(view, userInteractor, sharedPrefs)
    }

    @Test
    fun getLoginObserverOnErrorServerError() {
        presenter.getLoginObserver().onError(mockHttpResponse(ERROR_CODE_BAD_REQUEST))

        verify(view).enableLogin()
        verify(view).hideProgressBar()
        verify(view).showServerError()
        verifyNoMoreInteractions(view, userInteractor, sharedPrefs)
    }

    @Test
    fun getLoginObserverOnErrorInvalidPasswordError() {
        presenter.getLoginObserver().onError(mockHttpResponse(ERROR_CODE_INVALID_PASSWORD))

        verify(view).enableLogin()
        verify(view).hideProgressBar()
        verify(view).showInvalidPasswordError()
        verifyNoMoreInteractions(view, userInteractor, sharedPrefs)
    }

    @Test
    fun getLoginObserverOnErrorNotRegisteredError() {
        presenter.getLoginObserver().onError(mockHttpResponse(ERROR_CODE_NOT_REGISTERED))

        verify(view).enableLogin()
        verify(view).hideProgressBar()
        verify(view).showNotRegisteredUserError()
        verifyNoMoreInteractions(view, userInteractor, sharedPrefs)
    }

    @Test
    fun forgotPasswordClickedShouldOpenForgotPasswordActivity() {
        presenter.forgotPasswordClicked()

        verify(view).openForgotPasswordActivity()
        verifyNoMoreInteractions(view, userInteractor, sharedPrefs)
    }

    @Test
    fun iconBackClickedShouldGoBack() {
        presenter.iconBackClicked()

        verify(view).goBack()
        verifyNoMoreInteractions(view, userInteractor, sharedPrefs)
    }

    @Test
    fun registerClickedShouldOpenRegisterActivity() {
        presenter.registerClicked()

        verify(view).openRegisterActivity()
        verifyNoMoreInteractions(view, userInteractor, sharedPrefs)
    }

    @Test
    fun touchRootLayoutShouldClearInputFieldsFocusHideKeyboard() {
        presenter.touchRootLayout()

        verify(view).clearFocusFromInputFields()
        verify(view).hideKeyboard()
        verify(view).hideErrorLayout()
        verifyNoMoreInteractions(view, userInteractor, sharedPrefs)
    }

    @Test
    fun clickedDoneOnPasswordShouldRequestRootLayoutFocusHideKeyboard() {
        presenter.clickedDoneOnPassword()

        verify(view).requestFocusOnRootLayout()
        verify(view).hideKeyboard()
        verifyNoMoreInteractions(view, userInteractor, sharedPrefs)
    }

    @Test
    fun unSubscribe() {
        presenter.unSubscribe()

        verifyZeroInteractions(view, userInteractor, sharedPrefs)
    }
}