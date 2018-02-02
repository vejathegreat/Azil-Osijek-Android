package com.cobeisfresh.azil.common.utils

import com.cobeisfresh.azil.R
import com.cobeisfresh.azil.common.constants.*

/**
 * Created by Zerina Salitrezic on 12/10/2017.
 */

fun getGenderFormat(genderKey: String): String = when (genderKey) {
    FEMALE -> getString(R.string.all_pets_gender_female)
    MALE -> getString(R.string.all_pets_gender_male)
    else -> ""
}

fun getAgeFormat(ageKey: String): String = when (ageKey) {
    PUPPY -> getString(R.string.all_pets_age_puppy)
    YOUNG -> getString(R.string.all_pets_age_young)
    ADULT -> getString(R.string.all_pets_age_adult)
    else -> ""
}

fun getSizeFormat(sizeKey: String): String = when (sizeKey) {
    SMALL -> getString(R.string.all_pets_size_small)
    MEDIUM -> getString(R.string.all_pets_size_midi)
    LARGE -> getString(R.string.all_pets_size_big)
    else -> ""
}