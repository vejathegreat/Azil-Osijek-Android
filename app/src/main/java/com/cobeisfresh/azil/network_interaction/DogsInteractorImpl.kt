package com.cobeisfresh.azil.network_interaction

import com.cobeisfresh.azil.common.constants.KEY_AGE
import com.cobeisfresh.azil.common.constants.KEY_GENDER
import com.cobeisfresh.azil.common.constants.KEY_SIZE
import com.cobeisfresh.azil.common.extensions.observeWith
import com.cobeisfresh.azil.common.utils.getFilterFormat
import com.cobeisfresh.azil.common.utils.getFilterQueryMap
import com.cobeisfresh.azil.common.utils.getPagesQueryMap
import com.cobeisfresh.azil.common.utils.getSearchQueryMap
import com.cobeisfresh.azil.data.models.AdoptDogData
import com.cobeisfresh.azil.data.models.DogModel
import com.cobeisfresh.azil.data.models.FilterModel
import com.cobeisfresh.azil.data.models.mapToAdoptDogRequest
import com.cobeisfresh.azil.data.response.BaseResponse
import com.cobeisfresh.azil.data.response.DogDetailsResponse
import com.cobeisfresh.azil.data.response.DogsResponse
import com.cobeisfresh.azil.network_api.DogsApiService
import com.cobeisfresh.azil.schedulers.SchedulerManager
import io.reactivex.SingleObserver
import io.reactivex.disposables.Disposable

/**
 * Created by Zerina Salitrezic on 01/08/2017.
 */

class DogsInteractorImpl(private val schedulerManager: SchedulerManager, private val dogsApiService: DogsApiService) : DogsInteractor {

    override fun getDogs(page: Int, search: String, filters: List<FilterModel>, observer: SingleObserver<DogsResponse>) {
        val checkedFilters = filters.filter { it.isChecked }
        val genderStringFormat = getFilterFormat(KEY_GENDER, checkedFilters)
        val ageStringFormat = getFilterFormat(KEY_AGE, checkedFilters)
        val sizeStringFormat = getFilterFormat(KEY_SIZE, checkedFilters)

        dogsApiService.getDogs(
                getPagesQueryMap(page = page),
                getSearchQueryMap(searchWord = search),
                getFilterQueryMap(genderStringFormat, ageStringFormat, sizeStringFormat))
                .observeWith(manager = schedulerManager, observer = observer)
    }

    override fun getDogById(dogId: String, dogDetailsResponseSingleObserver: SingleObserver<DogDetailsResponse>) {
        dogsApiService.getDogById(dogId).observeWith(manager = schedulerManager, observer = dogDetailsResponseSingleObserver)
    }

    override fun adoptDog(adoptDogData: AdoptDogData, baseResponseSingleObserver: SingleObserver<BaseResponse>) {
        val adoptDogRequest = adoptDogData.mapToAdoptDogRequest()
        dogsApiService.adoptDog(adoptDogRequest).observeWith(manager = schedulerManager, observer = baseResponseSingleObserver)
    }

    override fun getMyDogs(page: Int, myDogsResponseSingleObserver: SingleObserver<List<DogModel>>) {
        dogsApiService.getMyDogs(getPagesQueryMap(page)).observeWith(manager = schedulerManager, observer = myDogsResponseSingleObserver)
    }

    override fun addRemoveFavourite(dogId: String, addRemoveFavouriteResponseSingleObserver: SingleObserver<BaseResponse>) {
        dogsApiService.addRemoveFavourite(dogId).observeWith(manager = schedulerManager, observer = addRemoveFavouriteResponseSingleObserver)
    }
}