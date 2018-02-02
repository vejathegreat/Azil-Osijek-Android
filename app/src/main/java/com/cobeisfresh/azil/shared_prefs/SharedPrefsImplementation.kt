package com.cobeisfresh.azil.shared_prefs

import android.content.SharedPreferences
import com.cobeisfresh.azil.common.constants.EMAIL

/**
 * Created by Zerina Salitrezic on 01/08/2017.
 */

private const val TOKEN = "token"
private const val IS_LOGGED_IN = "is_logged_in"
private const val IS_WELCOME_SHOWN = "is_welcome_shown"

class SharedPrefsImplementation(private val sharedPreferences: SharedPreferences) : BaseSharedPrefsImpl(), SharedPrefs {

    override fun getPreferences(): SharedPreferences = sharedPreferences

    override fun setLoggedIn(isLoggedIn: Boolean) = save(IS_LOGGED_IN, isLoggedIn)

    override fun isLoggedIn(): Boolean = loadBoolean(IS_LOGGED_IN)

    override fun setToken(token: String) = save(TOKEN, token)

    override fun getToken(): String = loadString(TOKEN)

    override fun setEmail(email: String) = save(EMAIL, email)

    override fun getEmail(): String = loadString(EMAIL)

    override fun setWelcomeShown() = save(IS_WELCOME_SHOWN, true)

    override fun isWelcomeShown(): Boolean = loadBoolean(IS_WELCOME_SHOWN)
}