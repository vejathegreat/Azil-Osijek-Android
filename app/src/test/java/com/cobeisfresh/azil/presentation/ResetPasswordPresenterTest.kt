package com.cobeisfresh.azil.presentation

import com.cobeisfresh.azil.*
import com.cobeisfresh.azil.network_interaction.UserInteractor
import com.cobeisfresh.azil.ui.forgot_reset_password.reset_password.ResetPasswordInterface
import com.cobeisfresh.azil.utils.TestDisposable
import com.nhaarman.mockito_kotlin.*
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

/**
 * Created by Zerina Salitrezic on 30/10/2017.
 */

class ResetPasswordPresenterTest {

    private lateinit var presenter: ResetPasswordPresenter

    private val view: ResetPasswordInterface.View = mock()
    private val userInteractor: UserInteractor = mock()

    @Before
    fun setUp() {
        presenter = ResetPasswordPresenter(userInteractor)
        presenter.setView(view)
    }

    @Test
    fun setEmailShouldSetEmail() {
        presenter.setEmail(EMAIL)

        assertEquals(presenter.resetPasswordData.email, EMAIL)
    }

    @Test
    fun codeChangedShouldSetCode() {
        presenter.codeChanged(CODE)

        assertEquals(presenter.resetPasswordData.code, CODE)
    }

    @Test
    fun newPasswordChangedShouldSetPassword() {
        presenter.newPasswordChanged(PASSWORD)

        assertEquals(presenter.resetPasswordData.password, PASSWORD)
    }

    @Test
    fun retypedNewPasswordChangedShouldSetRetypedPassword() {
        presenter.retypedNewPasswordChanged(PASSWORD)

        assertEquals(presenter.resetPasswordData.retypedPassword, PASSWORD)
    }

    @Test
    fun codeChangedShouldRemoveCodeErrorDisableReset() {
        presenter.codeChanged(CODE)

        verify(view).removeCodeError()
        verify(view).disableReset()
        verifyNoMoreInteractions(view, userInteractor)
    }

    @Test
    fun newPasswordChangedShouldRemoveNewPasswordErrorDisableReset() {
        presenter.newPasswordChanged(PASSWORD)

        verify(view).removeNewPasswordError()
        verify(view).disableReset()
        verifyNoMoreInteractions(view, userInteractor)
    }

    @Test
    fun retypedNewPasswordChangedShouldRemoveRetypedPasswordErrorDisableReset() {
        presenter.retypedNewPasswordChanged(PASSWORD)

        verify(view).removeRetypedNewPasswordError()
        verify(view).disableReset()
        verifyNoMoreInteractions(view, userInteractor)
    }

    @Test
    fun checkIsFormValidShouldEnableReset() {
        presenter.resetPasswordData = mockResetPasswordData()
        presenter.checkIsFormValid()

        verify(view).enableReset()
        verifyNoMoreInteractions(view, userInteractor)
    }

    @Test
    fun checkIsFormValidShouldDisableReset() {
        presenter.checkIsFormValid()

        verify(view).disableReset()
        verifyNoMoreInteractions(view, userInteractor)
    }

    @Test
    fun onCodeChangeFocusHasFocus() {
        presenter.onCodeChangeFocus(true)

        verify(view).setCodeBlueLine()
        verifyNoMoreInteractions(view, userInteractor)
    }

    @Test
    fun onCodeChangeFocusNoFocusValidCode() {
        presenter.resetPasswordData.code = CODE
        presenter.onCodeChangeFocus(false)

        verify(view).setCodeGreenLine()
        verify(view).removeCodeError()
        verifyNoMoreInteractions(view, userInteractor)
    }

    @Test
    fun onCodeChangeFocusNoFocusInvalidCode() {
        presenter.onCodeChangeFocus(false)

        verify(view).setCodeRedLine()
        verify(view).setCodeError()
        verifyNoMoreInteractions(view, userInteractor)
    }

    @Test
    fun onNewPasswordChangeFocusHasFocus() {
        presenter.onNewPasswordChangeFocus(true)

        verify(view).setNewPasswordBlueLine()
        verifyNoMoreInteractions(view, userInteractor)
    }

    @Test
    fun onNewPasswordChangeFocusNoFocusValidPasswords() {
        presenter.resetPasswordData.password = PASSWORD
        presenter.resetPasswordData.retypedPassword = PASSWORD
        presenter.onNewPasswordChangeFocus(false)

        verify(view).setNewPasswordGreenLine()
        verify(view).removeNewPasswordError()
        verify(view).setRetypedNewPasswordGreenLine()
        verify(view).removeRetypedNewPasswordError()
        verifyNoMoreInteractions(view, userInteractor)
    }

