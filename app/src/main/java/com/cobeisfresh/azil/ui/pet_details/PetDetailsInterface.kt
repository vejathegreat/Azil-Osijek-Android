package com.cobeisfresh.azil.ui.pet_details

import com.cobeisfresh.azil.data.models.PetImagesData
import com.cobeisfresh.azil.data.response.DogDetailsResponse
import com.cobeisfresh.azil.presentation.base.BasePresenter

/**
 * Created by Zerina Salitrezic on 03/08/2017.
 */

interface PetDetailsInterface {

    interface View {

        fun showImages(images: List<String>)

        fun showName(name: String)

        fun showAge(age: String)

        fun showDescription(description: String)

        fun setFavouriteIcon()

        fun setNotFavouriteIcon()

        fun showProgressBar()

        fun hideProgressBar()

        fun showNoInternetErrorFullScreen()

        fun hideErrorFullScreen()

        fun showServerErrorFullScreen()

        fun showDogDetailsLayout()

        fun hideDogDetailsLayout()

        fun hideFavouriteIcon()

        fun showUserMustExistError()

        fun showFavouritesProgressBar()

        fun hideFavouritesProgressBar()

        fun showSize(size: String)

        fun showGender(gender: String)

        fun openAdoptPetActivity(dogDetailsResponse: DogDetailsResponse)

        fun goBack()

        fun showPageIndicator()

        fun showAdoptButton()

        fun showServerError()

        fun showNoInternetError()

        fun hideErrorLayout()

        fun refreshPetDetails()

        fun openPetImagesActivity(petImagesData: PetImagesData)

        fun sendFavouriteStateEvent(isFavourite: Boolean, dogId: String)
    }

    interface Presenter : BasePresenter<View> {

        fun setDogId(dogId: String)

        fun getDogById(hasInternet: Boolean)

        fun iconFavouriteClicked(hasInternet: Boolean)

        fun iconBackClicked()

        fun adoptPetClicked()

        fun errorFullLayoutClicked(hasInternet: Boolean)

        fun errorLayoutClicked()

        fun userAuthenticated()

        fun imageClicked(petImagesData: PetImagesData)

        fun unSubscribe()
    }
}