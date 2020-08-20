package com.example.galleryapp.utils

import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
import com.bumptech.glide.Glide
import com.example.galleryapp.data.models.Image

@BindingAdapter("imageUrl")
fun ImageView.bindImage(image: Image) {
//    Log.d("BindAdapter", "$uri")
    Glide.with(this).load(image.imagePath).into(this)

}

@BindingAdapter("imageItem")
fun TextView.showImageName(image: Image) {
    text = image.imageName
}

@BindingAdapter("processProgress")
fun ProgressBar.processValue(output: MutableLiveData<Double>) {
    progress = 10
}

