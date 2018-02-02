package com.cobeisfresh.azil.ui.all_pets.filter.checked_filters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cobeisfresh.azil.R
import com.cobeisfresh.azil.data.models.FilterModel

/**
 * Created by Zerina Salitrezic on 02/11/2017.
 */

class CheckedFiltersAdapter(private val onCheckedFilterClick: (String) -> Unit)
    : RecyclerView.Adapter<CheckedFilterHolder>() {

    private val checkedFilters: MutableList<FilterModel> = ArrayList()

    fun setCheckedFilters(checkedFilters: List<FilterModel>) {
        this.checkedFilters.clear()
        this.checkedFilters.addAll(checkedFilters)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = checkedFilters.size

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): CheckedFilterHolder {
        val view: View = LayoutInflater.from(parent?.context).inflate(R.layout.list_item_checked_filter, parent, false)
        return CheckedFilterHolder(view, onCheckedFilterClick)
    }

    override fun onBindViewHolder(holder: CheckedFilterHolder?, position: Int) {
        checkedFilters.isNotEmpty().let {
            val checkedFilterModel = checkedFilters[position]
            holder?.setCheckedFilter(checkedFilterModel)
        }
    }

    fun removeCheckedFilter(filterValueKey: String) {
        for (i in checkedFilters.indices) {
            if (checkedFilters[i].filterValueKey == filterValueKey) {
                val position = checkedFilters.indexOf(checkedFilters[i])
                checkedFilters.removeAt(position)
                notifyItemRemoved(position)
                return
            }
        }
    }
}