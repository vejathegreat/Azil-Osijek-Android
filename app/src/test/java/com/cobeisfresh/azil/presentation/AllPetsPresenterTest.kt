package com.cobeisfresh.azil.presentation

import com.cobeisfresh.azil.*
import com.cobeisfresh.azil.data.enums.DogsOption
import com.cobeisfresh.azil.data.models.FilterData
import com.cobeisfresh.azil.network_interaction.DogsInteractor
import com.cobeisfresh.azil.ui.all_pets.AllPetsInterface
import com.cobeisfresh.azil.utils.TestDisposable
import com.nhaarman.mockito_kotlin.*
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import retrofit2.HttpException
import java.io.IOException

/**
 * Created by Zerina Salitrezic on 03/11/2017.
 */

class AllPetsPresenterTest {

    private lateinit var presenter: AllPetsPresenter

    private val view: AllPetsInterface.View = mock()
    private val dogsInteractor: DogsInteractor = mock()

    @Before
    fun setUp() {
        presenter = AllPetsPresenter(dogsInteractor)
        presenter.setView(view)
    }

    @Test
    fun setIsFirstPageShouldSetPageToFirst() {
        presenter.setIsFirstPage(true)

        assertEquals(presenter.page, 1)
    }

    @Test
    fun setIsFirstPageShouldSetPageToSecond() {
        presenter.setIsFirstPage(false)

        assertEquals(presenter.page, 2)
    }

    @Test
    fun searchInputChangedShouldSetSearchTerm() {
        presenter.searchInputChanged(SEARCH_TERM)

        assertEquals(presenter.searchTerm, SEARCH_TERM)
    }

    @Test
    fun searchInputChangedValidSearchTerm() {
        presenter.searchInputChanged(SEARCH_TERM)

        verify(view).showClearIcon()
        verifyNoMoreInteractions(view, dogsInteractor)
    }

    @Test
    fun searchInputChangedInvalidSearchTerm() {
        presenter.searchInputChanged(EMPTY_TEXT)

        verify(view).hideClearIcon()
        verifyNoMoreInteractions(view, dogsInteractor)
    }

    @Test
    fun getDogsShouldSendGetDogsRequest() {
        presenter.getDogs()

        verify(view).showProgressBar()
        verify(dogsInteractor).getDogs(any(), any(), any(), any())
        verifyNoMoreInteractions(view, dogsInteractor)
    }

    @Test
    fun getDogsObserverOnSubscribe() {
        presenter.getDogsObserver().onSubscribe(TestDisposable())

        verifyZeroInteractions(view, dogsInteractor)
    }

    @Test
    fun getDogsObserverOnSuccessNoOption() {
        presenter.getDogsObserver().onSuccess(mockDogsResponse())

        verify(view).hideProgressBar()
        verify(view).hideErrorFullScreen()
        verify(view).setDogs(any())
        verify(view).stopRefreshing()
        verifyNoMoreInteractions(view, dogsInteractor)
    }

    @Test
    fun getDogsObserverOnSuccessSearchOption() {
        presenter.dogsOption = DogsOption.SEARCH
        presenter.getDogsObserver().onSuccess(mockDogsResponse())

        verify(view).hideProgressBar()
        verify(view).hideErrorFullScreen()
        verify(view).hideDimView()
        verify(view).hideSearchInput()
        verify(view).showEnteredSearch(any())
        verify(view).setDogs(any())
        verify(view).stopRefreshing()
        verifyNoMoreInteractions(view, dogsInteractor)
    }

    @Test
    fun getDogsObserverOnSuccessFilterOption() {
        presenter.dogsOption = DogsOption.FILTER
        presenter.getDogsObserver().onSuccess(mockDogsResponse())

        verify(view).hideProgressBar()
        verify(view).hideErrorFullScreen()
        verify(view).clearSearchInput()
        verify(view).hideSearchInput()
        verify(view).hideEnteredSearch()
        verify(view).hideDimView()
        verify(view).showCheckedFiltersList()
        verify(view).setDogs(any())
        verify(view).stopRefreshing()
        verifyNoMoreInteractions(view, dogsInteractor)
    }

