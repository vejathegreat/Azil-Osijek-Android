package com.cobeisfresh.azil.presentation

import com.cobeisfresh.azil.*
import com.cobeisfresh.azil.network_interaction.UserInteractor
import com.cobeisfresh.azil.ui.edit_profile.EditProfileInterface
import com.cobeisfresh.azil.utils.TestDisposable
import com.nhaarman.mockito_kotlin.*
import org.junit.Before
import org.junit.Test

/**
 * Created by Zerina Salitrezic on 31/10/2017.
 */

class EditProfilePresenterTest {

    private lateinit var presenter: EditProfilePresenter

    private val view: EditProfileInterface.View = mock()
    private val userInteractor: UserInteractor = mock()

    @Before
    fun setUp() {
        presenter = EditProfilePresenter(userInteractor)
        presenter.setView(view)
    }

    @Test
    fun setUserResponseShouldShowShowUserDataCheckFormValidation() {
        presenter.fullName = NAME
        presenter.setUserResponse(mockUserResponse())

        verify(view).showProfileImage(any())
        verify(view).showFullName(any())
        verify(view).setFullNameGreenLine()
        verify(view).removeFullNameError()
        verifyNoMoreInteractions(view, userInteractor)
    }

    @Test
    fun fullNameChangedShouldRemoveFullNameErrorEnableSave() {
        presenter.fullName = NAME
        presenter.fullNameChanged(NAME)

        verify(view).removeFullNameError()
        verify(view).enableSave()
        verifyNoMoreInteractions(view, userInteractor)
    }

    @Test
    fun checkIsFormValidShouldDisableSave() {
        presenter.checkIsFormValid()

        verify(view).disableSave()
        verifyNoMoreInteractions(view, userInteractor)
    }

    @Test
    fun checkIsFormValidShouldEnableSave() {
        presenter.fullName = NAME
        presenter.checkIsFormValid()

        verify(view).enableSave()
        verifyNoMoreInteractions(view, userInteractor)
    }

    @Test
    fun onFullNameChangeFocusHasFocus() {
        presenter.onFullNameChangeFocus(true)

        verify(view).setFullNameBlueLine()
        verifyNoMoreInteractions(view, userInteractor)
    }

    @Test
    fun onFullNameChangeFocusNoFocusValidFullName() {
        presenter.fullName = NAME
        presenter.onFullNameChangeFocus(false)

        verify(view).setFullNameGreenLine()
        verify(view).removeFullNameError()
        verifyNoMoreInteractions(view, userInteractor)
    }

    @Test
    fun onFullNameChangeFocusNoFocusInvalidFullName() {
        presenter.onFullNameChangeFocus(false)

        verify(view).setFullNameRedLine()
        verify(view).setFullNameError()
        verifyNoMoreInteractions(view, userInteractor)
    }

    @Test
    fun profileImageClickedShouldShowImageChooserDialog() {
        presenter.profileImageClicked()

        verify(view).showImageChooserDialog()
        verifyNoMoreInteractions(view, userInteractor)
    }

    @Test
    fun galleryChosenShouldStartGallery() {
        presenter.galleryChosen()

        verify(view).startGallery()
        verifyNoMoreInteractions(view, userInteractor)
    }

    @Test
    fun cameraChosenShouldStartCamera() {
        presenter.cameraChosen()

        verify(view).startCamera()
        verifyNoMoreInteractions(view, userInteractor)
    }

    @Test
    fun saveClickedNoInternet() {
        presenter.saveClicked(false)

        verify(view).showNoInternetError()
        verifyNoMoreInteractions(view, userInteractor)
    }

    @Test
    fun saveClickedShouldSendSetMeRequest() {
        presenter.imagePath = IMAGE_URL
        presenter.fullName = NAME
        presenter.saveClicked(true)

        verify(view).showProgressBar()
        verify(view).disableSave()
        verify(userInteractor).setMe(any(), any(), any())
        verifyNoMoreInteractions(view, userInteractor)
    }

    @Test
    fun getMeObserverOnSubscribe() {
        presenter.getMeObserver().onSubscribe(TestDisposable())

        verifyZeroInteractions(view, userInteractor)
    }

    @Test
    fun getMeObserverOnSuccess() {
        presenter.getMeObserver().onSuccess(mockUserResponse())

        verify(view).hideProgressBar()
        verify(view).goBack(any())
        verifyNoMoreInteractions(view, userInteractor)
    }

    @Test
    fun getMeObserverOnErrorServerError() {
        presenter.getMeObserver().onError(mockHttpResponse(ERROR_CODE_BAD_REQUEST))

        verify(view).hideProgressBar()
        verify(view).enableSave()
        verify(view).showServerError()
        verifyNoMoreInteractions(view, userInteractor)
    }

    @Test
    fun getRegisterObserverOnErrorInvalidImageFormat() {
        presenter.getMeObserver().onError(mockHttpResponse(ERROR_CODE_WRONG_TYPE_FILE))

        verify(view).hideProgressBar()
        verify(view).enableSave()
        verify(view).showInvalidImageFormatError()
        verifyNoMoreInteractions(view, userInteractor)
    }

    @Test
    fun getRegisterObserverOnErrorUploadImageError() {
        presenter.getMeObserver().onError(mockHttpResponse(ERROR_CODE_IMAGE_UPLOAD))

        verify(view).hideProgressBar()
        verify(view).enableSave()
        verify(view).showUploadImageError()
        verifyNoMoreInteractions(view, userInteractor)
    }

    @Test
    fun setImagePathShouldShowImage() {
        presenter.setImagePath(IMAGE_URL)

        verify(view).showProfileImage(any())
        verifyNoMoreInteractions(view, userInteractor)
    }

    @Test
    fun iconBackClickedShouldGoBackWithUpdatedInfo() {
        presenter.iconBackClicked()

        verify(view).goBack(any())
        verifyNoMoreInteractions(view, userInteractor)
    }

    @Test
    fun imageCapturedShouldShowImage() {
        presenter.imagePath = IMAGE_URL
        presenter.imageCaptured()

        verify(view).showProfileImage(any())
        verifyNoMoreInteractions(view, userInteractor)
    }

    @Test
    fun touchRootLayoutShouldClearFocusFromFullName() {
        presenter.touchRootLayout()

        verify(view).clearFocusFromFullName()
        verify(view).hideKeyboard()
        verify(view).hideErrorLayout()
        verifyNoMoreInteractions(view, userInteractor)
    }

    @Test
    fun clickedDoneOnFullNameShouldRequestFocusRootLayoutHideKeyboard() {
        presenter.clickedDoneOnFullName()

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