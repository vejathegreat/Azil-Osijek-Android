package com.cobeisfresh.azil.presentation

import com.cobeisfresh.azil.common.extensions.isValid
import com.cobeisfresh.azil.common.utils.getFilters
import com.cobeisfresh.azil.data.models.FilterData
import com.cobeisfresh.azil.data.models.FilterModel
import com.cobeisfresh.azil.ui.all_pets.filter.FilterPetsInterface

/**
 * Created by Zerina Salitrezic on 04/10/2017.
 */

class FilterPetsPresenter : FilterPetsInterface.Presenter {

    private lateinit var view: FilterPetsInterface.View
    internal var filters: MutableList<FilterModel> = mutableListOf()

    override fun setView(view: FilterPetsInterface.View) {
        this.view = view
    }

    override fun setFilterData(filterData: FilterData) {
        filters = if (filterData.filters.isNotEmpty()) {
            filterData.filters
        } else {
            getFilters()
        }
        view.setFilters(filters)
    }

    override fun addFilter(filterValueKey: String) {
        filters
                .filter { it.filterValueKey == filterValueKey }
                .forEach { it.isChecked = true }
    }

    override fun removeFilter(filterValueKey: String) {
        if (filterValueKey.isValid() && filters.isNotEmpty()) {
            val filtered = filters.filter { it.filterValueKey == filterValueKey }
            for (filter in filtered) {
                filter.isChecked = false
            }
        }
    }

    override fun fetchResultsButtonClicked() {
        view.goBack(FilterData(filters))
    }

    override fun resetButtonClicked() {
        for (filter in filters) {
            filter.isChecked = false
        }
        view.unCheckAllFilters()
    }

    override fun filterCloseButtonClicked() = view.goBack()

    override fun backButtonClicked() = view.goBack()
}