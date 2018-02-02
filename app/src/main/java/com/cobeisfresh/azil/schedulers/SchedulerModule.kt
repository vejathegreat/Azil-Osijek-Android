package com.cobeisfresh.azil.schedulers

import dagger.Module
import dagger.Provides
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Named

/**
 * Created by Zerina Salitrezic on 01/08/2017.
 */

@Module
class SchedulerModule {

    companion object {
        private const val SYSTEM_IO: String = "SystemIO"
        private const val MAIN_THREAD: String = "MainThread"
    }

    @Provides
    @Named(SYSTEM_IO)
    fun provideSystemIO(): Scheduler = Schedulers.io()

    @Provides
    @Named(MAIN_THREAD)
    fun provideMainThread(): Scheduler = AndroidSchedulers.mainThread()

    @Provides
    fun provideSchedulerManager(@Named(MAIN_THREAD) mainScheduler: Scheduler, @Named(SYSTEM_IO) ioScheduler: Scheduler): SchedulerManager = SchedulerManagerImpl(mainScheduler, ioScheduler)
}