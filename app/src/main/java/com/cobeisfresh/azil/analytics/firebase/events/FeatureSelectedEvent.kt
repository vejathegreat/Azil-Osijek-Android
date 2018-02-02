package com.cobeisfresh.azil.analytics.firebase.events

/**
 * Created by Zerina Salitrezic on 23/08/2017.
 */

interface FeatureSelectedEvent {

    companion object {

        const val FEATURE_SELECTED_EVENT = "feature_selected_event"
        const val CATEGORY = "category"
        const val ACTION = "action"
        const val VALUE = "filterValue"
    }

    interface Category {

        companion object {

            const val LOGIN = "login"
            const val ADOPTION = "adoption"
            const val FILTER = "filter"
        }
    }

    interface Action {

        companion object {

            const val LOG_OUT = "button_ok_on_logout_alert_tapped"
            const val SEND_ADOPTION_REQUEST = "send_request_for_adoption"
            const val FILTER_GENDER_MALE = "filter_gender_male"
            const val FILTER_GENDER_FEMALE = "filter_gender_female"
            const val FILTER_AGE_PUPPIES = "filter_age_puppies"
            const val FILTER_AGE_YOUNG = "filter_age_young"
            const val FILTER_AGE_ADULT = "filter_age_adult"
            const val FILTER_SIZE_SMALL = "filter_size_small"
            const val FILTER_SIZE_MIDI = "filter_size_midi"
            const val FILTER_SIZE_BIG = "filter_size_big"
        }
    }

    interface Value {

        companion object {

            const val GENDER_MALE = "male"
            const val GENDER_FEMALE = "female"
            const val AGE_PUPPIES = "puppies"
            const val AGE_YOUNG = "young"
            const val AGE_ADULT = "_adult"
            const val SIZE_SMALL = "small"
            const val SIZE_MIDI = "midi"
            const val SIZE_BIG = "big"
        }
    }
}