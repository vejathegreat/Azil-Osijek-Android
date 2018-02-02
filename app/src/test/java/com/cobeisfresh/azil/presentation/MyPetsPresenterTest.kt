package com.cobeisfresh.azil.presentation

import com.cobeisfresh.azil.ERROR_CODE_BAD_REQUEST
import com.cobeisfresh.azil.mockDogsList
import com.cobeisfresh.azil.mockHttpResponse
import com.cobeisfresh.azil.network_interaction.DogsInteractor
import com.cobeisfresh.azil.shared_prefs.SharedPrefs
import com.cobeisfresh.azil.ui.my_pets.MyPetsInterface
import com.cobeisfresh.azil.utils.TestDisposable
import com.nhaarman.mockito_kotlin.*
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

/**
 * Created by Zerina Salitrezic on 17/10/2017.
 */

class MyPetsPresenterTest {

    private lateinit var presenter: MyPetsPresenter

    private val view: MyPetsInterface.View = mock()
    private val dogsInteractor: DogsInteractor = mock()
    private val sharedPrefs: SharedPrefs = mock()

    @Before
    fun setUp() {
        presenter = MyPetsPresenter(dogsInteractor, sharedPrefs)
        presenter.setView(view)
    }

    @Test
    fun setIsFirstPageShouldIncrementPage() {
        presenter.setIsFirstPage(false)

        assertEquals(presenter.page, 2)
    }

    @Test
    fun setIsFirstPageShouldSetPageOnOne() {
        presenter.setIsFirstPage(true)

        assertEquals(presenter.page, 1)
    }

    @Test
    fun errorFullScreenClickedNoInternet() {
        presenter.errorFullScreenClicked(false)

        verify(view).hideErrorFullScreen()
        verify(view).showProgressBar()
        verify(view).hideProgressBar()
        verify(view).showNoInternetErrorFullScreen()
        verifyNoMoreInteractions(view, dogsInteractor)
    }

    @Test
    fun errorFullScreenClickedShouldGetDogs() {
        presenter.errorFullScreenClicked(true)

        verify(view).hideErrorFullScreen()
        verify(view).showProgressBar()
        verify(dogsInteractor).getMyDogs(any(), any())
        verifyNoMoreInteractions(view, dogsInteractor)
    }


    @Test
    fun getMyDogsNoLoggedUser() {
        whenever(sharedPrefs.isLoggedIn()).thenReturn(false)
        presenter.getMyDogs(false)

        verify(sharedPrefs).isLoggedIn()
        verifyNoMoreInteractions(view, dogsInteractor)
    }

    @Test
    fun getMyDogsNoInternet() {
        whenever(sharedPrefs.isLoggedIn()).thenReturn(true)
        presenter.getMyDogs(false)

        verify(sharedPrefs).isLoggedIn()
        verify(view).hideDataLayout()
        verify(view).showNoInternetErrorFullScreen()
        verifyNoMoreInteractions(view, dogsInteractor)
    }

    @Test
    fun getMyDogsShouldSendGetDogsRequest() {
        whenever(sharedPrefs.isLoggedIn()).thenReturn(true)
        presenter.getMyDogs(true)

        verify(sharedPrefs).isLoggedIn()
        verify(view).showProgressBar()
        verify(dogsInteractor).getMyDogs(any(), any())
        verifyNoMoreInteractions(view, dogsInteractor)
    }

    @Test
    fun getMyDogsObserverOnSubscribe() {
        presenter.getMyDogsObserver().onSubscribe(TestDisposable())

        verifyZeroInteractions(view, dogsInteractor)
    }

    @Test
    fun getMyDogsObserverOnSuccessValidData() {
        presenter.getMyDogsObserver().onSuccess(mockDogsList())

        verify(view).hideProgressBar()
        verify(view).showDataLayout()
        verify(view).setMyDogs(any())
        verify(view).stopRefreshing()
        verifyNoMoreInteractions(view, dogsInteractor)
    }

    @Test
    fun getMyDogsObserverOnSuccessInvalidData() {
        presenter.getMyDogsObserver().onSuccess(listOf())

        verify(view).hideProgressBar()
        verify(view).hideDataLayout()
        verify(view).showNoDataLayout()
        verifyNoMoreInteractions(view, dogsInteractor)
    }

    @Test
    fun getMyDogsObserverOnError() {
        presenter.getMyDogsObserver().onError(mockHttpResponse(ERROR_CODE_BAD_REQUEST))

        verify(view).hideProgressBar()
        verify(view).showServerErrorFullScreen()
        verify(view).hideDataLayout()
        verify(view).stopRefreshing()
        verifyNoMoreInteractions(view, dogsInteractor)
    }

    @Test
    fun setMyDogsShouldAddMoreDogs() {
        presenter.page = 2
        presenter.setMyDogs(mockDogsList())

        verify(view).addMoreDogs(any())
        verifyNoMoreInteractions(view, dogsInteractor)
    }

    @Test
    fun onLastItemReachedNoInternet() {
        presenter.onLastItemReached(false)

        verify(view).showNoInternetErrorFullScreen()
        verifyNoMoreInteractions(view, dogsInteractor)
    }

    @Test
    fun onLastItemReachedShouldGetDogs() {
        presenter.onLastItemReached(true)

        verify(dogsInteractor).getMyDogs(any(), any())
        verifyNoMoreInteractions(view, dogsInteractor)
    }

    @Test
    fun onSwipeToRefreshNoInternet() {
        presenter.onSwipeToRefresh(false)

        verify(view).hideProgressBar()
        verify(view).stopRefreshing()
        verify(view).showNoInternetErrorFullScreen()
        verifyNoMoreInteractions(view, dogsInteractor)
    }

    @Test
    fun onSwipeToRefreshShouldGetDogs() {
        presenter.onSwipeToRefresh(true)

        verify(view).hideProgressBar()
        verify(dogsInteractor).getMyDogs(any(), any())
        verifyNoMoreInteractions(view, dogsInteractor)
    }

    @Test
    fun checkExistingFavouritesNoFavourites() {
        presenter.checkExistingFavourites(0)

        verify(view).showNoDataLayout()
        verify(view).hideDataLayout()
        verifyNoMoreInteractions(view, dogsInteractor)
    }

    @Test
    fun checkExistingFavouritesExistingFavourites() {
        presenter.checkExistingFavourites(2)

        verify(view).stopRefreshing()
        verify(view).showDataLayout()
        verify(view).hideNoDataLayout()
        verifyNoMoreInteractions(view, dogsInteractor)
    }

    @Test
    fun unSubscribe() {
        presenter.unSubscribe()

        verifyZeroInteractions(view, dogsInteractor)
    }
}