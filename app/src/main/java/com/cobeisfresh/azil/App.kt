package com.cobeisfresh.azil

import android.support.multidex.MultiDexApplication
import com.crashlytics.android.Crashlytics
import com.squareup.leakcanary.LeakCanary
import com.squareup.leakcanary.RefWatcher
import io.fabric.sdk.android.Fabric

/**
 * Created by Zerina Salitrezic on 31/07/2017.
 */

class App : MultiDexApplication() {

    companion object {
        private lateinit var sInstance: App

        private val component: AppComponent by lazy {
            DaggerAppComponent.builder()
                    .appModule(AppModule(sInstance))
                    .build()
        }
        private var refWatcher: RefWatcher? = null

        fun get(): App = sInstance

        fun setInstance(instance: App) {
            sInstance = instance
        }
    }

    override fun onCreate() {
        super.onCreate()
        setInstance(this)

        if (BuildConfig.DEBUG) {
            initMemLeakDetection()
        }

        if (!BuildConfig.DEBUG) {
            initCrashReporting()
        }

        component.inject(this)
    }

    private fun initMemLeakDetection() {
        refWatcher = LeakCanary.install(this)
    }

    private fun initCrashReporting() {
        Fabric.with(this, Crashlytics())
    }

    fun getRefWatcher(): RefWatcher? = refWatcher

    fun getComponent(): AppComponent = component
}