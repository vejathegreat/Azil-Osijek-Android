package com.cobeisfresh.azil.presentation

import com.cobeisfresh.azil.*
import com.cobeisfresh.azil.data.models.DogModel
import com.cobeisfresh.azil.data.response.BaseResponse
import com.cobeisfresh.azil.network_interaction.DogsInteractor
import com.cobeisfresh.azil.ui.my_pets.list.MyPetsHolderInterface
import com.cobeisfresh.azil.utils.TestDisposable
import com.nhaarman.mockito_kotlin.*
import org.junit.Before
import org.junit.Test

/**
 * Created by Zerina Salitrezic on 17/10/2017.
 */

class MyPetsHolderPresenterTest {

    private lateinit var presenter: MyPetsHolderPresenter

    private val view: MyPetsHolderInterface.View = mock()
    private val dogsInteractor: DogsInteractor = mock()

    @Before
    fun setUp() {
        presenter = MyPetsHolderPresenter(dogsInteractor)
        presenter.setView(view)
    }

    @Test
    fun setDogModelShouldShowDogData() {
        presenter.setDogModel(mockDog())

        verify(view).setFavouriteIcon()
        verify(view).showName(any())
        verify(view).showGender(any())
        verify(view).showAge(any())
        verify(view).showSize(any())
        verify(view).showDescription(any())
        verify(view).showImage(any())
        verifyNoMoreInteractions(view, dogsInteractor)
    }

    @Test
    fun showNameNoInteractions() {
        presenter.showName(EMPTY_TEXT)

        verifyZeroInteractions(view, dogsInteractor)
    }

    @Test
    fun showNameShouldShowName() {
        presenter.showName(NAME)

        verify(view).showName(any())
        verifyNoMoreInteractions(view, dogsInteractor)
    }

    @Test
    fun showGenderNoInteractions() {
        presenter.showGender(EMPTY_TEXT)

        verifyZeroInteractions(view, dogsInteractor)
    }

    @Test
    fun showGenderShouldShowGender() {
        presenter.showGender(GENDER)

        verify(view).showGender(any())
        verifyNoMoreInteractions(view, dogsInteractor)
    }

    @Test
    fun showAgeNoInteractions() {
        presenter.showAge(EMPTY_TEXT)

        verifyZeroInteractions(view, dogsInteractor)
    }

    @Test
    fun showAgeShouldShowAge() {
        presenter.showAge(AGE)

        verify(view).showAge(any())
        verifyNoMoreInteractions(view, dogsInteractor)
    }

    @Test
    fun showSizeNoInteractions() {
        presenter.showSize(EMPTY_TEXT)

        verifyZeroInteractions(view, dogsInteractor)
    }

    @Test
    fun showSizeShouldShowSize() {
        presenter.showSize(SIZE)

        verify(view).showSize(any())
        verifyNoMoreInteractions(view, dogsInteractor)
    }

    @Test
    fun showDescriptionNoInteractions() {
        presenter.showDescription(EMPTY_TEXT)

        verifyZeroInteractions(view, dogsInteractor)
    }

    @Test
    fun showDescriptionShouldShowDescription() {
        presenter.showDescription(DESCRIPTION)

        verify(view).showDescription(any())
        verifyNoMoreInteractions(view, dogsInteractor)
    }

    @Test
    fun showImageShouldShowImage() {
        presenter.showImage(mockPhotosList())

        verify(view).showImage(any())
        verifyNoMoreInteractions(view, dogsInteractor)
    }

    @Test
    fun setImagesShouldPlaceHolder() {
        presenter.showImage(listOf())

        verify(view).showPlaceholder()
        verifyNoMoreInteractions(view, dogsInteractor)
    }

    @Test
    fun dogLayoutClickedShouldCallOnItemClick() {
        presenter.dogLayoutClicked()

        verify(view).callOnItemClick(any())
        verifyNoMoreInteractions(view, dogsInteractor)
    }

    @Test
    fun iconFavouriteClickedShouldShowDeleteDialog() {
        presenter.iconFavouriteClicked()

        verify(view).showDeleteDialog(any())
        verifyNoMoreInteractions(view, dogsInteractor)
    }

    @Test
    fun deleteFavouriteNoInternet() {
        presenter.deleteFavourite(false, DogModel())

        verify(view).showNoInternetError()
        verifyNoMoreInteractions(view, dogsInteractor)
    }

    @Test
    fun deleteFavouriteShouldCallAddRemoveFavouriteRequest() {
        presenter.deleteFavourite(true, mockDog())

        verify(view).hideFavouriteIcon()
        verify(view).showFavouritesProgressBar()
        verify(dogsInteractor).addRemoveFavourite(any(), any())
        verifyNoMoreInteractions(view, dogsInteractor)
    }

    @Test
    fun getAddRemoveObserverOnSubscribe() {
        presenter.getAddRemoveObserver().onSubscribe(TestDisposable())

        verifyZeroInteractions(view, dogsInteractor)
    }

    @Test
    fun getAddRemoveObserverOnSuccess() {
        presenter.getAddRemoveObserver().onSuccess(BaseResponse())

        verify(view).hideFavouritesProgressBar()
        verify(view).sendDogDeletedEvent(any())
        verifyNoMoreInteractions(view, dogsInteractor)
    }

    @Test
    fun getAddRemoveObserverOnError() {
        presenter.getAddRemoveObserver().onError(mockHttpResponse(ERROR_CODE_BAD_REQUEST))

        verify(view).hideFavouritesProgressBar()
        verify(view).setFavouriteIcon()
        verify(view).showServerError()
        verifyNoMoreInteractions(view, dogsInteractor)
    }
}