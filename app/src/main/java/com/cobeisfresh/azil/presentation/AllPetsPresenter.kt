package com.cobeisfresh.azil.presentation

import com.cobeisfresh.azil.common.constants.*
import com.cobeisfresh.azil.common.utils.validateString
import com.cobeisfresh.azil.data.enums.DogsOption
import com.cobeisfresh.azil.data.models.DogModel
import com.cobeisfresh.azil.data.models.FilterData
import com.cobeisfresh.azil.data.models.FilterModel
import com.cobeisfresh.azil.data.response.DogsResponse
import com.cobeisfresh.azil.network_interaction.DogsInteractor
import com.cobeisfresh.azil.ui.all_pets.AllPetsInterface
import io.reactivex.SingleObserver
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import retrofit2.HttpException
import java.io.IOException

/**
 * Created by Zerina Salitrezic on 02/08/2017.
 */

class AllPetsPresenter(private val dogsInteractor: DogsInteractor) : AllPetsInterface.Presenter {

    private lateinit var view: AllPetsInterface.View
    private val compositeDisposable = CompositeDisposable()
    internal var filters: MutableList<FilterModel> = mutableListOf()
    internal var searchTerm = ""
    internal var page = 1
    internal var dogsOption = DogsOption.ALL

    override fun setView(view: AllPetsInterface.View) {
        this.view = view
    }

    override fun setIsFirstPage(shouldReset: Boolean) {
        page = if (shouldReset) 1 else page + 1
    }

    override fun searchInputChanged(searchTerm: String) {
        this.searchTerm = searchTerm
        validateString(value = searchTerm,
                onValid = { view.showClearIcon() },
                onInvalid = { view.hideClearIcon() })
    }

    override fun getDogs() {
        view.showProgressBar()
        getAllDogs()
    }

    private fun getAllDogs() {
        dogsOption = DogsOption.ALL
        dogsInteractor.getDogs(page = page, observer = getDogsObserver())
    }

    private fun filterAllDogs() {
        dogsOption = DogsOption.FILTER
        dogsInteractor.getDogs(page = page, filters = filters, observer = getDogsObserver())
    }

    override fun searchDogs() {
        view.hideKeyboard()

        validateString(value = searchTerm, onValid = {
            setIsFirstPage(true)
            removeCheckedFilters()
            view.hideCheckedFiltersRecyclerView()
            view.showProgressBar()
            searchAllDogs()
        })
    }

    internal fun searchAllDogs() {
        dogsOption = DogsOption.SEARCH
        dogsInteractor.getDogs(page = page, search = searchTerm, observer = getDogsObserver())
    }

    internal fun getDogsObserver(): SingleObserver<DogsResponse> {
        return object : SingleObserver<DogsResponse> {
            override fun onSubscribe(d: Disposable?) {
                compositeDisposable.add(d)
            }

            override fun onSuccess(dogsResponse: DogsResponse) {
                view.hideProgressBar()
                view.hideErrorFullScreen()
                when (dogsOption) {
                    DogsOption.SEARCH -> handleSearchOptionOnSuccess()
                    DogsOption.FILTER -> handleFilterOptionOnSuccess()
                    else -> {
                    }
                }

                setDogs(dogsResponse.dogs)
            }

            override fun onError(e: Throwable?) {
                view.hideProgressBar()
                view.stopRefreshing()
                handleError(e)
                if (dogsOption == DogsOption.SEARCH) {
                    handleSearchOptionOnError()
                }
            }
        }
    }

    private fun handleSearchOptionOnSuccess() {
        view.hideDimView()
        view.hideSearchInput()
        view.showEnteredSearch(searchTerm)
    }

    private fun handleSearchOptionOnError() {
        view.hideDimView()
        view.hideSearchInput()
    }

    private fun handleFilterOptionOnSuccess() {
        view.clearSearchInput()
        view.hideSearchInput()
        view.hideEnteredSearch()
        view.hideDimView()
        view.showCheckedFiltersList()
    }

    internal fun handleError(error: Throwable?) {
        if (error is HttpException) {
            view.showServerErrorFullScreen()
        } else if (error is IOException) {
            view.showNoInternetErrorFullScreen()
        }
    }

    internal fun setDogs(dogs: List<DogModel>) {
        if (page == 1) {
            view.setDogs(dogs)
            view.stopRefreshing()
        } else {
            view.addMoreDogs(dogs)
        }
    }

    override fun filterDogs(filterData: FilterData) {
        filters = filterData.filters
        checkIsAnyFilterChecked()
        handleFeatureSelectedEvents()
    }

    private fun checkIsAnyFilterChecked() {
        val isAnyFilterChecked = filters.any { it.isChecked }

        if (isAnyFilterChecked) {
            view.setCheckedFilters(filters)
            filterAllDogs()
        } else {
            dogsOption = DogsOption.ALL
            view.hideFilterSearchLayout()
            view.hideCheckedFiltersRecyclerView()
            getAllDogs()
        }
    }

    private fun handleFeatureSelectedEvents() {
        filters
                .filter { it.isChecked }
                .forEach {
                    when (it.filterValueKey) {
                        MALE -> view.sendMaleFeatureSelectedEvent()
                        FEMALE -> view.sendFemaleFeatureSelectedEvent()
                        PUPPY -> view.sendPuppiesFeatureSelectedEvent()
                        YOUNG -> view.sendYoungFeatureSelectedEvent()
                        ADULT -> view.sendAdultFeatureSelectedEvent()
                        SMALL -> view.sendSmallFeatureSelectedEvent()
                        MEDIUM -> view.sendMidiFeatureSelectedEvent()
                        LARGE -> view.sendBigFeatureSelectedEvent()
                    }
                }
    }

    private fun removeCheckedFilters() = filters.forEach { it.isChecked = false }

    override fun filterClicked() = view.openFilterPetsActivity(FilterData(filters))

    override fun deleteChosenFilter(filterValueKey: String) {
        setIsFirstPage(true)
        filters
                .filter { it.filterValueKey == filterValueKey }
                .forEach { it.isChecked = false }
        view.removeCheckedFilter(filterValueKey)
        checkIsAnyFilterChecked()
    }

    override fun onSwipeToRefresh() {
        view.hideProgressBar()
        handleGetDogsOption()
    }

    override fun addMoreDogs() = handleGetDogsOption()

    override fun errorFullScreenClicked() {
        view.showProgressBar()
        handleGetDogsOption()
    }

    private fun handleGetDogsOption() = when (dogsOption) {
        DogsOption.FILTER -> filterAllDogs()
        DogsOption.SEARCH -> searchAllDogs()
        DogsOption.ALL -> getAllDogs()
    }

    override fun searchClicked() {
        view.sendSearchDogsByNameFeatureSelectedEvent()
        view.showSearchInput()
        view.requestFocusSearch()
        view.showDimView()
    }

    override fun cancelSearchClicked() {
        dogsOption = DogsOption.ALL
        view.clearSearchInput()
        view.hideFilterSearchLayout()
        view.hideEnteredSearch()
        view.getAllDogs()
    }

    override fun clearSearchClicked() = view.clearSearchInput()

    override fun dimViewClicked() {
        view.hideSearchInput()
        view.hideKeyboard()
        view.hideDimView()
    }

    override fun onDogClick(dogId: String) = view.openPetDetailsActivity(dogId)

    override fun unSubscribe() = compositeDisposable.clear()
}