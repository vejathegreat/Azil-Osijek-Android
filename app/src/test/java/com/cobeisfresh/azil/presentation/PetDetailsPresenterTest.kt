package com.cobeisfresh.azil.presentation

import com.cobeisfresh.azil.*
import com.cobeisfresh.azil.data.response.BaseResponse
import com.cobeisfresh.azil.network_interaction.DogsInteractor
import com.cobeisfresh.azil.shared_prefs.SharedPrefs
import com.cobeisfresh.azil.ui.pet_details.PetDetailsInterface
import com.cobeisfresh.azil.utils.TestDisposable
import com.nhaarman.mockito_kotlin.*
import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Test


/**
 * Created by Zerina Salitrezic on 12/10/2017.
 */

class PetDetailsPresenterTest {

    private lateinit var presenter: PetDetailsPresenter

    private val view: PetDetailsInterface.View = mock()
    private val dogsInteractor: DogsInteractor = mock()
    private val sharedPrefs: SharedPrefs = mock()

    @Before
    fun setUp() {
        presenter = PetDetailsPresenter(dogsInteractor, sharedPrefs)
        presenter.setView(view)
    }

    @Test
    fun setDogId() {
        presenter.setDogId(ID)

        assertEquals(presenter.dogId, ID)
    }

    @Test
    fun userAuthenticatedShouldRefreshPetDetails() {
        presenter.userAuthenticated()

        verify(view).refreshPetDetails()
        verifyNoMoreInteractions(view, dogsInteractor, sharedPrefs)
    }

    @Test
    fun errorFullLayoutClickedNoInternet() {
        presenter.errorFullLayoutClicked(false)

        verify(view).hideErrorFullScreen()
        verify(view).showProgressBar()
        verify(view).hideProgressBar()
        verify(view).showNoInternetErrorFullScreen()
        verifyNoMoreInteractions(view, dogsInteractor, sharedPrefs)
    }

    @Test
    fun errorFullLayoutClickedShouldSendRequestGetDogById() {
        presenter.errorFullLayoutClicked(true)

        verify(view).hideErrorFullScreen()
        verify(view).showProgressBar()
        verify(dogsInteractor).getDogById(any(), any())
        verifyNoMoreInteractions(view, dogsInteractor, sharedPrefs)
    }

    @Test
    fun dogLayoutClickedShouldHideErrorLayout() {
        presenter.errorLayoutClicked()

        verify(view).hideErrorLayout()
        verifyNoMoreInteractions(view, dogsInteractor, sharedPrefs)
    }

    @Test
    fun getDogByIdNoInternet() {
        presenter.getDogById(false)

        verify(view).showNoInternetErrorFullScreen()
        verifyNoMoreInteractions(view, dogsInteractor, sharedPrefs)
    }

    @Test
    fun getDogByIdShouldSendRequestGetDogById() {
        presenter.getDogById(true)

        verify(view).showProgressBar()
        verify(dogsInteractor).getDogById(any(), any())
        verifyNoMoreInteractions(view, dogsInteractor, sharedPrefs)
    }

    @Test
    fun getDogObserverOnSubscribe() {
        presenter.getDogObserver().onSubscribe(TestDisposable())

        verifyZeroInteractions(view, dogsInteractor, sharedPrefs)
    }

    @Test
    fun getDogObserverOnSuccessShouldShowData() {
        presenter.getDogObserver().onSuccess(mockDogDetailsResponse())

        verify(view).hideProgressBar()
        verify(view).showDogDetailsLayout()
        verify(view).showAdoptButton()
        verify(view).setNotFavouriteIcon()
        verify(view).showPageIndicator()
        verify(view).showImages(any())
        verify(view).showName(any())
        verify(view).showGender(any())
        verify(view).showAge(any())
        verify(view).showSize(any())
        verify(view).showDescription(any())
        verifyNoMoreInteractions(view, dogsInteractor, sharedPrefs)
    }

