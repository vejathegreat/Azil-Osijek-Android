package com.cobeisfresh.azil.common.utils

import android.os.Build

/**
 * Created by Zerina Salitrezic on 01/09/2017.
 */

fun onNougatAndAbove(onValid: () -> Unit, onInvalid: () -> Unit = {}) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        onValid()
    } else {
        onInvalid()
    }
}

fun onLollipopAndAbove(onValid: () -> Unit, onInvalid: () -> Unit = {}) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        onValid()
    } else {
        onInvalid()
    }
}