    @Test
    fun getDogsObserverOnErrorServerError() {
        val exception = mock<HttpException>()
        whenever(exception.code()).thenReturn(ERROR_CODE_BAD_REQUEST)
        presenter.getDogsObserver().onError(exception)

        verify(view).hideProgressBar()
        verify(view).stopRefreshing()
        verify(view).showServerErrorFullScreen()
        verifyNoMoreInteractions(view, dogsInteractor)
    }

    @Test
    fun getDogsObserverOnErrorServerErrorSearchOption() {
        presenter.dogsOption = DogsOption.SEARCH
        val exception = mock<HttpException>()
        whenever(exception.code()).thenReturn(ERROR_CODE_BAD_REQUEST)
        presenter.getDogsObserver().onError(exception)

        verify(view).hideProgressBar()
        verify(view).stopRefreshing()
        verify(view).showServerErrorFullScreen()
        verify(view).hideDimView()
        verify(view).hideSearchInput()
        verifyNoMoreInteractions(view, dogsInteractor)
    }

    @Test
    fun getDogsObserverOnErrorNoInternetError() {
        presenter.getDogsObserver().onError(IOException())

        verify(view).hideProgressBar()
        verify(view).stopRefreshing()
        verify(view).showNoInternetErrorFullScreen()
        verifyNoMoreInteractions(view, dogsInteractor)
    }

    @Test
    fun setDogsShouldSetDogs() {
        presenter.setDogs(mockDogsList())

        verify(view).setDogs(any())
        verify(view).stopRefreshing()
        verifyNoMoreInteractions(view, dogsInteractor)
    }

    @Test
    fun setDogsShouldAddMoreDogs() {
        presenter.page = 2
        presenter.setDogs(mockDogsList())

        verify(view).addMoreDogs(any())
        verifyNoMoreInteractions(view, dogsInteractor)
    }

    @Test
    fun searchDogsByFilterWhenFiltersUnchecked() {
        presenter.filterDogs(FilterData(mockUncheckedFilters()))

        verify(view).hideFilterSearchLayout()
        verify(view).hideCheckedFiltersRecyclerView()
        verify(dogsInteractor).getDogs(any(), any(), any(), any())
        verifyNoMoreInteractions(view, dogsInteractor)
    }

    @Test
    fun setFiltersCheckedFiltersWhenFiltersChecked() {
        presenter.filterDogs(FilterData(mockCheckedFilters()))

        verify(view).setCheckedFilters(any())
        verify(dogsInteractor).getDogs(any(), any(), any(), any())
        verify(view).sendMaleFeatureSelectedEvent()
        verify(view).sendFemaleFeatureSelectedEvent()
        verify(view).sendPuppiesFeatureSelectedEvent()
        verify(view).sendYoungFeatureSelectedEvent()
        verify(view).sendAdultFeatureSelectedEvent()
        verify(view).sendSmallFeatureSelectedEvent()
        verify(view).sendMidiFeatureSelectedEvent()
        verify(view).sendBigFeatureSelectedEvent()
        verifyNoMoreInteractions(view, dogsInteractor)
    }


    @Test
    fun searchDogsBySearchInputShouldSendGetDogsByNameRequest() {
        presenter.searchTerm = SEARCH_TERM
        presenter.searchDogs()

        verify(view).hideKeyboard()
        verify(view).hideCheckedFiltersRecyclerView()
        verify(view).showProgressBar()
        verify(dogsInteractor).getDogs(any(), any(), any(), any())
        verifyNoMoreInteractions(view, dogsInteractor)
    }

    @Test
    fun searchMoreDogsBySearchInputShouldSendGetDogsRequest() {
        presenter.searchTerm = SEARCH_TERM
        presenter.searchAllDogs()

        verify(dogsInteractor).getDogs(any(), any(), any(), any())
        verifyNoMoreInteractions(view, dogsInteractor)
    }

    @Test
    fun onSwipeToRefreshFilterOption() {
        presenter.dogsOption = DogsOption.FILTER
        presenter.onSwipeToRefresh()

        verify(view).hideProgressBar()
        verify(dogsInteractor).getDogs(any(), any(), any(), any())
        verifyNoMoreInteractions(view, dogsInteractor)
    }

