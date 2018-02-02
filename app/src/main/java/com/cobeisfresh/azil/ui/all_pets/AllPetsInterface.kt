package com.cobeisfresh.azil.ui.all_pets

import com.cobeisfresh.azil.data.models.DogModel
import com.cobeisfresh.azil.data.models.FilterData
import com.cobeisfresh.azil.data.models.FilterModel
import com.cobeisfresh.azil.presentation.base.BasePresenter

/**
 * Created by Zerina Salitrezic on 02/08/2017.
 */

interface AllPetsInterface {

    interface View {

        fun showProgressBar()

        fun hideProgressBar()

        fun setDogs(data: List<DogModel>)

        fun addMoreDogs(data: List<DogModel>)

        fun showServerErrorFullScreen()

        fun showNoInternetErrorFullScreen()

        fun hideErrorFullScreen()

        fun stopRefreshing()

        fun showSearchInput()

        fun hideSearchInput()

        fun clearSearchInput()

        fun getAllDogs()

        fun showClearIcon()

        fun hideClearIcon()

        fun hideKeyboard()

        fun requestFocusSearch()

        fun openFilterPetsActivity(filterData: FilterData)

        fun showDimView()

        fun hideDimView()

        fun hideErrorLayout()

        fun showEnteredSearch(searchTerm: String)

        fun hideEnteredSearch()

        fun hideFilterSearchLayout()

        fun removeCheckedFilter(filterValueKey: String)

        fun hideCheckedFiltersRecyclerView()

        fun showCheckedFiltersList()

        fun setCheckedFilters(filters: MutableList<FilterModel>)

        fun sendSearchDogsByNameFeatureSelectedEvent()

        fun sendMaleFeatureSelectedEvent()

        fun sendFemaleFeatureSelectedEvent()

        fun sendPuppiesFeatureSelectedEvent()

        fun sendYoungFeatureSelectedEvent()

        fun sendAdultFeatureSelectedEvent()

        fun sendSmallFeatureSelectedEvent()

        fun sendMidiFeatureSelectedEvent()

        fun sendBigFeatureSelectedEvent()

        fun openPetDetailsActivity(dogId: String)
    }

    interface Presenter : BasePresenter<View> {

        fun getDogs()

        fun searchDogs()

        fun filterDogs(filterData: FilterData)

        fun searchClicked()

        fun filterClicked()

        fun clearSearchClicked()

        fun searchInputChanged(searchTerm: String)

        fun cancelSearchClicked()

        fun addMoreDogs()

        fun onSwipeToRefresh()

        fun setIsFirstPage(shouldReset: Boolean)

        fun dimViewClicked()

        fun errorFullScreenClicked()

        fun deleteChosenFilter(filterValueKey: String)

        fun onDogClick(dogId: String)

        fun unSubscribe()
    }
}