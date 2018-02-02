package com.cobeisfresh.azil

import android.content.Context
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by Zerina Salitrezic on 31/07/2017.
 */

@Module
class AppModule(private val app: Context) {

    @Provides
    @Singleton
    fun provideApp(): Context = app
}