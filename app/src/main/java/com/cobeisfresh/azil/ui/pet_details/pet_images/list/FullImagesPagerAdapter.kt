package com.cobeisfresh.azil.ui.pet_details.pet_images.list

import android.graphics.Bitmap
import android.support.v4.view.PagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.Transition
import com.cobeisfresh.azil.R
import java.util.*

/**
 * Created by Zerina Salitrezic on 10/01/2018.
 */

class FullImagesPagerAdapter : PagerAdapter() {

    private val images = ArrayList<String>()

    fun setImages(images: List<String>) {
        this.images.clear()
        this.images.addAll(images)
        notifyDataSetChanged()
    }

    override fun isViewFromObject(view: View, `object`: Any) = view == `object`

    override fun getCount(): Int = images.size

    private fun getItem(position: Int): String = if (images.isEmpty()) "" else images[position]

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view = LayoutInflater.from(container.context).inflate(R.layout.item_full_image, container, false)
        loadImage(view, position)
        container.addView(view)
        return view
    }

    private fun loadImage(root: View, position: Int) {
        val imageItem = getItem(position)
        val petImage = root.findViewById(R.id.petPhoto) as ImageView

        if (imageItem.isNotBlank()) {
            Glide.with(root.context)
                    .asBitmap()
                    .load(imageItem)
                    .into(object : SimpleTarget<Bitmap>(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL) {
                        override fun onResourceReady(resource: Bitmap?, transition: Transition<in Bitmap>?) {
                            petImage.setImageBitmap(resource)
                        }
                    })
        }
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }
}