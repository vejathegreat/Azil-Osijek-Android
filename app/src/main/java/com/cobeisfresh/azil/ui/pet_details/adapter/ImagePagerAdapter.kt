package com.cobeisfresh.azil.ui.pet_details.adapter

import android.support.v4.view.PagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.cobeisfresh.azil.R
import com.cobeisfresh.azil.common.extensions.onClick
import com.cobeisfresh.azil.common.utils.loadImage
import com.cobeisfresh.azil.common.utils.validateString
import com.cobeisfresh.azil.data.models.PetImagesData


/**
 * Created by Zerina Salitrezic on 11/10/2017.
 */

class ImagePagerAdapter(private val onImageClick: (PetImagesData) -> Unit) : PagerAdapter() {

    private val images: MutableList<String> = ArrayList()

    fun setImages(images: List<String>) {
        this.images.clear()
        this.images.addAll(images)
        notifyDataSetChanged()
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean = view == `object`

    override fun getCount(): Int {
        return if (images.size > 0) {
            images.size
        } else {
            1
        }
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view: View = LayoutInflater.from(container.context).inflate(R.layout.list_item_image, container, false)
        val petPhoto: ImageView = view.findViewById(R.id.petPhoto) as ImageView
        val image: String = getItem(position)
        validateString(image, onValid = { loadImage(petPhoto, images[position], R.drawable.dog_placeholder) })
        if (images.size > 0) {
            petPhoto.onClick { onImageClick(PetImagesData(position, images)) }
        }
        container.addView(view)
        return view
    }

    private fun getItem(position: Int): String = if (images.isEmpty()) "" else images[position]

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }
}