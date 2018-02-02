package com.cobeisfresh.azil.ui.all_pets.filter.filters

import android.support.v7.widget.RecyclerView
import android.view.View
import com.cobeisfresh.azil.R
import com.cobeisfresh.azil.common.extensions.gone
import com.cobeisfresh.azil.common.extensions.onCheckedChange
import com.cobeisfresh.azil.common.extensions.visible
import com.cobeisfresh.azil.common.utils.getString
import com.cobeisfresh.azil.data.models.FilterModel
import kotlinx.android.synthetic.main.list_item_filter.view.*

/**
 * Created by Zerina Salitrezic on 18/08/2017.
 */

class FiltersHolder(itemView: View,
                    private val onAddFilter: (String) -> Unit,
                    private val onRemoveFilter: (String) -> Unit) : RecyclerView.ViewHolder(itemView) {

    var filter: FilterModel = FilterModel()

    init {
        addCheckedChangeListener()
    }

    private fun addCheckedChangeListener() {
        itemView.filterValueCheckBox.onCheckedChange { isChecked ->
            val filterValueKey = filter.filterValueKey
            if (isChecked) {
                onAddFilter(filterValueKey)
            } else {
                onRemoveFilter(filterValueKey)
            }
        }
    }

    fun setFilterData(filterModel: FilterModel) = with(filterModel) {
        filter = this
        itemView.filterName.text = filterName
        itemView.filterValue.text = filterValue
        itemView.filterValueCheckBox.isChecked = isChecked
        setFilterDividerVisibility(filterValue)
    }

    private fun setFilterDividerVisibility(filterValue: String) {
        if (filterValue == getString(R.string.filter_female) || filterValue == getString(R.string.filter_adult) || filterValue == getString(R.string.filter_big)) {
            itemView.filterDivider.visible()
        } else {
            itemView.filterDivider.gone()
        }
    }

    fun setFilterNameVisibility(isFilterNameVisible: Boolean) = if (isFilterNameVisible) {
        itemView.filterName.visible()
    } else {
        itemView.filterName.gone()
    }
}