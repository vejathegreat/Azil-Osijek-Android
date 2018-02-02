package com.cobeisfresh.azil.network_interaction

import com.cobeisfresh.azil.data.models.AdoptDogData
import com.cobeisfresh.azil.data.models.DogModel
import com.cobeisfresh.azil.data.models.FilterModel
import com.cobeisfresh.azil.data.response.BaseResponse
import com.cobeisfresh.azil.data.response.DogDetailsResponse
import com.cobeisfresh.azil.data.response.DogsResponse
import io.reactivex.SingleObserver

/**
 * Created by Zerina Salitrezic on 01/08/2017.
 */

interface DogsInteractor {

    fun getDogs(page: Int, search: String = "", filters: List<FilterModel> = mutableListOf(), observer: SingleObserver<DogsResponse>)

    fun getDogById(dogId: String, dogDetailsResponseSingleObserver: SingleObserver<DogDetailsResponse>)

    fun adoptDog(adoptDogData: AdoptDogData, baseResponseSingleObserver: SingleObserver<BaseResponse>)

    fun getMyDogs(page: Int, myDogsResponseSingleObserver: SingleObserver<List<DogModel>>)

    fun addRemoveFavourite(dogId: String, addRemoveFavouriteResponseSingleObserver: SingleObserver<BaseResponse>)
}