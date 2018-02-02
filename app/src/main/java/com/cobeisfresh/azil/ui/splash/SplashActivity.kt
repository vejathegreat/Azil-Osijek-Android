package com.cobeisfresh.azil.ui.splash

import android.os.Bundle
import com.cobeisfresh.azil.R
import com.cobeisfresh.azil.ui.base.BaseActivity
import com.cobeisfresh.azil.ui.main.MainActivity
import io.reactivex.Observable
import io.reactivex.functions.Consumer
import java.util.concurrent.TimeUnit

/**
 * Created by Zerina Salitrezic on 31/07/2017.
 */

class SplashActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        initUi()
    }

    override fun initUi() {
        Observable.timer(2, TimeUnit.SECONDS).subscribe(bindTimerConsumer())
    }

    private fun bindTimerConsumer(): Consumer<Long> = Consumer {
        openMainActivity()
    }

    private fun openMainActivity() {
        startActivity(MainActivity.getLaunchIntent(this))
        finish()
    }
}