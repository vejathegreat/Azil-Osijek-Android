package com.cobeisfresh.azil.analytics.firebase

import android.os.Bundle
import com.cobeisfresh.azil.analytics.firebase.events.FeatureSelectedEvent
import com.cobeisfresh.azil.analytics.firebase.events.ScreenEvent
import com.google.firebase.analytics.FirebaseAnalytics

/**
 * Created by Zerina Salitrezic on 23/08/2017.
 */

class FirebaseTracking(private val analytics: FirebaseAnalytics) : FirebaseTracker {

    override fun logScreenEvent(name: String) {
        val bundle: Bundle? = Bundle().apply { putString(ScreenEvent.SCREEN_NAME, name) }

        analytics.logEvent(ScreenEvent.SCREEN_EVENT, bundle)
    }

    override fun logFeatureSelectedEvent(categoryName: String, actionName: String, labelName: String) {
        val bundle = Bundle().apply {
            putString(FeatureSelectedEvent.CATEGORY, categoryName)
            putString(FeatureSelectedEvent.ACTION, actionName)
            putString(FeatureSelectedEvent.VALUE, labelName)
        }

        analytics.logEvent(FeatureSelectedEvent.FEATURE_SELECTED_EVENT, bundle)
    }

    override fun logFeatureSelectedEvent(categoryName: String, actionName: String, value: Long) {
        val bundle = Bundle().apply {
            putString(FeatureSelectedEvent.CATEGORY, categoryName)
            putString(FeatureSelectedEvent.ACTION, actionName)
            putLong(FeatureSelectedEvent.VALUE, value)
        }

        analytics.logEvent(FeatureSelectedEvent.FEATURE_SELECTED_EVENT, bundle)
    }
}