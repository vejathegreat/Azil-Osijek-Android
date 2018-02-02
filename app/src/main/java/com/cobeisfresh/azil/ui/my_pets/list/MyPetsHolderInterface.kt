package com.cobeisfresh.azil.ui.my_pets.list

import com.cobeisfresh.azil.data.models.DogModel
import com.cobeisfresh.azil.presentation.base.BasePresenter

/**
 * Created by Zerina Salitrezic on 21/08/2017.
 */

interface MyPetsHolderInterface {

    interface View {

        fun showName(name: String)

        fun showImage(image: String)

        fun showAge(age: String)

        fun showGender(gender: String)

        fun showSize(size: String)

        fun showDescription(description: String)

        fun setFavouriteIcon()

        fun setNotFavouriteIcon()

        fun showPlaceholder()

        fun callOnItemClick(id: String)

        fun showNoInternetError()

        fun hideFavouriteIcon()

        fun showFavouritesProgressBar()

        fun hideFavouritesProgressBar()

        fun showDeleteDialog(dog: DogModel)

        fun showServerError()

        fun sendDogDeletedEvent(id: String)
    }

    interface Presenter : BasePresenter<View> {

        fun setDogModel(dogModel: DogModel)

        fun dogLayoutClicked()

        fun iconFavouriteClicked()

        fun deleteFavourite(hasInternet: Boolean, dog: DogModel)
    }
}