package com.example.galleryapp.ui.fragments

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.galleryapp.R
import com.example.galleryapp.databinding.BottomSheetItemBinding
import com.example.galleryapp.ui.viewmodels.HomeViewModel
import com.example.galleryapp.ui.viewmodels.HomeViewModelFactory
import com.example.galleryapp.utils.URIPathHelper
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class HomeBottomSheet : BottomSheetDialogFragment() {
    lateinit var binding: BottomSheetItemBinding

    private val viewModel by viewModels<HomeViewModel> {
        HomeViewModelFactory()
    }

    companion object {
        private const val TAG = "CameraXView"
        private const val FILENAME = "yyyy-MM-dd-HH-mm-ss-SSS"
        private const val PHOTO_EXTENSION = ".jpg"
        private const val PICKFILE_RESULT_CODE = 123
        private const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        binding =
            DataBindingUtil.inflate(layoutInflater, R.layout.bottom_sheet_item, container, false)

        binding.camera.setOnClickListener {

            val action = HomeBottomSheetDirections.actionHomeBottomSheetToCameraFragment()
            findNavController().navigate(action)
        }

        binding.files.setOnClickListener {
            val intent = Intent().apply {
                type = "image/*"
                action = Intent.ACTION_GET_CONTENT
                putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            }

            startActivityForResult(intent, PICKFILE_RESULT_CODE)

        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                HomeBottomSheet.REQUIRED_PERMISSIONS,
                HomeBottomSheet.REQUEST_CODE_PERMISSIONS
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == HomeBottomSheet.REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionsGranted()) {
                Toast.makeText(context, "Permission not granted", Toast.LENGTH_SHORT).show()
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun allPermissionsGranted() = HomeBottomSheet.REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            requireContext(), it
        ) == PackageManager.PERMISSION_GRANTED
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            PICKFILE_RESULT_CODE -> if (resultCode == Activity.RESULT_OK) {

                if (data != null) {

                    val uriImages = mutableListOf<String>()
                    val uriPathHelper = URIPathHelper()

                    if (null != data.clipData) {
                        for (i in 0 until data.clipData!!.itemCount) {
                            val uri = data.clipData!!.getItemAt(i).uri
                            val result = uriPathHelper.getPath(requireContext(), uri)

                            uriImages.add(result!!)
                        }
                    } else {
                        val uri = data.data
                        if (uri != null) {
                            val result = uriPathHelper.getPath(requireContext(), uri)

                            uriImages.add(result!!)
                        }

                    }

                    viewModel.saveToFirestore(uriImages)


                    val action =
                        HomeBottomSheetDirections.actionHomeBottomSheetToHomeFragment("Uploading Images")
                    findNavController().navigate(action)

                }
            }
        }
    }

}