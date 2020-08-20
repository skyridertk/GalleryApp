package com.example.galleryapp.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.galleryapp.R
import com.example.galleryapp.data.models.Image
import com.example.galleryapp.databinding.FragmentHomeBinding
import com.example.galleryapp.ui.adapters.ImageListener
import com.example.galleryapp.ui.adapters.ImagesAdapter
import com.example.galleryapp.ui.viewmodels.HomeViewModel
import com.example.galleryapp.ui.viewmodels.HomeViewModelFactory
import com.google.firebase.firestore.ktx.toObject

class HomeFragment : Fragment(R.layout.fragment_home) {
    private val viewModel by viewModels<HomeViewModel> {
        HomeViewModelFactory()
    }

    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_home, container, false)

        val imageAdapter = ImagesAdapter(ImageListener { item ->
            val action = HomeFragmentDirections.actionHomeFragmentToHomeDialogFragment(item)
            findNavController().navigate(action)
        })

        binding.listOfImages.adapter = imageAdapter

        val manager = GridLayoutManager(requireContext(), 3)

        binding.listOfImages.layoutManager = manager

        viewModel.imagesCollectionReference.addSnapshotListener { value, e ->
            if (e != null) {
                Log.d("Preview", "Listen failed.", e)
                return@addSnapshotListener
            }
            val images = ArrayList<Image>()
            images.clear()
            for (doc in value!!) {

                val image = doc.toObject<Image>()
                images.add(image)
            }

            imageAdapter.submitList(images)
        }


        binding.uploadButton.setOnClickListener {

            val action = HomeFragmentDirections.actionHomeFragmentToHomeBottomSheet()
            findNavController().navigate(action)
        }

        return binding.root
    }
}
