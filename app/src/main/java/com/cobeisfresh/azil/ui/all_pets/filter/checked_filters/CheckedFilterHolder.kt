package com.cobeisfresh.azil.ui.all_pets.filter.checked_filters

import android.support.v7.widget.RecyclerView
import android.view.View
import com.cobeisfresh.azil.common.extensions.onClick
import com.cobeisfresh.azil.data.models.FilterModel
import kotlinx.android.synthetic.main.list_item_checked_filter.view.*

/**
 * Created by Zerina Salitrezic on 02/11/2017.
 */

class CheckedFilterHolder(itemView: View,
                          private val onCheckedFilterClick: (String) -> Unit) : RecyclerView.ViewHolder(itemView) {

    private var checkedFilterValueKey = ""

    init {
        itemView.onClick { onCheckedFilterClick(checkedFilterValueKey) }
    }

    fun setCheckedFilter(checkedFilterModel: FilterModel) = with(checkedFilterModel) {
        checkedFilterValueKey = filterValueKey
        itemView.checkedFilter.text = filterValue
    }
}