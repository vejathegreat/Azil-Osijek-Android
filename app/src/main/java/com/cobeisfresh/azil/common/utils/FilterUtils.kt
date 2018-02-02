package com.cobeisfresh.azil.common.utils

import com.cobeisfresh.azil.R
import com.cobeisfresh.azil.common.constants.*
import com.cobeisfresh.azil.data.models.FilterModel
import java.util.*

/**
 * Created by Zerina Salitrezic on 23/08/2017.
 */

fun getFilters(): MutableList<FilterModel> {
    val filters = ArrayList<FilterModel>()
    filters.add(FilterModel(getString(R.string.filter_gender), KEY_GENDER, getString(R.string.filter_male), MALE))
    filters.add(FilterModel(getString(R.string.filter_gender), KEY_GENDER, getString(R.string.filter_female), FEMALE))
    filters.add(FilterModel(getString(R.string.filter_age), KEY_AGE, getString(R.string.filter_puppies), PUPPY))
    filters.add(FilterModel(getString(R.string.filter_age), KEY_AGE, getString(R.string.filter_young), YOUNG))
    filters.add(FilterModel(getString(R.string.filter_age), KEY_AGE, getString(R.string.filter_adult), ADULT))
    filters.add(FilterModel(getString(R.string.filter_size), KEY_SIZE, getString(R.string.filter_small), SMALL))
    filters.add(FilterModel(getString(R.string.filter_size), KEY_SIZE, getString(R.string.filter_mid), MEDIUM))
    filters.add(FilterModel(getString(R.string.filter_size), KEY_SIZE, getString(R.string.filter_big), LARGE))
    return filters
}