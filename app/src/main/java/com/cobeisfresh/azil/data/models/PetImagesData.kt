package com.cobeisfresh.azil.data.models

import java.io.Serializable

/**
 * Created by Zerina Salitrezic on 10/01/2018.
 */

data class PetImagesData(val position: Int = -1,
                         val images: List<String> = listOf()) : Serializable