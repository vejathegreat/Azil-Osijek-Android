package com.cobeisfresh.azil.ui.all_pets.filter.filters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cobeisfresh.azil.R
import com.cobeisfresh.azil.data.models.FilterModel

/**
 * Created by Zerina Salitrezic on 18/08/2017.
 */

class FiltersAdapter(private val onAddFilter: (String) -> Unit,
                     private val onRemoveFilter: (String) -> Unit) : RecyclerView.Adapter<FiltersHolder>() {

    private val filters: MutableList<FilterModel> = ArrayList()

    fun setFilters(filters: List<FilterModel>) {
        this.filters.clear()
        this.filters.addAll(filters)
        notifyDataSetChanged()
    }

    override fun getItemCount() = filters.size

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): FiltersHolder {
        val view: View = LayoutInflater.from(parent?.context).inflate(R.layout.list_item_filter, parent, false)
        return FiltersHolder(view, onAddFilter, onRemoveFilter)
    }

    override fun onBindViewHolder(holder: FiltersHolder?, position: Int) {
        filters.isNotEmpty().let {
            val filterModel = filters[position]
            val currentKey = filterModel.filterNameKey
            var isFilterNameVisible = false
            if (position == 0) {
                isFilterNameVisible = true
            } else if (position > 0) {
                val previousKey = filters[position - 1].filterNameKey
                isFilterNameVisible = previousKey != currentKey
            }

            holder?.setFilterData(filterModel)
            holder?.setFilterNameVisibility(isFilterNameVisible)
        }
    }

    fun unCheckAllFilters() {
        for (filter in filters) {
            filter.isChecked = false
            notifyDataSetChanged()
        }
    }
}