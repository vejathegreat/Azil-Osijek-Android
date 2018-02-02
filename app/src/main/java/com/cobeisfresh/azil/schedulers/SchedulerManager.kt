package com.cobeisfresh.azil.schedulers

import io.reactivex.Scheduler

/**
 * Created by Zerina Salitrezic on 01/08/2017.
 */

interface SchedulerManager {

    fun mainThread(): Scheduler

    fun io(): Scheduler
}