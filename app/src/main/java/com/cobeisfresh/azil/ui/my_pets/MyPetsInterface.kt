package com.cobeisfresh.azil.ui.my_pets

import com.cobeisfresh.azil.data.models.DogModel
import com.cobeisfresh.azil.presentation.base.BasePresenter

/**
 * Created by Zerina Salitrezic on 21/08/2017.
 */

interface MyPetsInterface {

    interface View {

        fun showProgressBar()

        fun hideProgressBar()

        fun stopRefreshing()

        fun showNoInternetErrorFullScreen()

        fun showServerErrorFullScreen()

        fun hideErrorFullScreen()

        fun showServerError()

        fun showNoInternetError()

        fun hideErrorLayout()

        fun setMyDogs(myDogs: List<DogModel>)

        fun addMoreDogs(myDogs: List<DogModel>)

        fun showNoDataLayout()

        fun showDataLayout()

        fun hideDataLayout()

        fun hideNoDataLayout()
    }

    interface Presenter : BasePresenter<View> {

        fun setIsFirstPage(shouldReset: Boolean)

        fun getMyDogs(hasInternet: Boolean)

        fun onLastItemReached(hasInternet: Boolean)

        fun onSwipeToRefresh(hasInternet: Boolean)

        fun errorFullScreenClicked(hasInternet: Boolean)

        fun checkExistingFavourites(numberOfFavourites: Int)

        fun unSubscribe()
    }
}