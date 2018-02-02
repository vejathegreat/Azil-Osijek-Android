package com.cobeisfresh.azil.common.utils

import android.text.SpannableString
import android.text.style.RelativeSizeSpan
import com.cobeisfresh.azil.App
import com.cobeisfresh.azil.R
import com.cobeisfresh.azil.common.constants.KEY_AGE
import com.cobeisfresh.azil.common.constants.KEY_GENDER
import com.cobeisfresh.azil.common.constants.KEY_SIZE
import com.cobeisfresh.azil.common.constants.NUMBER_OF_ITEMS
import com.cobeisfresh.azil.common.extensions.isValid
import com.cobeisfresh.azil.data.models.FilterModel
import java.util.*

/**
 * Created by Zerina Salitrezic on 01/08/2017.
 */

private const val COMMA = ","
private const val KEY_PAGE = "page"
private const val KEY_TAKE = "take"
private const val KEY_SEARCH = "search"

fun validateString(value: String, onValid: (String) -> Unit, onInvalid: () -> Unit = {}) = if (value.isValid()) onValid(value) else onInvalid()

fun isEmpty(vararg strings: String?): Boolean = strings.any { toCheck -> toCheck == null || toCheck.isEmpty() || toCheck.trim { it <= ' ' }.isEmpty() }

fun getPagesQueryMap(page: Int, take: Int = NUMBER_OF_ITEMS): Map<String, Int> {
    val queryMap = HashMap<String, Int>()
    queryMap[KEY_PAGE] = page
    queryMap[KEY_TAKE] = take
    return queryMap
}

fun getSearchQueryMap(searchWord: String): Map<String, String> {
    val queryMap = HashMap<String, String>()
    validateString(searchWord, onValid = {
        queryMap[KEY_SEARCH] = searchWord
    })
    return queryMap
}

fun getString(resource: Int): String = App.get().resources.getString(resource)

fun getQuoteFormat(): SpannableString {
    val welcomeQuoteLength = getString(R.string.welcome_quote).length
    val authorQuoteLength = getString(R.string.welcome_quote_author).length
    val welcomeQuoteFormat = String.format(Locale.getDefault(), getString(R.string.welcome_quote_format), getString(R.string.welcome_quote_author))

    val spannableString = SpannableString(welcomeQuoteFormat)
    spannableString.setSpan(RelativeSizeSpan(0.6f), welcomeQuoteLength, welcomeQuoteLength + authorQuoteLength + 1, 0)
    return spannableString
}

fun getFilterQueryMap(gender: String, age: String, size: String): Map<String, String> {
    val queryMap = HashMap<String, String>()
    validateString(gender, onValid = { queryMap[KEY_GENDER] = gender })
    validateString(age, onValid = { queryMap[KEY_AGE] = age })
    validateString(size, onValid = { queryMap[KEY_SIZE] = size })
    return queryMap
}

fun getFilterFormat(filterKey: String, checkedFilters: List<FilterModel>): String {
    val keys = checkedFilters
            .filter { it.filterNameKey == filterKey }
            .map { it.filterValueKey }

    if (!keys.isEmpty()) {
        val builder = StringBuilder()
        for (i in 0..keys.size - 2) {
            builder.append(keys[i]).append(COMMA)
        }
        builder.append(keys[keys.size - 1])
        return builder.toString()
    }
    return ""
}