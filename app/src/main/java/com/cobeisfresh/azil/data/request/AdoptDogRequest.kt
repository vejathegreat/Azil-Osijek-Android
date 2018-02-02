package com.cobeisfresh.azil.data.request

import com.google.gson.annotations.SerializedName

/**
 * Created by Zerina Salitrezic on 22/08/2017.
 */

data class AdoptDogRequest(var name: String = "",
                           var email: String = "",
                           var message: String = "",
                           @SerializedName("id")
                           var petId: String = "")