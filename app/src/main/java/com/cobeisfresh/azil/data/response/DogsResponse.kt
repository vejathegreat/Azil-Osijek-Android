package com.cobeisfresh.azil.data.response

import com.cobeisfresh.azil.data.models.DogModel
import com.google.gson.annotations.SerializedName

/**
 * Created by Zerina Salitrezic on 22/08/2017.
 */

data class DogsResponse(@SerializedName("data")
                        var dogs: List<DogModel> = arrayListOf())