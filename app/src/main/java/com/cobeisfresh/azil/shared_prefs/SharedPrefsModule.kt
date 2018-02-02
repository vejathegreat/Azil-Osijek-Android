package com.cobeisfresh.azil.shared_prefs

import android.content.Context
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by Zerina Salitrezic on 31/07/2017.
 */

private const val SHARED_PREFS_INSTANCE_NAME = "GlobalSharedPrefs"

@Module
class SharedPrefsModule {

    @Provides
    @Singleton
    fun provideSharedPreferences(context: Context): SharedPreferences = context.getSharedPreferences(SHARED_PREFS_INSTANCE_NAME, Context.MODE_PRIVATE)

    @Provides
    @Singleton
    fun providePreferenceHelper(preferences: SharedPreferences): SharedPrefs = SharedPrefsImplementation(preferences)
}