package com.cobeisfresh.azil.presentation

import com.cobeisfresh.azil.data.models.PetImagesData
import com.cobeisfresh.azil.mockPetImagesDataMoreImages
import com.cobeisfresh.azil.mockPetImagesDataOneImage
import com.cobeisfresh.azil.ui.pet_details.pet_images.PetImagesInterface
import com.nhaarman.mockito_kotlin.*
import org.junit.Before
import org.junit.Test

/**
 * Created by Zerina Salitrezic on 12/01/2018.
 */

class PetImagesPresenterTest {

    private lateinit var presenter: PetImagesPresenter

    private val view: PetImagesInterface.View = mock()

    @Before
    fun setUp() {
        presenter = PetImagesPresenter()
        presenter.setView(view)
    }

    @Test
    fun setPetImagesDataEmptyImagesList() {
        presenter.setPetImagesData(PetImagesData())

        verifyZeroInteractions(view)
    }

    @Test
    fun setPetImagesDataValidImagesListOneImage() {
        presenter.setPetImagesData(mockPetImagesDataOneImage())

        verify(view).showImages(any())
        verify(view).setPosition(any())
        verifyNoMoreInteractions(view)
    }

    @Test
    fun setPetImagesDataValidImagesListMoreImages() {
        presenter.setPetImagesData(mockPetImagesDataMoreImages())

        verify(view).showImages(any())
        verify(view).setPosition(any())
        verify(view).showPageIndicator()
        verifyNoMoreInteractions(view)
    }

    @Test
    fun iconBackClickedShouldGoBack() {
        presenter.iconBackClicked()

        verify(view).goBack()
        verifyNoMoreInteractions(view)
    }
}