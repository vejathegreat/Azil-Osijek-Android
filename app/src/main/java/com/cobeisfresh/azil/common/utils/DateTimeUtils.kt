package com.cobeisfresh.azil.common.utils

import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Zerina Salitrezic on 24/08/2017.
 */

private const val DATE_FORMAT_IMAGE = "yyyyMMdd_HHmmss"

fun getCurrentTimeStringFormat(): String {
    val dateFormat = SimpleDateFormat(DATE_FORMAT_IMAGE, Locale.getDefault())
    return dateFormat.format(Date())
}