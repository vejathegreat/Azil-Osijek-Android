package com.cobeisfresh.azil.presentation

import com.cobeisfresh.azil.*
import com.cobeisfresh.azil.network_interaction.UserInteractor
import com.cobeisfresh.azil.ui.forgot_reset_password.forgot_password.ForgotPasswordInterface
import com.cobeisfresh.azil.utils.TestDisposable
import com.nhaarman.mockito_kotlin.*
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

/**
 * Created by Zerina Salitrezic on 27/10/2017.
 */

class ForgotPasswordPresenterTest {

    private lateinit var presenter: ForgotPasswordPresenter

    private val view: ForgotPasswordInterface.View = mock()
    private val userInteractor: UserInteractor = mock()

    @Before
    fun setUp() {
        presenter = ForgotPasswordPresenter(userInteractor)
        presenter.setView(view)
    }

    @Test
    fun emailChangedShouldSetEmail() {
        presenter.emailChanged(EMAIL)

        assertEquals(presenter.email, EMAIL)
    }

    @Test
    fun emailChangedShouldRemoveEmailErrorEnableSend() {
        presenter.emailChanged(EMAIL)

        verify(view).removeEmailError()
        verify(view).enableSendButton()
        verifyNoMoreInteractions(view, userInteractor)
    }

    @Test
    fun checkIsFormValidShouldEnableSend() {
        presenter.email = EMAIL
        presenter.checkIsFormValid()

        verify(view).enableSendButton()
        verifyNoMoreInteractions(view, userInteractor)
    }

    @Test
    fun checkIsFormValidShouldDisableSend() {
        presenter.checkIsFormValid()

        verify(view).disableSendButton()
        verifyNoMoreInteractions(view, userInteractor)
    }

    @Test
    fun onEmailChangeFocusNoFocusValidEmail() {
        presenter.email = EMAIL
        presenter.onEmailChangeFocus(false)

        verify(view).setEmailGreenLine()
        verify(view).removeEmailError()
        verifyNoMoreInteractions(view, userInteractor)
    }

    @Test
    fun onEmailChangeFocusNoFocusInvalidEmail() {
        presenter.onEmailChangeFocus(false)

        verify(view).setEmailRedLine()
        verify(view).setEmailError()
        verifyNoMoreInteractions(view, userInteractor)
    }

    @Test
    fun onEmailChangeFocusHasFocus() {
        presenter.onEmailChangeFocus(true)

        verify(view).setEmailBlueLine()
        verifyNoMoreInteractions(view, userInteractor)
    }

    @Test
    fun sendButtonClickedNoInternet() {
        presenter.sendButtonClicked(false)

        verify(view).showNoInternetError()
        verifyNoMoreInteractions(view, userInteractor)
    }

    @Test
    fun sendButtonClickedShouldSendForgotPasswordRequest() {
        presenter.sendButtonClicked(true)

        verify(view).disableSendButton()
        verify(view).showProgressBar()
        verify(userInteractor).forgotPassword(any(), any())
        verifyNoMoreInteractions(view, userInteractor)
    }

    @Test
    fun getForgotPasswordObserverOnSubscribe() {
        presenter.getForgotPasswordObserver().onSubscribe(TestDisposable())

        verifyZeroInteractions(view, userInteractor)
    }

    @Test
    fun getForgotPasswordObserverOnSuccess() {
        presenter.getForgotPasswordObserver().onSuccess(mockBaseResponse())

        verify(view).hideProgressBar()
        verify(view).enableSendButton()
        verify(view).goToResetPasswordActivity(any())
        verifyNoMoreInteractions(view, userInteractor)
    }

    @Test
    fun getForgotPasswordObserverOnErrorServerError() {
        presenter.getForgotPasswordObserver().onError(mockHttpResponse(ERROR_CODE_BAD_REQUEST))

        verify(view).hideProgressBar()
        verify(view).enableSendButton()
        verify(view).showServerError()
        verifyNoMoreInteractions(view, userInteractor)
    }

    @Test
    fun getForgotPasswordObserverOnErrorUserNotRegistered() {
        presenter.getForgotPasswordObserver().onError(mockHttpResponse(ERROR_CODE_USER_NOT_REGISTERED))

        verify(view).hideProgressBar()
        verify(view).enableSendButton()
        verify(view).showUnknownUserError()
        verifyNoMoreInteractions(view, userInteractor)
    }

    @Test
    fun iconBackClickedShouldGoBack() {
        presenter.iconBackClicked()

        verify(view).goBack()
        verifyNoMoreInteractions(view, userInteractor)
    }

    @Test
    fun touchRootLayoutShouldClearFocusFromEmail() {
        presenter.touchRootLayout()

        verify(view).clearFocusFromEmail()
        verify(view).hideKeyboard()
        verify(view).hideErrorLayout()
        verifyNoMoreInteractions(view, userInteractor)
    }

    @Test
    fun clickedDoneOnPasswordShouldRequestFocusRootLayout() {
        presenter.clickedDoneOnEmail()

        verify(view).requestFocusOnRootLayout()
        verify(view).hideKeyboard()
        verifyNoMoreInteractions(view, userInteractor)
    }

    @Test
    fun unSubscribe() {
        presenter.unSubscribe()

        verifyZeroInteractions(view, userInteractor)
    }
}