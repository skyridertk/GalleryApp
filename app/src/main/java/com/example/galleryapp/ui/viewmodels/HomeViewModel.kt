package com.example.galleryapp.ui.viewmodels

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.galleryapp.data.models.Image
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch


class HomeViewModel : ViewModel() {
    private val job = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + job)

    val imagesCollectionReference = Firebase.firestore.collection("images")

    private val _images = MutableLiveData<List<Image?>>()
    val images: LiveData<List<Image?>>
        get() = _images

    val uploadingInProgress = MutableLiveData<Boolean>(false)
    val progressState = MutableLiveData<Double>(0.0)

    lateinit var firebaseStorage: StorageReference

    init {
        Log.d("HomeviewModel", "Called")
        firebaseStorage = FirebaseStorage.getInstance().reference
    }

    fun saveImage(image: Image) = uiScope.launch {
        Log.d("Upload", "Task invoked $image")

    }

    fun deleteImage(image: String) {

//        imagesCollectionReference.document(image).delete()
    }


    fun processUpload(ImageList: MutableList<Uri>) {

        val ImageFolder = FirebaseStorage.getInstance().reference.child("ImageFolder")

        var uploads = 0
        while (uploads < ImageList.size) {
            val Image: Uri = ImageList.get(uploads)
            val imagename = ImageFolder.child("image/" + Image.lastPathSegment)
            imagename.putFile(ImageList.get(uploads)).addOnSuccessListener {
                imagename.downloadUrl.addOnSuccessListener { uri ->
                    val url = uri.toString()
                    Log.d("UploadTask", "Upload is completed")

                    val image = Image(Image.lastPathSegment!!, url)
                    try {

                        imagesCollectionReference.add(image)

                    } catch (e: Exception) {
                        Log.d("Upload", "Exception invoked $image")
                    }

                }
            }.addOnProgressListener { taskSnapshot ->
                val progress = (100.0 * taskSnapshot.bytesTransferred) / taskSnapshot.totalByteCount
                progressState.postValue(progress)
                Log.d("UploadTask", "Upload is $progress% done")
            }.addOnFailureListener {
                Log.d("UploadTask", "Upload is failed")
            }
            uploads++
        }

        uploadingInProgress.postValue(true)
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