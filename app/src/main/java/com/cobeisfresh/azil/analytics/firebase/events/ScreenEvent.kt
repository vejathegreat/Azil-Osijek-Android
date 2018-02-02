package com.cobeisfresh.azil.analytics.firebase.events

/**
 * Created by Zerina Salitrezic on 23/08/2017.
 */

interface ScreenEvent {

    companion object {

        const val SCREEN_EVENT = "screen_event"
        const val SCREEN_NAME = "screen_name"
    }

    interface ScreenName {

        companion object {

            const val WELCOME = "welcome"
            const val ALL_PETS = "all_pets_list"
            const val SEARCH_PETS_BY_NAME = "search_all_pets"
            const val FILTER_PETS = "filter_all_pets"
            const val PET_DETAILS = "pet_details"
            const val ADOPT_PET = "adopt_pet"
            const val REGISTRATION = "registration"
            const val LOGIN = "login"
            const val FORGOT_PASSWORD = "forgot_password"
            const val MY_PETS = "my_pets"
            const val INFO = "info_default"
            const val EDIT_PROFILE = "edit_profile"
            const val ABOUT_SHELTER = "about_shelter"
            const val ABOUT_ASSOCIATION = "about_association"
            const val DONATIONS = "donations"
            const val TERMS_AND_CONDITIONS = "terms_and_conditions"
            const val PRIVACY_POLICY = "privacy_policy"
        }
    }
}