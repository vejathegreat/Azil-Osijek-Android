package com.cobeisfresh.azil.common.utils

import android.content.Context
import android.net.ConnectivityManager
import com.cobeisfresh.azil.App

/**
 * Created by Zerina Salitrezic on 18/08/2017.
 */

fun hasInternet(): Boolean {
    val manager: ConnectivityManager? = App.get().getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager

    return manager?.let {
        val info = manager.activeNetworkInfo

        info != null && info.isAvailable && info.isConnectedOrConnecting
    } ?: false
}