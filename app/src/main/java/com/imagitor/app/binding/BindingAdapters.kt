package com.imagitor.app.binding

import androidx.appcompat.widget.AppCompatImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.imagitor.app.R


@BindingAdapter("loadImage")
fun loadImage(imageView: AppCompatImageView, image: Any?) {
    Glide.with(imageView.context).load(image ?: R.drawable.ic_placeholder)
        .into(imageView)
}