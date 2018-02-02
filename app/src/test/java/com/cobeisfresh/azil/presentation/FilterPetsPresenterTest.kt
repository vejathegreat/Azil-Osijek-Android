package com.cobeisfresh.azil.presentation

import com.cobeisfresh.azil.SMALL_VALUE
import com.cobeisfresh.azil.mockCheckedFilters
import com.cobeisfresh.azil.mockFilterDataFiltersExist
import com.cobeisfresh.azil.ui.all_pets.filter.FilterPetsInterface
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.verifyNoMoreInteractions
import org.junit.Before
import org.junit.Test

/**
 * Created by Zerina Salitrezic on 03/11/2017.
 */

class FilterPetsPresenterTest {

    private lateinit var presenter: FilterPetsPresenter

    private val view: FilterPetsInterface.View = mock()

    @Before
    fun setUp() {
        presenter = FilterPetsPresenter()
        presenter.setView(view)
    }

    @Test
    fun setFilterDataFiltersExist() {
        presenter.setFilterData(mockFilterDataFiltersExist())

        verify(view).setFilters(any())
        verifyNoMoreInteractions(view)
    }

    @Test
    fun setFilterDataNoFilters() {
//        presenter.setFilterData(mockFilterDataNoFilters())
//
//        verify(view).setFilters(any())
//        verifyNoMoreInteractions(view)
    }

    @Test
    fun addFilter() {
        presenter.addFilter(SMALL_VALUE)

        verifyNoMoreInteractions(view)
    }

    @Test
    fun removeFilter() {
        presenter.filters = mockCheckedFilters()
        presenter.removeFilter(SMALL_VALUE)

        verifyNoMoreInteractions(view)
    }

    @Test
    fun fetchResultsButtonClickedShouldGoBackWithResult() {
        presenter.fetchResultsButtonClicked()

        verify(view).goBack(any())
        verifyNoMoreInteractions(view)
    }

    @Test
    fun resetButtonClickedShouldUnCheckAllFilters() {
        presenter.filters = mockCheckedFilters()
        presenter.resetButtonClicked()

        verify(view).unCheckAllFilters()
        verifyNoMoreInteractions(view)
    }

    @Test
    fun filterCloseButtonClickedShouldGoBack() {
        presenter.filterCloseButtonClicked()

        verify(view).goBack()
        verifyNoMoreInteractions(view)
    }

    @Test
    fun backButtonClickedShouldGoBack() {
        presenter.backButtonClicked()

        verify(view).goBack()
        verifyNoMoreInteractions(view)
    }
}