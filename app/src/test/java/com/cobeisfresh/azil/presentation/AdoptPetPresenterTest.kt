package com.cobeisfresh.azil.presentation

import com.cobeisfresh.azil.*
import com.cobeisfresh.azil.network_interaction.DogsInteractor
import com.cobeisfresh.azil.network_interaction.UserInteractor
import com.cobeisfresh.azil.shared_prefs.SharedPrefs
import com.cobeisfresh.azil.ui.adopt_pet.AdoptPetInterface
import com.cobeisfresh.azil.utils.TestDisposable
import com.nhaarman.mockito_kotlin.*
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

/**
 * Created by Zerina Salitrezic on 25/10/2017.
 */

class AdoptPetPresenterTest {

    private lateinit var presenter: AdoptPetPresenter

    private val view: AdoptPetInterface.View = mock()
    private val userInteractor: UserInteractor = mock()
    private val dogsInteractor: DogsInteractor = mock()
    private val sharedPrefs: SharedPrefs = mock()

    @Before
    fun setUp() {
        presenter = AdoptPetPresenter(dogsInteractor, userInteractor, sharedPrefs)
        presenter.setView(view)
    }

    @Test
    fun setDogData() {
        presenter.setDogData(mockDogDetailsResponse())

        assertEquals(presenter.adoptDogData.petId, ID)
    }

    @Test
    fun setIsAcceptBrochure() {
        presenter.setIsAcceptBrochure(true)

        assertEquals(presenter.adoptDogData.isAcceptBrochure, true)
    }

    @Test
    fun fullNameChanged() {
        presenter.fullNameChanged(NAME)

        assertEquals(presenter.adoptDogData.name, NAME)
    }

    @Test
    fun emailChanged() {
        presenter.emailChanged(EMAIL)

        assertEquals(presenter.adoptDogData.email, EMAIL)
    }

    @Test
    fun messageChanged() {
        presenter.messageChanged(MESSAGE)

        assertEquals(presenter.adoptDogData.message, MESSAGE)
    }

    @Test
    fun setDogDataShouldShowDogData() {
        presenter.setDogData(mockDogDetailsResponse())

        verify(view).showImage(any())
        verify(view).showName(any())
        verifyNoMoreInteractions(view, userInteractor, dogsInteractor, sharedPrefs)
    }

    @Test
    fun showImageShouldShowImage() {
        presenter.showImage(mockPhotosList())

        verify(view).showImage(any())
        verifyNoMoreInteractions(view, userInteractor, dogsInteractor, sharedPrefs)
    }

    @Test
    fun showImageShouldShowPlaceholder() {
        presenter.showImage(listOf())

        verify(view).showPlaceholder()
        verifyNoMoreInteractions(view, userInteractor, dogsInteractor, sharedPrefs)
    }

    @Test
    fun showNameNoInteractions() {
        presenter.showName(EMPTY_TEXT)

        verifyZeroInteractions(view, userInteractor, dogsInteractor, sharedPrefs)
    }

    @Test
    fun showNameShouldShowName() {
        presenter.showName(NAME)

        verify(view).showName(any())
        verifyNoMoreInteractions(view, userInteractor, dogsInteractor, sharedPrefs)
    }

    @Test
    fun getMeWhenNoUser() {
        presenter.getMe(false)

        verify(sharedPrefs).isLoggedIn()
        verifyZeroInteractions(view, userInteractor, dogsInteractor, sharedPrefs)
    }

    @Test
    fun getMeWhenNoInternet() {
        whenever(sharedPrefs.isLoggedIn()).thenReturn(true)
        presenter.getMe(false)

        verify(sharedPrefs).isLoggedIn()
        verify(view).showNoInternetError()
        verifyNoMoreInteractions(view, userInteractor, dogsInteractor, sharedPrefs)
    }

    @Test
    fun getMeShouldSendGetMeRequest() {
        whenever(sharedPrefs.isLoggedIn()).thenReturn(true)
        presenter.getMe(true)

        verify(sharedPrefs).isLoggedIn()
        verify(userInteractor).getMe(any())
        verifyNoMoreInteractions(view, userInteractor, dogsInteractor, sharedPrefs)
    }

    @Test
    fun getMeObserverOnSubscribe() {
        presenter.getMeObserver().onSubscribe(TestDisposable())

        verifyZeroInteractions(view, userInteractor, dogsInteractor, sharedPrefs)
    }

    @Test
    fun getMeObserverOnSuccessShouldSetUserDataCheckValidation() {
        presenter.adoptDogData.name = NAME
        presenter.adoptDogData.email = EMAIL
        presenter.getMeObserver().onSuccess(mockUserResponse())

        verify(view).showUserEmail(any())
        verify(view).showUserName(any())
        verify(view).setNameGreenLine()
        verify(view).removeNameError()
        verify(view).setEmailGreenLine()
        verify(view).removeEmailError()
        verifyNoMoreInteractions(view, userInteractor, dogsInteractor, sharedPrefs)
    }

