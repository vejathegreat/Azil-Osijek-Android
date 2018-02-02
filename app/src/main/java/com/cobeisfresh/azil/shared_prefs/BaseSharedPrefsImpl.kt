package com.cobeisfresh.azil.shared_prefs

import android.content.SharedPreferences

/**
 * Created by Zerina Salitrezic on 01/08/2017.
 */

abstract class BaseSharedPrefsImpl {

    protected abstract fun getPreferences(): SharedPreferences

    protected fun loadString(id: String): String = getPreferences().getString(id, "")

    protected fun loadInt(id: String): Int = getPreferences().getInt(id, -1)

    protected fun loadBoolean(id: String): Boolean = getPreferences().getBoolean(id, false)

    protected fun loadFloat(id: String): Float = getPreferences().getFloat(id, -1f)

    protected fun loadLong(id: String): Long = getPreferences().getLong(id, -1)

    protected fun save(id: String, value: String) = getPreferences().edit().putString(id, value).apply()

    protected fun save(id: String, value: Boolean) = getPreferences().edit().putBoolean(id, value).apply()

    protected fun save(id: String, value: Int) = getPreferences().edit().putInt(id, value).apply()

    protected fun save(id: String, value: Float) = getPreferences().edit().putFloat(id, value).apply()

    protected fun save(id: String, value: Long) = getPreferences().edit().putLong(id, value).apply()
}