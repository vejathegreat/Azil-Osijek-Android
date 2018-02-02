package com.cobeisfresh.azil.presentation

import com.cobeisfresh.azil.*
import com.cobeisfresh.azil.network_interaction.UserInteractor
import com.cobeisfresh.azil.ui.edit_password.EditPasswordInterface
import com.cobeisfresh.azil.utils.TestDisposable
import com.nhaarman.mockito_kotlin.*
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

/**
 * Created by Zerina Salitrezic on 26/10/2017.
 */

class EditPasswordPresenterTest {

    private lateinit var presenter: EditPasswordPresenter

    private val view: EditPasswordInterface.View = mock()
    private val userInteractor: UserInteractor = mock()

    @Before
    fun setUp() {
        presenter = EditPasswordPresenter(userInteractor)
        presenter.setView(view)
    }

    @Test
    fun oldPasswordChanged() {
        presenter.oldPasswordChanged(PASSWORD)

        assertEquals(presenter.changePasswordData.oldPassword, PASSWORD)
    }

    @Test
    fun newPasswordChanged() {
        presenter.newPasswordChanged(PASSWORD)

        assertEquals(presenter.changePasswordData.newPassword, PASSWORD)
    }

    @Test
    fun retypedNewPasswordChanged() {
        presenter.retypedNewPasswordChanged(PASSWORD)

        assertEquals(presenter.changePasswordData.retypedNewPassword, PASSWORD)
    }

    @Test
    fun oldPasswordChangedShouldRemoveOldPasswordErrorDisableChange() {
        presenter.oldPasswordChanged(PASSWORD)

        verify(view).removeOldPasswordError()
        verify(view).disableChange()
        verifyNoMoreInteractions(view, userInteractor)
    }

    @Test
    fun newPasswordChangedShouldRemoveNewPasswordErrorDisableChange() {
        presenter.newPasswordChanged(PASSWORD)

        verify(view).removeNewPasswordError()
        verify(view).disableChange()
        verifyNoMoreInteractions(view, userInteractor)
    }

    @Test
    fun retypedNewPasswordChangedShouldRemoveRetypedPasswordDisableChange() {
        presenter.retypedNewPasswordChanged(PASSWORD)

        verify(view).removeRetypedPasswordError()
        verify(view).disableChange()
        verifyNoMoreInteractions(view, userInteractor)
    }

    @Test
    fun checkIsFormValidShouldEnableChange() {
        presenter.changePasswordData = mockChangePasswordData()
        presenter.checkIsFormValid()

        verify(view).enableChange()
        verifyNoMoreInteractions(view, userInteractor)
    }

    @Test
    fun checkIsFormValidShouldDisableChange() {
        presenter.checkIsFormValid()

        verify(view).disableChange()
        verifyNoMoreInteractions(view, userInteractor)
    }

    @Test
    fun onOldPasswordChangeFocusNoFocusValidPassword() {
        presenter.changePasswordData.oldPassword = PASSWORD
        presenter.onOldPasswordChangeFocus(false)

        verify(view).setOldPasswordGreenLine()
        verify(view).removeOldPasswordError()
        verifyNoMoreInteractions(view, userInteractor)
    }

    @Test
    fun onOldPasswordChangeFocusNoFocusInvalidPassword() {
        presenter.onOldPasswordChangeFocus(false)

        verify(view).setOldPasswordRedLine()
        verify(view).setOldPasswordError()
        verifyNoMoreInteractions(view, userInteractor)
    }

    @Test
    fun onOldPasswordChangeFocusHasFocus() {
        presenter.onOldPasswordChangeFocus(true)

        verify(view).setOldPasswordBlueLine()
        verifyNoMoreInteractions(view, userInteractor)
    }

    @Test
    fun onNewPasswordChangeFocusNoFocusValidPasswords() {
        presenter.changePasswordData.newPassword = PASSWORD
        presenter.changePasswordData.retypedNewPassword = PASSWORD
        presenter.onNewPasswordChangeFocus(false)

        verify(view).setNewPasswordGreenLine()
        verify(view).removeNewPasswordError()
        verify(view).setRetypedPasswordGreenLine()
        verify(view).removeRetypedPasswordError()
        verifyNoMoreInteractions(view, userInteractor)
    }

