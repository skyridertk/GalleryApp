package com.example.galleryapp.ui.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.galleryapp.data.models.Image
import com.example.galleryapp.data.repository.Repository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job


class HomeViewModel : ViewModel() {
    private val job = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + job)

    private val _images = MutableLiveData<List<Image>>()

    val repository = Repository()

    fun saveToFirestore(ImageList: MutableList<String>) {
        repository.saveImageItem(ImageList)
    }

    fun getImages(): LiveData<List<Image>> {
        repository.getImages().addSnapshotListener { value, e ->
            if (e != null) {
                Log.d("Preview", "Listen failed.", e)
                return@addSnapshotListener
            }

            val images: MutableList<Image> = mutableListOf()

            for (doc in value!!) {

                val image = doc.toObject(Image::class.java)
                images.add(image)
            }

            _images.value = images
        }

        return _images
    }

    fun deleteImage(image: String) {
        repository.deleteImage(image)
    }


    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}

class HomeViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel() as T
        }

        throw IllegalArgumentException()
    }

}