    @Test
    fun getDogObserverOnError() {
        presenter.getDogObserver().onError(mockHttpResponse(ERROR_CODE_BAD_REQUEST))

        verify(view).hideProgressBar()
        verify(view).hideDogDetailsLayout()
        verify(view).showServerErrorFullScreen()
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
    fun setImagesShouldShowImages() {
        presenter.setImages(mockPhotosList())

        verify(view).showPageIndicator()
        verify(view).showImages(any())
        verifyNoMoreInteractions(view, dogsInteractor, sharedPrefs)
    }

    @Test
    fun handlePageIndicatorVisibilityNoInteractions() {
        presenter.handlePageIndicatorVisibility(0)

        verifyZeroInteractions(view, dogsInteractor, sharedPrefs)
    }

    @Test
    fun handlePageIndicatorVisibilityShouldShowPageIndicator() {
        presenter.handlePageIndicatorVisibility(2)

        verify(view).showPageIndicator()
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
    fun showLongDescriptionNoInteractions() {
        presenter.showLongDescription(EMPTY_TEXT)

        verifyZeroInteractions(view, dogsInteractor, sharedPrefs)
    }

    @Test
    fun showLongDescriptionShouldShowDescription() {
        presenter.showLongDescription(DESCRIPTION)

        verify(view).showDescription(any())
        verifyNoMoreInteractions(view, dogsInteractor, sharedPrefs)
    }

    @Test
    fun iconFavouriteClickedNoUser() {
        presenter.iconFavouriteClicked(true)

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
    fun iconFavouriteClickedShouldSendAddRemoveFavouriteRequest() {
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
        presenter.hasCurrentlyFavourite = false
        presenter.getAddRemoveObserver().onSuccess(BaseResponse())

        verify(view).hideFavouritesProgressBar()
        verify(view).sendFavouriteStateEvent(any(), any())
        verify(view).setFavouriteIcon()
        verifyNoMoreInteractions(view, dogsInteractor, sharedPrefs)
    }

    @Test
    fun getAddRemoveObserverOnError() {
        presenter.getAddRemoveObserver().onError(mockHttpResponse(ERROR_CODE_BAD_REQUEST))

        verify(view).hideFavouritesProgressBar()
        verify(view).showServerError()
        verify(view).setNotFavouriteIcon()
        verifyNoMoreInteractions(view, dogsInteractor, sharedPrefs)
    }

    @Test
    fun handleFavouriteStateShouldSetNotFavouriteIcon() {
        presenter.handleFavouriteState()

        verify(view).setNotFavouriteIcon()
        verifyNoMoreInteractions(view, dogsInteractor, sharedPrefs)
    }

    @Test
    fun handleFavouriteStateShouldSetFavouriteIcon() {
        presenter.hasCurrentlyFavourite = true
        presenter.handleFavouriteState()

        verify(view).setFavouriteIcon()
        verifyNoMoreInteractions(view, dogsInteractor, sharedPrefs)
    }

    @Test
    fun iconBackClickedShouldGoBack() {
        presenter.iconBackClicked()

        verify(view).goBack()
        verifyNoMoreInteractions(view, dogsInteractor, sharedPrefs)
    }

    @Test
    fun adoptPetClickedNoInteractions() {
        presenter.adoptPetClicked()

        verifyZeroInteractions(view, dogsInteractor, sharedPrefs)
    }

    @Test
    fun adoptPetClickedShouldOpenAdoptPetActivity() {
        presenter.dogDetailsResponse = mockDogDetailsResponse()
        presenter.adoptPetClicked()

        verify(view).openAdoptPetActivity(any())
        verifyNoMoreInteractions(view, dogsInteractor, sharedPrefs)
    }

    @Test
    fun imageClickedShouldOpenPetImagesActivity() {
        presenter.imageClicked(mockPetImagesDataMoreImages())

        verify(view).openPetImagesActivity(any())
        verifyNoMoreInteractions(view, dogsInteractor, sharedPrefs)
    }

    @Test
    fun unSubscribe() {
        presenter.unSubscribe()

        verifyZeroInteractions(view, dogsInteractor, sharedPrefs)
    }
}