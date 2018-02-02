package com.cobeisfresh.azil.common.utils

import android.support.v4.content.ContextCompat
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

/**
 * Created by Zerina Salitrezic on 28/08/2017.
 */

fun loadImage(imageView: ImageView?, imagePath: String) {
    imageView?.run { Glide.with(context).load(imagePath).into(this) }
}

fun loadImage(imageView: ImageView?, imagePath: String, placeholder: Int) {
    imageView?.run { Glide.with(context).load(imagePath).apply(RequestOptions().placeholder(ContextCompat.getDrawable(context, placeholder))).into(this) }
}

fun loadPlaceholder(imageView: ImageView?, placeholder: Int) {
    imageView?.run { Glide.with(context).load("").apply(RequestOptions().placeholder(ContextCompat.getDrawable(context, placeholder))).into(this) }
}