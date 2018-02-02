package com.cobeisfresh.azil.analytics.firebase

/**
 * Created by Zerina Salitrezic on 23/08/2017.
 */

interface FirebaseTracker {

    fun logScreenEvent(name: String)

    fun logFeatureSelectedEvent(categoryName: String, actionName: String, labelName: String)

    fun logFeatureSelectedEvent(categoryName: String, actionName: String, value: Long)
}