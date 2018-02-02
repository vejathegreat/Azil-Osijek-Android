package com.cobeisfresh.azil.ui.base

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.cobeisfresh.azil.App
import com.cobeisfresh.azil.analytics.firebase.FirebaseTracker
import javax.inject.Inject

/**
 * Created by Zerina Salitrezic on 06/02/2017.
 */

abstract class BaseActivity : AppCompatActivity() {

    @Inject
    lateinit var firebaseTracker: FirebaseTracker

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        App.get().getComponent().inject(this)
    }

    protected abstract fun initUi()

    protected fun sendScreenTrackingEvent(name: String) = firebaseTracker.logScreenEvent(name)

    protected fun sendFeatureTrackingEvent(categoryName: String, actionName: String, labelName: String) {
        firebaseTracker.logFeatureSelectedEvent(categoryName, actionName, labelName)
    }
}
