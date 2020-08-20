package com.example.galleryapp.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.galleryapp.data.models.Image
import com.example.galleryapp.databinding.ImageItemBinding

class ImagesDataDiff : DiffUtil.ItemCallback<Image>() {
    override fun areItemsTheSame(oldItem: Image, newItem: Image): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Image, newItem: Image): Boolean {
        return oldItem.imagePath == newItem.imagePath
    }

}


class ImagesAdapter(val imageListener: ImageListener) :
    ListAdapter<Image, ImagesAdapter.ImageViewHolder>(ImagesDataDiff()) {
    class ImageViewHolder(val binding: ImageItemBinding) : RecyclerView.ViewHolder(binding.root) {
        companion object {
            fun from(parent: ViewGroup): ImageViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ImageItemBinding.inflate(layoutInflater, parent, false)
                return ImageViewHolder(binding)
            }
        }

        fun bind(
            item: Image,
            imageListener: ImageListener
        ) {
            binding.image = item
            binding.clicklistener = imageListener
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        return ImageViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, imageListener)
    }


}

class ImageListener(val clickListener: (item: String) -> Unit) {
    fun onClick(image: Image) = clickListener(image.imagePath)
}