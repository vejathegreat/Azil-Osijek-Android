package com.cobeisfresh.azil.presentation

import com.cobeisfresh.azil.data.models.PetImagesData
import com.cobeisfresh.azil.ui.pet_details.pet_images.PetImagesInterface

/**
 * Created by Zerina Salitrezic on 10/01/2018.
 */

class PetImagesPresenter : PetImagesInterface.Presenter {

    private lateinit var view: PetImagesInterface.View

    override fun setView(view: PetImagesInterface.View) {
        this.view = view
    }

    override fun setPetImagesData(petImagesData: PetImagesData) {
        if (petImagesData.images.isNotEmpty()) {
            with(petImagesData) {
                view.showImages(images)
                view.setPosition(position)
                handlePageIndicatorVisibility(images.size)
            }
        }
    }

    private fun handlePageIndicatorVisibility(imagesSize: Int) {
        if (imagesSize > 1) {
            view.showPageIndicator()
        }
    }

    override fun iconBackClicked() = view.goBack()
}