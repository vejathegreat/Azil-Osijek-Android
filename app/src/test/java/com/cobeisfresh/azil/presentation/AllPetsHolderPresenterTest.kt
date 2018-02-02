package com.cobeisfresh.azil.presentation

import com.cobeisfresh.azil.*
import com.cobeisfresh.azil.network_interaction.DogsInteractor
import com.cobeisfresh.azil.shared_prefs.SharedPrefs
import com.cobeisfresh.azil.ui.all_pets.list.AllPetsHolderInterface
import com.cobeisfresh.azil.utils.TestDisposable
import com.nhaarman.mockito_kotlin.*
import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Test

/**
 * Created by Zerina Salitrezic on 03/11/2017.
 */

class AllPetsHolderPresenterTest {

    private lateinit var presenter: AllPetsHolderPresenter

    private val view: AllPetsHolderInterface.View = mock()
    private val dogsInteractor: DogsInteractor = mock()
    private val sharedPrefs: SharedPrefs = mock()

    @Before
    fun setUp() {
        presenter = AllPetsHolderPresenter(dogsInteractor, sharedPrefs)
        presenter.setView(view)
    }

    @Test
    fun setDogModel() {
        presenter.setDogModel(mockDog())

        assertEquals(presenter.dogModel, mockDog())
        assertEquals(presenter.dogId, ID)
    }

    @Test
    fun setDogModelShouldShowDogData() {
        presenter.setDogModel(mockDog())

        verify(view).setNotFavouriteIcon()
        verify(view).showName(any())
        verify(view).showGender(any())
        verify(view).showAge(any())
        verify(view).showSize(any())
        verify(view).showDescription(any())
        verify(view).showImage(any())
        verifyNoMoreInteractions(view, dogsInteractor, sharedPrefs)
    }

    @Test
    fun setIsFavouriteShouldSetFavouriteIcon() {
        presenter.setIsFavourite(true)

        verify(view).setFavouriteIcon()
        verifyNoMoreInteractions(view, dogsInteractor, sharedPrefs)
    }

    @Test
    fun setIsFavouriteShouldSetNotFavouriteIcon() {
        presenter.setIsFavourite(false)

        verify(view).setNotFavouriteIcon()
        verifyNoMoreInteractions(view, dogsInteractor, sharedPrefs)
    }

    @Test
    fun showNameNoInteractions() {
        presenter.showName(EMPTY_TEXT)

        verifyZeroInteractions(view, dogsInteractor, sharedPrefs)
    }

    @Test
    fun showNameShouldShowName() {
        presenter.showName(NAME)

        verify(view).showName(any())
        verifyNoMoreInteractions(view, dogsInteractor, sharedPrefs)
    }

    @Test
    fun showGenderNoInteractions() {
        presenter.showGender(EMPTY_TEXT)

        verifyZeroInteractions(view, dogsInteractor, sharedPrefs)
    }

    @Test
    fun showGenderShouldShowGender() {
        presenter.showGender(GENDER)

        verify(view).showGender(any())
        verifyNoMoreInteractions(view, dogsInteractor, sharedPrefs)
    }

    @Test
    fun showAgeNoInteractions() {
        presenter.showAge(EMPTY_TEXT)

        verifyZeroInteractions(view, dogsInteractor, sharedPrefs)
    }

    @Test
    fun showAgeShouldShowAge() {
        presenter.showAge(AGE)

        verify(view).showAge(any())
        verifyNoMoreInteractions(view, dogsInteractor, sharedPrefs)
    }

    @Test
    fun showSizeNoInteractions() {
        presenter.showSize(EMPTY_TEXT)

        verifyZeroInteractions(view, dogsInteractor, sharedPrefs)
    }

    @Test
    fun showSizeShouldShowSize() {
        presenter.showSize(SIZE)

        verify(view).showSize(any())
        verifyNoMoreInteractions(view, dogsInteractor, sharedPrefs)
    }

