package com.cobeisfresh.azil.analytics

import android.content.Context
import com.cobeisfresh.azil.AppModule
import com.cobeisfresh.azil.analytics.firebase.FirebaseTracker
import com.cobeisfresh.azil.analytics.firebase.FirebaseTracking
import com.google.firebase.analytics.FirebaseAnalytics
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by Zerina Salitrezic on 31/07/2017.
 */

@Module(includes = [(AppModule::class)])
class AnalyticsModule {

    @Provides
    @Singleton
    fun provideFirebaseAnalytics(from: Context): FirebaseAnalytics = FirebaseAnalytics.getInstance(from)

    @Provides
    fun provideFirebase(firebaseAnalytics: FirebaseAnalytics): FirebaseTracker = FirebaseTracking(firebaseAnalytics)
}