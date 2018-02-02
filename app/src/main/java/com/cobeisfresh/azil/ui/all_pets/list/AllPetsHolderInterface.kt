package com.cobeisfresh.azil.ui.all_pets.list

import com.cobeisfresh.azil.data.models.DogModel
import com.cobeisfresh.azil.presentation.base.BasePresenter

/**
 * Created by Zerina Salitrezic on 02/08/2017.
 */

interface AllPetsHolderInterface {

    interface View {

        fun showName(name: String)

        fun showAge(age: String)

        fun showSize(size: String)

        fun showDescription(description: String)

        fun showImage(image: String)

        fun callOnItemClick(dogId: String)

        fun showNoInternetError()

        fun setFavouriteIcon()

        fun hideFavouriteIcon()

        fun setNotFavouriteIcon()

        fun showUserMustExistError()

        fun showPlaceholder()

        fun showFavouritesProgressBar()

        fun hideFavouritesProgressBar()

        fun showGender(gender: String)

        fun showChangeFavouriteStateError()

        fun sendFavouritedDogEvent()

        fun sendDogDeletedEvent(dogId: String)
    }

    interface Presenter : BasePresenter<View> {

        fun setDogModel(dog: DogModel)

        fun dogLayoutClicked()

        fun iconFavouriteClicked(hasInternet: Boolean)
    }
}