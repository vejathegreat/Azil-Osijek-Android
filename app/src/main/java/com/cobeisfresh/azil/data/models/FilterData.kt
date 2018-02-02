package com.cobeisfresh.azil.data.models

import java.io.Serializable

/**
 * Created by Zerina Salitrezic on 06/10/2017.
 */

data class FilterData(val filters: MutableList<FilterModel> = arrayListOf()) : Serializable