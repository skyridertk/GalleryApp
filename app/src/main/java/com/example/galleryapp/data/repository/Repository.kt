package com.example.galleryapp.data.repository


import android.net.Uri
import androidx.work.*
import com.example.galleryapp.data.models.Image
import com.example.galleryapp.workers.ImageUploadWorker
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.sql.Timestamp

class Repository {
    val firestoreRef = Firebase.firestore
    val firebaseStorageRef = FirebaseStorage.getInstance().reference

    fun saveImageItem(ImageList: MutableList<String>) {
        var uploads = 0

        while (uploads < ImageList.size) {
            val uri = ImageList.get(uploads)

            addWorker(uri)

            uploads++
        }

    }

    fun addWorker(uri: String) {

        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val oneTimeTask = OneTimeWorkRequestBuilder<ImageUploadWorker>()
            .setConstraints(constraints)
            .setInputData(
                workDataOf(
                    ImageUploadWorker.KEY_IMAGE_URI to uri
                )
            )
            .build()

        WorkManager.getInstance().enqueueUniqueWork(
            ImageUploadWorker.WORK_NAME,
            ExistingWorkPolicy.APPEND,
            oneTimeTask
        )
    }


    fun upload(
        uri: Uri,
        block: ((Result<Uri>, Int) -> Unit)?
    ) {
        val imageRef = firebaseStorageRef.child("ImageFolder")
            .child("${Timestamp(System.currentTimeMillis())} - " + uri.lastPathSegment!!)
        imageRef.putFile(uri)
            .addOnProgressListener { taskSnapshot ->
                val progress = (100.0 * taskSnapshot.bytesTransferred) / taskSnapshot.totalByteCount
                block?.invoke(Result.Loading, progress.toInt())
            }.continueWithTask { task ->
                if (!task.isSuccessful) {
                    throw task.exception!!
                }
                imageRef.downloadUrl

            }
            .addOnSuccessListener {

            imageRef.downloadUrl.addOnSuccessListener { uri ->
                    firestoreRef.collection("images")
                        .add(Image(uri.lastPathSegment!!, uri.toString()))
                }

                block?.invoke(Result.Success(uri), 100)
            }
            .addOnFailureListener {

            block?.invoke(Result.Error(it), 0)
            }


    }

    fun getImages(): CollectionReference {
        val collectionReference = firestoreRef.collection("images")
        return collectionReference
    }

    fun deleteImage(image: String) {
        var docRef = firestoreRef.collection("images")
            .whereEqualTo("imagePath", image)
            .get().addOnCompleteListener { data ->

                for (document in data.result!!) {
                    firestoreRef.collection("images").document(document.id).delete()
                    firebaseStorageRef.child(document.data.get("imageName").toString()).delete()
                }
            }

    }
}

sealed class Result<out R> {
    data class Success<out T>(val data: T) : Result<T>()
    data class Error(val exception: Exception) : Result<Nothing>()
    object Loading : Result<Nothing>()
}
