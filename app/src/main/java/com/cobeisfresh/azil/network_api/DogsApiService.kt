package com.cobeisfresh.azil.network_api

import com.cobeisfresh.azil.data.models.DogModel
import com.cobeisfresh.azil.data.request.AdoptDogRequest
import com.cobeisfresh.azil.data.response.BaseResponse
import com.cobeisfresh.azil.data.response.DogDetailsResponse
import com.cobeisfresh.azil.data.response.DogsResponse
import io.reactivex.Single
import retrofit2.http.*

/**
 * Created by Zerina Salitrezic on 01/08/2017.
 */

interface DogsApiService {

    @GET("pets/all")
    fun getDogs(@QueryMap pageQueryMap: Map<String, Int>, @QueryMap searchQueryMap: Map<String, String>?, @QueryMap filterQueryMap: Map<String, String>?): Single<DogsResponse>

    @GET("pets/pet")
    fun getDogById(@Query("id") dogId: String): Single<DogDetailsResponse>

    @GET("pets/favorites")
    fun getMyDogs(@QueryMap queryMap: Map<String, Int>): Single<List<DogModel>>

    @GET("pets/favorites/toggle")
    fun addRemoveFavourite(@Query("id") dogId: String): Single<BaseResponse>

    @POST("me/adopt")
    fun adoptDog(@Body adoptDogRequest: AdoptDogRequest): Single<BaseResponse>
}