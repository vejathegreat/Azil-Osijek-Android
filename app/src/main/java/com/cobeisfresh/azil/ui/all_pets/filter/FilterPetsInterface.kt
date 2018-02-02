package com.cobeisfresh.azil.ui.all_pets.filter

import com.cobeisfresh.azil.data.models.FilterData
import com.cobeisfresh.azil.data.models.FilterModel
import com.cobeisfresh.azil.presentation.base.BasePresenter

/**
 * Created by Zerina Salitrezic on 04/10/2017.
 */

interface FilterPetsInterface {

    interface View {

        fun goBack()

        fun goBack(filterData: FilterData)

        fun unCheckAllFilters()

        fun setFilters(filters: MutableList<FilterModel>)
    }

    interface Presenter : BasePresenter<View> {

        fun setFilterData(filterData: FilterData)

        fun filterCloseButtonClicked()

        fun fetchResultsButtonClicked()

        fun resetButtonClicked()

        fun backButtonClicked()

        fun addFilter(filterValueKey: String)

        fun removeFilter(filterValueKey: String)
    }
}