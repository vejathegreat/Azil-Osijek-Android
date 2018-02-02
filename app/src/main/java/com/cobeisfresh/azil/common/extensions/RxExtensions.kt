package com.cobeisfresh.azil.common.extensions

import com.cobeisfresh.azil.schedulers.SchedulerManager
import io.reactivex.Single
import io.reactivex.SingleObserver

/**
 * Created by Zerina Salitrezic on 01/08/2017.
 */

fun <T> Single<T>.observeWith(manager: SchedulerManager, observer: SingleObserver<T>) =
        subscribeOn(manager.io()).
                observeOn(manager.mainThread()).
                subscribe(observer)