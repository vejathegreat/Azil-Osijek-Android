package com.cobeisfresh.azil.presentation

import com.cobeisfresh.azil.*
import com.cobeisfresh.azil.network_interaction.UserInteractor
import com.cobeisfresh.azil.shared_prefs.SharedPrefs
import com.cobeisfresh.azil.ui.register.RegisterInterface
import com.cobeisfresh.azil.utils.TestDisposable
import com.nhaarman.mockito_kotlin.*
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

/**
 * Created by Zerina Salitrezic on 23/10/2017.
 */

class RegisterPresenterTest {

    private lateinit var presenter: RegisterPresenter

    private val view: RegisterInterface.View = mock()
    private val userInteractor: UserInteractor = mock()
    private val sharedPrefs: SharedPrefs = mock()

    @Before
    fun setUp() {
        presenter = RegisterPresenter(userInteractor, sharedPrefs)
        presenter.setView(view)
    }

    @Test
    fun nameChangedShouldSetName() {
        presenter.nameChanged(NAME)

        assertEquals(presenter.userData.name, NAME)
    }

    @Test
    fun emailChangedShouldSetEmail() {
        presenter.emailChanged(EMAIL)

        assertEquals(presenter.userData.email, EMAIL)
    }

    @Test
    fun passwordChangedShouldSetPassword() {
        presenter.passwordChanged(PASSWORD)

        assertEquals(presenter.userData.password, PASSWORD)
    }

    @Test
    fun retypedPasswordChangedShouldSetRetypedPassword() {
        presenter.retypedPasswordChanged(PASSWORD)

        assertEquals(presenter.userData.repeatedPassword, PASSWORD)
    }

    @Test
    fun nameChangedShouldRemoveNameErrorDisableRegister() {
        presenter.nameChanged(NAME)

        verify(view).removeNameError()
        verify(view).disableRegister()
        verifyNoMoreInteractions(view, userInteractor, sharedPrefs)
    }

    @Test
    fun emailChangedShouldRemoveEmailErrorDisableRegister() {
        presenter.emailChanged(EMAIL)

        verify(view).removeEmailError()
        verify(view).disableRegister()
        verifyNoMoreInteractions(view, userInteractor, sharedPrefs)
    }

    @Test
    fun passwordChangedShouldRemovePasswordErrorDisableRegister() {
        presenter.passwordChanged(PASSWORD)

        verify(view).removePasswordError()
        verify(view).disableRegister()
        verifyNoMoreInteractions(view, userInteractor, sharedPrefs)
    }

    @Test
    fun retypedPasswordChangedShouldRemoveRetypedPasswordErrorDisableRegister() {
        presenter.retypedPasswordChanged(PASSWORD)

        verify(view).removePasswordsNotSameError()
        verify(view).disableRegister()
        verifyNoMoreInteractions(view, userInteractor, sharedPrefs)
    }

    @Test
    fun checkIsFormValidShouldEnableRegister() {
        presenter.userData.name = NAME
        presenter.userData.email = EMAIL
        presenter.userData.password = PASSWORD
        presenter.userData.repeatedPassword = PASSWORD
        presenter.checkIsFormValid()

        verify(view).enableRegister()
        verifyNoMoreInteractions(view, userInteractor, sharedPrefs)
    }

    @Test
    fun checkIsFormValidShouldDisableRegister() {
        presenter.checkIsFormValid()

        verify(view).disableRegister()
        verifyNoMoreInteractions(view, userInteractor, sharedPrefs)
    }

    @Test
    fun onNameChangeFocusNoFocusValidName() {
        presenter.userData.name = NAME
        presenter.onNameChangeFocus(false)

        verify(view).setNameGreenLine()
        verify(view).removeNameError()
        verifyNoMoreInteractions(view, userInteractor, sharedPrefs)
    }

    @Test
    fun onNameChangeFocusNoFocusInvalidName() {
        presenter.onNameChangeFocus(false)

        verify(view).setNameRedLine()
        verify(view).setNameError()
        verifyNoMoreInteractions(view, userInteractor, sharedPrefs)
    }