    @Test
    fun getMeObserverOnError() {
        presenter.getMeObserver().onError(mockHttpResponse(ERROR_CODE_BAD_REQUEST))

        verifyZeroInteractions(view, userInteractor, dogsInteractor, sharedPrefs)
    }

    @Test
    fun adoptPetClickedNoInternet() {
        presenter.adoptPetClicked(false)

        verify(view).showNoInternetError()
        verifyNoMoreInteractions(view, userInteractor, dogsInteractor, sharedPrefs)
    }

    @Test
    fun adoptPetClickedShouldSendAdoptDogRequest() {
        presenter.adoptPetClicked(true)

        verify(view).showProgressBar()
        verify(view).disableAdopt()
        verify(dogsInteractor).adoptDog(any(), any())
        verifyNoMoreInteractions(view, userInteractor, dogsInteractor, sharedPrefs)
    }

    @Test
    fun getAdoptDogObserverOnSubscribe() {
        presenter.getAdoptDogObserver().onSubscribe(TestDisposable())

        verifyZeroInteractions(view, userInteractor, dogsInteractor, sharedPrefs)
    }

    @Test
    fun getAdoptDogObserverOnSuccess() {
        presenter.getAdoptDogObserver().onSuccess(mockBaseResponse())

        verify(view).sendAdoptionFeatureSelectedEvent()
        verify(view).hideProgressBar()
        verify(view).showSuccessAdoptionLayout()
        verifyNoMoreInteractions(view, userInteractor, dogsInteractor, sharedPrefs)
    }

    @Test
    fun getAdoptDogObserverOnError() {
        presenter.getAdoptDogObserver().onError(mockHttpResponse(ERROR_CODE_BAD_REQUEST))

        verify(view).hideProgressBar()
        verify(view).enableAdopt()
        verify(view).showServerError()
        verifyNoMoreInteractions(view, userInteractor, dogsInteractor, sharedPrefs)
    }

    @Test
    fun setIsAcceptBrochureShouldDisableAdopt() {
        presenter.setIsAcceptBrochure(false)

        verify(view).disableAdopt()
        verifyNoMoreInteractions(view, userInteractor, dogsInteractor, sharedPrefs)
    }

    @Test
    fun nameChangedShouldRemoveNameErrorDisableAdopt() {
        presenter.fullNameChanged(NAME)

        verify(view).removeNameError()
        verify(view).disableAdopt()
        verifyNoMoreInteractions(view, userInteractor, sharedPrefs)
    }

    @Test
    fun emailChangedShouldRemoveEmailErrorDisableAdopt() {
        presenter.emailChanged(EMAIL)

        verify(view).removeEmailError()
        verify(view).disableAdopt()
        verifyNoMoreInteractions(view, userInteractor, sharedPrefs)
    }

    @Test
    fun checkIsFormValidShouldEnableAdoptDog() {
        presenter.adoptDogData = mockAdoptDogData()
        presenter.checkIsFormValid()

        verify(view).enableAdopt()
        verifyNoMoreInteractions(view, userInteractor, sharedPrefs)
    }

    @Test
    fun checkIsFormValidShouldDisableAdoptDog() {
        presenter.checkIsFormValid()

        verify(view).disableAdopt()
        verifyNoMoreInteractions(view, userInteractor, sharedPrefs)
    }

    @Test
    fun onNameChangeFocusNoFocusValidName() {
        presenter.adoptDogData.name = NAME
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
        presenter.adoptDogData.email = EMAIL
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
    fun successAdoptionLayoutClickedShouldGoBack() {
        presenter.successAdoptionLayoutClicked()

        verify(view).goBack()
        verifyNoMoreInteractions(view, userInteractor, sharedPrefs)
    }

    @Test
    fun iconBackClickedShouldGoBack() {
        presenter.iconBackClicked()

        verify(view).goBack()
        verifyNoMoreInteractions(view, userInteractor, dogsInteractor, sharedPrefs)
    }

    @Test
    fun touchRootLayoutShouldClearInputFieldsFocusHideKeyboard() {
        presenter.touchRootLayout()

        verify(view).clearFocusFromInputFields()
        verify(view).hideKeyboard()
        verify(view).hideErrorLayout()
        verifyNoMoreInteractions(view, userInteractor, dogsInteractor, sharedPrefs)
    }

    @Test
    fun unSubscribe() {
        presenter.unSubscribe()

        verifyZeroInteractions(view, userInteractor, dogsInteractor, sharedPrefs)
    }
}