package com.cobeisfresh.azil.schedulers

import io.reactivex.Scheduler

/**
 * Created by Zerina Salitrezic on 01/08/2017.
 */

class SchedulerManagerImpl(private val mainThread: Scheduler, private val systemIO: Scheduler) : SchedulerManager {

    override fun io(): Scheduler = systemIO

    override fun mainThread(): Scheduler = mainThread
}