package com.cobeisfresh.azil.network_interaction

import com.cobeisfresh.azil.network_api.BackendApiModule
import com.cobeisfresh.azil.network_api.DogsApiService
import com.cobeisfresh.azil.network_api.UserApiService
import com.cobeisfresh.azil.schedulers.SchedulerManager
import com.cobeisfresh.azil.schedulers.SchedulerModule
import dagger.Module
import dagger.Provides

/**
 * Created by Zerina Salitrezic on 31/07/2017.
 */

@Module(includes = arrayOf(BackendApiModule::class, SchedulerModule::class))
class NetworkInteractionModule {

    @Provides
    fun provideUserInteractor(schedulerManager: SchedulerManager, userApiService: UserApiService): UserInteractor = UserInteractorImpl(schedulerManager, userApiService)

    @Provides
    fun provideDogsInteractor(schedulerManager: SchedulerManager, dogsApiService: DogsApiService): DogsInteractor = DogsInteractorImpl(schedulerManager, dogsApiService)
}