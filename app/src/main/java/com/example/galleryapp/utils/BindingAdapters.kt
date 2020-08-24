package com.example.galleryapp.utils

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.example.galleryapp.R
import com.example.galleryapp.data.models.Image

@BindingAdapter("imageUrl")
fun ImageView.bindImage(image: Image) {
    Glide.with(this).load(image.imagePath)
        .placeholder(R.drawable.ic_baseline_broken_image_24)
        .into(this)
}

@BindingAdapter("imageItem")
fun TextView.showImageName(image: Image) {
    text = image.imageName
}