    @Test
    fun onNewPasswordChangeFocusNoFocusInvalidPasswords() {
        presenter.changePasswordData.retypedNewPassword = PASSWORD
        presenter.onNewPasswordChangeFocus(false)

        verify(view).setNewPasswordRedLine()
        verify(view).setNewPasswordError()
        verify(view).setRetypedPasswordRedLine()
        verify(view).setPasswordsNotSameError()
        verifyNoMoreInteractions(view, userInteractor)
    }

    @Test
    fun onNewPasswordChangeFocusHasFocus() {
        presenter.onNewPasswordChangeFocus(true)

        verify(view).setNewPasswordBlueLine()
        verifyNoMoreInteractions(view, userInteractor)
    }

    @Test
    fun onRetypedPasswordChangeFocusNoFocusSamePasswords() {
        presenter.changePasswordData.newPassword = PASSWORD
        presenter.changePasswordData.retypedNewPassword = PASSWORD
        presenter.onRetypedNewPasswordChangeFocus(false)

        verify(view).setRetypedPasswordGreenLine()
        verify(view).removeRetypedPasswordError()
        verifyNoMoreInteractions(view, userInteractor)
    }

    @Test
    fun onRetypedPasswordChangeFocusNoFocusNotSamePasswords() {
        presenter.changePasswordData.retypedNewPassword = PASSWORD
        presenter.onRetypedNewPasswordChangeFocus(false)

        verify(view).setRetypedPasswordRedLine()
        verify(view).setPasswordsNotSameError()
        verifyNoMoreInteractions(view, userInteractor)
    }

    @Test
    fun onRetypedPasswordChangeFocusHasFocus() {
        presenter.onRetypedNewPasswordChangeFocus(true)

        verify(view).setRetypedPasswordBlueLine()
        verifyNoMoreInteractions(view, userInteractor)
    }

    @Test
    fun changePasswordClickedNoInternet() {
        presenter.changePasswordClicked(false)

        verify(view).showNoInternetError()
        verifyNoMoreInteractions(view, userInteractor)
    }

    @Test
    fun changePasswordClickedShouldSendChangePasswordRequest() {
        presenter.changePasswordClicked(true)

        verify(view).showProgressBar()
        verify(view).disableChange()
        verify(userInteractor).changePassword(any(), any())
        verifyNoMoreInteractions(view, userInteractor)
    }

    @Test
    fun getChangePasswordObserverOnSubscribe() {
        presenter.getChangePasswordObserver().onSubscribe(TestDisposable())

        verifyZeroInteractions(view, userInteractor)
    }

    @Test
    fun getChangePasswordObserverOnSuccess() {
        presenter.getChangePasswordObserver().onSuccess(mockBaseResponse())

        verify(view).hideProgressBar()
        verify(view).showPasswordChangedSuccess()
        verifyNoMoreInteractions(view, userInteractor)
    }

    @Test
    fun getChangePasswordObserverOnErrorOldPasswordInvalidError() {
        presenter.getChangePasswordObserver().onError(mockHttpResponse(ERROR_CODE_INVALID_OLD_PASSWORD))

        verify(view).hideProgressBar()
        verify(view).enableChange()
        verify(view).showPasswordInvalidError()
        verifyNoMoreInteractions(view, userInteractor)
    }

    @Test
    fun getChangePasswordObserverOnErrorPasswordsMismatchedError() {
        presenter.getChangePasswordObserver().onError(mockHttpResponse(ERROR_CODE_PASSWORDS_MISMATCHED))

        verify(view).hideProgressBar()
        verify(view).enableChange()
        verify(view).showPasswordsMismatchedError()
        verifyNoMoreInteractions(view, userInteractor)
    }

    @Test
    fun getChangePasswordObserverOnErrorServerError() {
        presenter.getChangePasswordObserver().onError(mockHttpResponse(ERROR_CODE_BAD_REQUEST))

        verify(view).hideProgressBar()
        verify(view).enableChange()
        verify(view).showServerError()
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
    fun clickedDoneOnRetypePasswordShouldRequestRootLayoutFocus() {
        presenter.clickedDoneOnRetypePassword()

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