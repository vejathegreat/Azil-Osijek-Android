package com.cobeisfresh.azil.shared_prefs

/**
 * Created by Zerina Salitrezic on 01/08/2017.
 */

interface SharedPrefs {

    fun setLoggedIn(isLoggedIn: Boolean)

    fun isLoggedIn(): Boolean

    fun setToken(token: String)

    fun getToken(): String

    fun setEmail(email: String)

    fun getEmail(): String

    fun setWelcomeShown()

    fun isWelcomeShown(): Boolean
}