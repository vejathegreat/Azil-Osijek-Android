package com.cobeisfresh.azil.ui.pet_details.pet_images

import com.cobeisfresh.azil.data.models.PetImagesData
import com.cobeisfresh.azil.presentation.base.BasePresenter

/**
 * Created by Zerina Salitrezic on 10/01/2018.
 */

interface PetImagesInterface {

    interface View {

        fun showImages(images: List<String>)

        fun setPosition(position: Int)

        fun showPageIndicator()

        fun goBack()
    }

    interface Presenter : BasePresenter<View> {

        fun setPetImagesData(petImagesData: PetImagesData)

        fun iconBackClicked()
    }
}