    @Test
    fun onSwipeToRefreshSearchOption() {
        presenter.dogsOption = DogsOption.SEARCH
        presenter.searchTerm = SEARCH_TERM
        presenter.onSwipeToRefresh()

        verify(view).hideProgressBar()
        verify(dogsInteractor).getDogs(any(), any(), any(), any())
        verifyNoMoreInteractions(view, dogsInteractor)
    }

    @Test
    fun onSwipeToRefreshNoOption() {
        presenter.onSwipeToRefresh()

        verify(view).hideProgressBar()
        verify(dogsInteractor).getDogs(any(), any(), any(), any())
        verifyNoMoreInteractions(view, dogsInteractor)
    }

    @Test
    fun filterClickedShouldOpenFilterPetsActivity() {
        presenter.filterClicked()

        verify(view).openFilterPetsActivity(any())
        verifyNoMoreInteractions(view, dogsInteractor)
    }

    @Test
    fun checkedFilterClickShouldRemoveCheckedFilter() {
        presenter.filters = mockCheckedFilters()
        presenter.deleteChosenFilter(SMALL_VALUE)

        verify(view).removeCheckedFilter(any())
        verify(view).setCheckedFilters(any())
        verify(dogsInteractor).getDogs(any(), any(), any(), any())
        verifyNoMoreInteractions(view, dogsInteractor)
    }

    @Test
    fun addMoreDogsFilterOption() {
        presenter.dogsOption = DogsOption.FILTER
        presenter.addMoreDogs()

        verify(dogsInteractor).getDogs(any(), any(), any(), any())
        verifyNoMoreInteractions(view, dogsInteractor)
    }

    @Test
    fun addMoreDogsSearchOption() {
        presenter.dogsOption = DogsOption.SEARCH
        presenter.searchTerm = SEARCH_TERM
        presenter.addMoreDogs()

        verify(dogsInteractor).getDogs(any(), any(), any(), any())
        verifyNoMoreInteractions(view, dogsInteractor)
    }

    @Test
    fun addMoreDogsNoOption() {
        presenter.addMoreDogs()

        verify(dogsInteractor).getDogs(any(), any(), any(), any())
        verifyNoMoreInteractions(view, dogsInteractor)
    }

    @Test
    fun searchClickedShouldShowSearchInput() {
        presenter.searchClicked()

        verify(view).sendSearchDogsByNameFeatureSelectedEvent()
        verify(view).showSearchInput()
        verify(view).requestFocusSearch()
        verify(view).showDimView()
        verifyNoMoreInteractions(view, dogsInteractor)
    }

    @Test
    fun cancelSearchClickedShouldHideEnteredSearch() {
        presenter.cancelSearchClicked()

        verify(view).clearSearchInput()
        verify(view).hideFilterSearchLayout()
        verify(view).hideEnteredSearch()
        verify(view).getAllDogs()
        verifyNoMoreInteractions(view, dogsInteractor)
    }

    @Test
    fun clearSearchClickedShouldClearSearchInput() {
        presenter.clearSearchClicked()

        verify(view).clearSearchInput()
        verifyNoMoreInteractions(view, dogsInteractor)
    }

    @Test
    fun dimViewClickedShouldHideDimView() {
        presenter.dimViewClicked()

        verify(view).hideSearchInput()
        verify(view).hideKeyboard()
        verify(view).hideDimView()
        verifyNoMoreInteractions(view, dogsInteractor)
    }

    @Test
    fun errorFullScreenClickedShouldSendGetDogsRequest() {
        presenter.errorFullScreenClicked()

        verify(view).showProgressBar()
        verify(dogsInteractor).getDogs(any(), any(), any(), any())
        verifyNoMoreInteractions(view, dogsInteractor)
    }

    @Test
    fun onDogClickShouldOpenPetDetailsActivity() {
        presenter.onDogClick(ID)

        verify(view).openPetDetailsActivity(any())
        verifyNoMoreInteractions(view, dogsInteractor)
    }

    @Test
    fun unSubscribe() {
        presenter.unSubscribe()

        verifyZeroInteractions(view, dogsInteractor)
    }
}