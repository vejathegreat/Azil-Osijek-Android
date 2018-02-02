package com.cobeisfresh.azil.data.models

import java.io.Serializable

/**
 * Created by Zerina Salitrezic on 23/08/2017.
 */

data class FilterModel(var filterName: String = "",
                       var filterNameKey: String = "",
                       var filterValue: String = "",
                       var filterValueKey: String = "",
                       var isChecked: Boolean = false) : Serializable