    @Test
    fun showDescriptionNoInteractions() {
        presenter.showDescription(EMPTY_TEXT)

        verifyZeroInteractions(view, dogsInteractor, sharedPrefs)
    }

    @Test
    fun showDescriptionShouldShowDescription() {
        presenter.showDescription(DESCRIPTION)

        verify(view).showDescription(any())
        verifyNoMoreInteractions(view, dogsInteractor, sharedPrefs)
    }

    @Test
    fun showImageShouldShowImage() {
        presenter.showImage(mockPhotosList())

        verify(view).showImage(any())
        verifyNoMoreInteractions(view, dogsInteractor, sharedPrefs)
    }

    @Test
    fun setImagesShouldPlaceHolder() {
        presenter.showImage(listOf())

        verify(view).showPlaceholder()
        verifyNoMoreInteractions(view, dogsInteractor, sharedPrefs)
    }

    @Test
    fun dogLayoutClickedShouldCallOnItemClick() {
        presenter.dogLayoutClicked()

        verify(view).callOnItemClick(any())
        verifyNoMoreInteractions(view, dogsInteractor, sharedPrefs)
    }

    @Test
    fun iconFavouriteClickedUserNotLoggedIn() {
        presenter.iconFavouriteClicked(false)

        verify(sharedPrefs).isLoggedIn()
        verify(view).showUserMustExistError()
        verifyNoMoreInteractions(view, dogsInteractor, sharedPrefs)
    }

    @Test
    fun iconFavouriteClickedNoInternet() {
        whenever(sharedPrefs.isLoggedIn()).thenReturn(true)
        presenter.iconFavouriteClicked(false)

        verify(sharedPrefs).isLoggedIn()
        verify(view).showNoInternetError()
        verifyNoMoreInteractions(view, dogsInteractor, sharedPrefs)
    }

    @Test
    fun iconFavouriteClickedSendAddRemoveFavouriteRequest() {
        whenever(sharedPrefs.isLoggedIn()).thenReturn(true)
        presenter.iconFavouriteClicked(true)

        verify(sharedPrefs).isLoggedIn()
        verify(view).hideFavouriteIcon()
        verify(view).showFavouritesProgressBar()
        verify(dogsInteractor).addRemoveFavourite(any(), any())
        verifyNoMoreInteractions(view, dogsInteractor, sharedPrefs)
    }

    @Test
    fun getAddRemoveObserverOnSubscribe() {
        presenter.getAddRemoveObserver().onSubscribe(TestDisposable())

        verifyZeroInteractions(view, dogsInteractor, sharedPrefs)
    }

    @Test
    fun getAddRemoveObserverOnSuccess() {
        presenter.isCurrentlyFavourite = false
        presenter.getAddRemoveObserver().onSuccess(mockBaseResponse())

        verify(view).hideFavouritesProgressBar()
        verify(view).setFavouriteIcon()
        verify(view).sendFavouritedDogEvent()
        verifyNoMoreInteractions(view, dogsInteractor, sharedPrefs)
    }

    @Test
    fun getAddRemoveObserverOnError() {
        presenter.getAddRemoveObserver().onError(mockHttpResponse(ERROR_CODE_BAD_REQUEST))

        verify(view).hideFavouritesProgressBar()
        verify(view).showChangeFavouriteStateError()
        verify(view).setNotFavouriteIcon()
        verifyNoMoreInteractions(view, dogsInteractor, sharedPrefs)
    }

    @Test
    fun sendFavouriteEventShouldSendFavouriteDogEvent() {
        presenter.isCurrentlyFavourite = true
        presenter.sendFavouriteEvent()

        verify(view).sendFavouritedDogEvent()
        verifyNoMoreInteractions(view, dogsInteractor, sharedPrefs)
    }

    @Test
    fun sendFavouriteEventShouldSendDogDeletedEvent() {
        presenter.sendFavouriteEvent()

        verify(view).sendDogDeletedEvent(any())
        verifyNoMoreInteractions(view, dogsInteractor, sharedPrefs)
    }
}