    @Test
    fun onNewPasswordChangeFocusNoFocusInvalidPasswordNotSamePasswords() {
        presenter.resetPasswordData.retypedPassword = PASSWORD
        presenter.onNewPasswordChangeFocus(false)

        verify(view).setNewPasswordRedLine()
        verify(view).setNewPasswordError()
        verify(view).setRetypedNewPasswordRedLine()
        verify(view).setPasswordsNotMatchError()
        verifyNoMoreInteractions(view, userInteractor)
    }

    @Test
    fun onRetypedNewPasswordChangeFocusHasFocus() {
        presenter.onRetypedNewPasswordChangeFocus(true)

        verify(view).setRetypedNewPasswordBlueLine()
        verifyNoMoreInteractions(view, userInteractor)
    }

    @Test
    fun onRetypedNewPasswordChangeFocusNoFocusSamePasswords() {
        presenter.resetPasswordData.password = PASSWORD
        presenter.resetPasswordData.retypedPassword = PASSWORD
        presenter.onRetypedNewPasswordChangeFocus(false)

        verify(view).setRetypedNewPasswordGreenLine()
        verify(view).removeRetypedNewPasswordError()
        verifyNoMoreInteractions(view, userInteractor)
    }

    @Test
    fun onRetypedNewPasswordChangeFocusNoFocusValidPasswordNotSamePasswords() {
        presenter.resetPasswordData.retypedPassword = PASSWORD
        presenter.onRetypedNewPasswordChangeFocus(false)

        verify(view).setRetypedNewPasswordRedLine()
        verify(view).setPasswordsNotMatchError()
        verifyNoMoreInteractions(view, userInteractor)
    }

    @Test
    fun resetPasswordClickedNoInternet() {
        presenter.resetPasswordClicked(false)

        verify(view).showNoInternetError()
        verifyNoMoreInteractions(view, userInteractor)
    }

    @Test
    fun resetPasswordClickedShouldSendResetPasswordRequest() {
        presenter.resetPasswordClicked(true)

        verify(view).showProgressBar()
        verify(view).disableReset()
        verify(userInteractor).resetPassword(any(), any())
        verifyNoMoreInteractions(view, userInteractor)
    }

    @Test
    fun getResetPasswordObserverOnSubscribe() {
        presenter.getResetPasswordObserver().onSubscribe(TestDisposable())

        verifyZeroInteractions(view, userInteractor)
    }

    @Test
    fun getResetPasswordObserverOnSuccess() {
        presenter.getResetPasswordObserver().onSuccess(mockBaseResponse())

        verify(view).hideProgressBar()
        verify(view).enableReset()
        verify(view).goToLoginActivity()
        verifyNoMoreInteractions(view, userInteractor)
    }

    @Test
    fun getResetPasswordObserverOnErrorServerError() {
        presenter.getResetPasswordObserver().onError(mockHttpResponse(ERROR_CODE_BAD_REQUEST))

        verify(view).hideProgressBar()
        verify(view).enableReset()
        verify(view).showServerError()
        verifyNoMoreInteractions(view, userInteractor)
    }

    @Test
    fun getResetPasswordObserverOnErrorInvalidPassword() {
        presenter.getResetPasswordObserver().onError(mockHttpResponse(ERROR_CODE_FORGOT_INVALID_PASSWORD))

        verify(view).hideProgressBar()
        verify(view).enableReset()
        verify(view).setNewPasswordError()
        verifyNoMoreInteractions(view, userInteractor)
    }

    @Test
    fun getResetPasswordObserverOnErrorWrongRecoveryCode() {
        presenter.getResetPasswordObserver().onError(mockHttpResponse(ERROR_CODE_WRONG_RECOVERY_CODE))

        verify(view).hideProgressBar()
        verify(view).enableReset()
        verify(view).showCodeWrongError()
        verifyNoMoreInteractions(view, userInteractor)
    }

    @Test
    fun getResetPasswordObserverOnErrorExpiredRecoveryCode() {
        presenter.getResetPasswordObserver().onError(mockHttpResponse(ERROR_CODE_EXPIRED_RECOVERY_CODE))

        verify(view).hideProgressBar()
        verify(view).enableReset()
        verify(view).showCodeExpiredError()
        verifyNoMoreInteractions(view, userInteractor)
    }

    @Test
    fun iconBackClickedShouldGoBack() {
        presenter.iconBackClicked()

        verify(view).goBack()
        verifyNoMoreInteractions(view, userInteractor)
    }

    @Test
    fun touchRootLayoutShouldClearInputFieldsFocusHideKeyboard() {
        presenter.touchRootLayout()

        verify(view).clearFocusFromInputFields()
        verify(view).hideKeyboard()
        verify(view).hideErrorLayout()
        verifyNoMoreInteractions(view, userInteractor)
    }

    @Test
    fun clickedDoneOnPasswordShouldRequestFocusRootLayout() {
        presenter.clickedDoneOnRetypedPassword()

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