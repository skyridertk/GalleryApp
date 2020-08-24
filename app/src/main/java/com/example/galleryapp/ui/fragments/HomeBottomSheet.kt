package com.example.galleryapp.ui.fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

    val PICKFILE_RESULT_CODE = 123

    private val viewModel by viewModels<HomeViewModel> {
        HomeViewModelFactory()
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

//                            uriImages.add(Uri.fromFile(File(result)))
                            uriImages.add(result!!)
                        }
                    } else {
                        val uri = data.data
                        if (uri != null) {
                            val result = uriPathHelper.getPath(requireContext(), uri)

//                            uriImages.add(Uri.fromFile(File(result)))
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