    @Test
    fun onNameChangeFocusHasFocus() {
        presenter.onNameChangeFocus(true)

        verify(view).setNameBlueLine()
        verifyNoMoreInteractions(view, userInteractor, sharedPrefs)
    }

    @Test
    fun onEmailChangeFocusNoFocusValidEmail() {
        presenter.userData.email = EMAIL
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
    fun onPasswordChangeFocusNoFocusValidPasswordPasswordsNotSame() {
        presenter.userData.password = PASSWORD
        presenter.onPasswordChangeFocus(false)

        verify(view).setPasswordGreenLine()
        verify(view).removePasswordError()
        verify(view).setRetypedPasswordRedLine()
        verify(view).setPasswordsNotSameError()
        verifyNoMoreInteractions(view, userInteractor, sharedPrefs)
    }

    @Test
    fun onPasswordChangeFocusNoFocusInvalidPasswordPasswordsNotSame() {
        presenter.userData.repeatedPassword = PASSWORD
        presenter.onPasswordChangeFocus(false)

        verify(view).setPasswordRedLine()
        verify(view).setPasswordError()
        verify(view).setRetypedPasswordRedLine()
        verify(view).setPasswordsNotSameError()
        verifyNoMoreInteractions(view, userInteractor, sharedPrefs)
    }

    @Test
    fun onPasswordChangeFocusHasFocus() {
        presenter.onPasswordChangeFocus(true)

        verify(view).setPasswordBlueLine()
        verifyNoMoreInteractions(view, userInteractor, sharedPrefs)
    }

    @Test
    fun onRetypedPasswordChangeFocusNoFocusValidPasswords() {
        presenter.userData.password = PASSWORD
        presenter.userData.repeatedPassword = PASSWORD
        presenter.onRetypedPasswordChangeFocus(false)

        verify(view).setRetypedPasswordGreenLine()
        verify(view).removePasswordsNotSameError()
        verifyNoMoreInteractions(view, userInteractor, sharedPrefs)
    }

    @Test
    fun onRetypedPasswordChangeFocusNoFocusNotSamePasswords() {
        presenter.userData.password = PASSWORD
        presenter.onRetypedPasswordChangeFocus(false)

        verify(view).setRetypedPasswordRedLine()
        verify(view).setPasswordsNotSameError()
        verifyNoMoreInteractions(view, userInteractor, sharedPrefs)
    }

    @Test
    fun onRetypedPasswordChangeFocusHasFocus() {
        presenter.onRetypedPasswordChangeFocus(true)

        verify(view).setRetypedPasswordBlueLine()
        verifyNoMoreInteractions(view, userInteractor, sharedPrefs)
    }

    @Test
    fun registerClickedNoInternet() {
        presenter.registerClicked(false)

        verify(view).showNoInternetError()
        verifyNoMoreInteractions(view, userInteractor, sharedPrefs)
    }

    @Test
    fun registerClickedShouldSendRegisterUserRequest() {
        presenter.imagePath = IMAGE_URL
        presenter.registerClicked(true)

        verify(view).showProgressBar()
        verify(view).disableRegister()
        verify(userInteractor).registerUser(any(), any(), any())
        verifyNoMoreInteractions(view, userInteractor, sharedPrefs)
    }

    @Test
    fun getRegisterObserverOnSubscribe() {
        presenter.getRegisterObserver().onSubscribe(TestDisposable())

        verifyZeroInteractions(view, userInteractor, sharedPrefs)
    }

    @Test
    fun getRegisterObserverOnSuccess() {
        presenter.getRegisterObserver().onSuccess(mockLoginResponse())

        verify(view).hideProgressBar()
        verify(sharedPrefs).setLoggedIn(any())
        verify(sharedPrefs).setToken(any())
        verify(view).goBackWithResult()
        verifyNoMoreInteractions(view, userInteractor, sharedPrefs)
    }

    @Test
    fun getRegisterObserverOnErrorServerError() {
        presenter.getRegisterObserver().onError(mockHttpResponse(ERROR_CODE_BAD_REQUEST))

        verify(view).enableRegister()
        verify(view).hideProgressBar()
        verify(view).showServerError()
        verifyNoMoreInteractions(view, userInteractor, sharedPrefs)
    }

    @Test
    fun getRegisterObserverOnErrorUserAlreadyExists() {
        presenter.getRegisterObserver().onError(mockHttpResponse(ERROR_CODE_USER_ALREADY_EXISTS))

        verify(view).enableRegister()
        verify(view).hideProgressBar()
        verify(view).showUserExistsError()
        verifyNoMoreInteractions(view, userInteractor, sharedPrefs)
    }

    @Test
    fun getRegisterObserverOnErrorInvalidImageFormat() {
        presenter.getRegisterObserver().onError(mockHttpResponse(ERROR_CODE_WRONG_TYPE_FILE))

        verify(view).enableRegister()
        verify(view).hideProgressBar()
        verify(view).showInvalidImageFormatError()
        verifyNoMoreInteractions(view, userInteractor, sharedPrefs)
    }

    @Test
    fun getRegisterObserverOnErrorUploadImageError() {
        presenter.getRegisterObserver().onError(mockHttpResponse(ERROR_CODE_IMAGE_UPLOAD))

        verify(view).enableRegister()
        verify(view).hideProgressBar()
        verify(view).showUploadImageError()
        verifyNoMoreInteractions(view, userInteractor, sharedPrefs)
    }

    @Test
    fun termsAndConditionsClicked() {
        presenter.termsAndConditionsClicked()

        verify(view).goToTermsAndConditions()
        verifyNoMoreInteractions(view, userInteractor, sharedPrefs)
    }

    @Test
    fun iconBackClickedShouldGoBack() {
        presenter.iconBackClicked()

        verify(view).goBack()
        verifyNoMoreInteractions(view, userInteractor, sharedPrefs)
    }

    @Test
    fun profileImageClickedShouldShowImageChooserDialog() {
        presenter.profileImageClicked()

        verify(view).showImageChooserDialog()
        verifyNoMoreInteractions(view, userInteractor, sharedPrefs)
    }

    @Test
    fun cameraChosenShouldStartCamera() {
        presenter.cameraChosen()

        verify(view).startCamera()
        verifyNoMoreInteractions(view, userInteractor, sharedPrefs)
    }

    @Test
    fun galleryChosenShouldStartGallery() {
        presenter.galleryChosen()

        verify(view).startGallery()
        verifyNoMoreInteractions(view, userInteractor, sharedPrefs)
    }

    @Test
    fun setImagePathNoInteractionsInvalidPath() {
        presenter.setImagePath(EMPTY_TEXT)

        verifyZeroInteractions(view, userInteractor, sharedPrefs)
    }


    @Test
    fun setImagePathValidPath() {
        presenter.setImagePath(IMAGE_URL)

        verify(view).loadImage(any())
        verifyNoMoreInteractions(view, userInteractor, sharedPrefs)
    }

    @Test
    fun onTouchRootLayoutShouldClearInputFieldsFocusHideKeyboard() {
        presenter.touchRootLayout()

        verify(view).clearFocusFromInputFields()
        verify(view).requestFocusOnRootLayout()
        verify(view).hideKeyboard()
        verify(view).hideErrorLayout()
        verifyNoMoreInteractions(view, userInteractor, sharedPrefs)
    }

    @Test
    fun clickedDoneOnRetypedPasswordShouldClearInputFieldsFocusHideKeyboard() {
        presenter.clickedDoneOnRetypedPassword()

        verify(view).clearFocusFromInputFields()
        verify(view).requestFocusOnRootLayout()
        verify(view).hideKeyboard()
        verifyNoMoreInteractions(view, userInteractor, sharedPrefs)
    }

    @Test
    fun imageCapturedShouldLoadImage() {
        presenter.imageCaptured()

        verify(view).loadImage(any())
        verifyNoMoreInteractions(view, userInteractor, sharedPrefs)
    }

    @Test
    fun unSubscribe() {
        presenter.unSubscribe()

        verifyZeroInteractions(view, userInteractor, sharedPrefs)
    }
}