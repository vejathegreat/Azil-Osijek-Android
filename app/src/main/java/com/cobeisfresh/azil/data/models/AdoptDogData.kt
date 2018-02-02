package com.cobeisfresh.azil.data.models

import com.cobeisfresh.azil.common.extensions.isValidEmail
import com.cobeisfresh.azil.common.extensions.isValidName
import com.cobeisfresh.azil.data.request.AdoptDogRequest
import com.google.gson.annotations.SerializedName

/**
 * Created by Zerina Salitrezic on 09/01/2018.
 */

data class AdoptDogData(var name: String = "",
                        var email: String = "",
                        var message: String = "",
                        @SerializedName("id")
                        var petId: String = "",
                        var isAcceptBrochure: Boolean = false)

fun AdoptDogData.isAdoptDogFormValid(): Boolean {
    return name.isValidName()
            && email.isValidEmail()
            && isAcceptBrochure
}

fun AdoptDogData.mapToAdoptDogRequest(): AdoptDogRequest {
    return AdoptDogRequest(name, email, message, petId)
}
