package com.cobeisfresh.azil.ui.pet_details.pet_images

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import com.cobeisfresh.azil.App
import com.cobeisfresh.azil.R
import com.cobeisfresh.azil.common.extensions.onClick
import com.cobeisfresh.azil.data.models.PetImagesData
import com.cobeisfresh.azil.ui.base.BaseActivity
import com.cobeisfresh.azil.ui.pet_details.pet_images.list.FullImagesPagerAdapter
import kotlinx.android.synthetic.main.activity_pet_images.*
import javax.inject.Inject

/**
 * Created by Zerina Salitrezic on 10/01/2018.
 */

class PetImagesActivity : BaseActivity(), PetImagesInterface.View {

    @Inject
    lateinit var presenter: PetImagesInterface.Presenter

    private lateinit var fullImagesPagerAdapter: FullImagesPagerAdapter

    companion object {
        private const val PET_IMAGES = "pet_images"

        fun getLaunchIntent(from: Context, petImagesData: PetImagesData): Intent {
            val intent = Intent(from, PetImagesActivity::class.java)
            intent.putExtra(PET_IMAGES, petImagesData)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        App.get().getComponent().inject(this)
        setContentView(R.layout.activity_pet_images)
        presenter.setView(this)
        initUi()
    }

    override fun initUi() {
        setFullScreen()
        setPagerAdapter()
        setListeners()
        getExtras()
    }

    private fun setFullScreen() = window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

    private fun setListeners() {
        iconClose.onClick { presenter.iconBackClicked() }
    }

    private fun setPagerAdapter() {
        fullImagesPagerAdapter = FullImagesPagerAdapter()
        imagesPager.adapter = fullImagesPagerAdapter
    }

    private fun getExtras() {
        val intent = intent
        val petImagesData = intent.getSerializableExtra(PET_IMAGES) as PetImagesData
        presenter.setPetImagesData(petImagesData)
    }

    override fun showImages(images: List<String>) = fullImagesPagerAdapter.setImages(images)

    override fun setPosition(position: Int) {
        imagesPager.currentItem = position
    }

    override fun showPageIndicator() = circlePageIndicator.setViewPager(imagesPager)

    override fun goBack() = finish()
}