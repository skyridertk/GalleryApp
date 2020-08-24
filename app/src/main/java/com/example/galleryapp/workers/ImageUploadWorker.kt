package com.example.galleryapp.workers

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.galleryapp.R
import com.example.galleryapp.data.repository.Repository
import com.example.galleryapp.ui.MainActivity
import com.example.galleryapp.utils.Notifier
import java.io.File

class ImageUploadWorker(appContext: Context, params: WorkerParameters) :
    CoroutineWorker(appContext, params) {
    companion object {
        const val KEY_IMAGE_URI: String = "key-image-uri"
        const val KEY_UPLOADED_URI: String = "key-uploaded-uri"
        const val WORK_NAME = "com.example.galleryapp.workers.ImageUploadWorker"
    }

    private val context = applicationContext
    private val repository = Repository()
    private var title: String = "Image Upload"

    override suspend fun doWork(): Result {
        val fileUri = Uri.fromFile(File(inputData.getString(KEY_IMAGE_URI)))

        fileUri?.let { uri ->
            repository.upload(uri) { result, percentage ->
                when (result) {
                    is com.example.galleryapp.data.repository.Result.Success -> {
                        showUploadFinishedNotification(result.data)
                    }

                    is com.example.galleryapp.data.repository.Result.Loading -> {
                        showProgressNotification(
                            context.getString(R.string.progress_uploading),
                            percentage
                        )
                    }

                    is com.example.galleryapp.data.repository.Result.Error -> {
                        showUploadFinishedNotification(null)
                    }
                }
            }
        }

        return Result.success()
    }

    private fun showProgressNotification(caption: String, percent: Int) {
        Notifier
            .progressable(
                context,
                100, percent
            ) {
                notificationId = title.hashCode()
                contentTitle = title
                contentText = caption
                smallIcon = R.drawable.ic_baseline_attach_file_24
            }
    }

    private fun showUploadFinishedNotification(downloadUrl: Uri?) {
        Notifier
            .dismissNotification(context, title.hashCode())

        if (downloadUrl != null) return

        val caption = context.getString(
            R.string.upload_failure
        )

        // Make Intent to MainActivity
        val intent = Intent(applicationContext, MainActivity::class.java)
            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)

        val pendingIntent = PendingIntent.getActivity(
            applicationContext, 0 /* requestCode */, intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        Notifier.show(context) {
            notificationId = title.hashCode()
            contentTitle = title
            contentText = caption
            this.pendingIntent = pendingIntent
        }
    }
}