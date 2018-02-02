package com.cobeisfresh.azil.ui.base

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.View
import com.cobeisfresh.azil.App
import com.cobeisfresh.azil.BuildConfig
import com.cobeisfresh.azil.analytics.firebase.FirebaseTracker
import com.squareup.leakcanary.RefWatcher
import javax.inject.Inject

/**
 * Created by Cobe Dev Team
 */

abstract class BaseFragment : Fragment() {

    @Inject
    lateinit var firebaseTracker: FirebaseTracker

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        App.get().getComponent().inject(this)
    }

    protected abstract fun prepareUi()

    protected abstract fun prepareData()

    override fun onDestroy() {
        super.onDestroy()
        if (BuildConfig.DEBUG) {
            val refWatcher: RefWatcher? = App.get().getRefWatcher()
            refWatcher?.watch(this)
        }
    }

    protected fun sendScreenTrackingEvent(name: String) = firebaseTracker.logScreenEvent(name)

    protected fun sendFeatureTrackingEvent(categoryName: String, actionName: String, labelName: String) {
        firebaseTracker.logFeatureSelectedEvent(categoryName, actionName, labelName)
    }
}
