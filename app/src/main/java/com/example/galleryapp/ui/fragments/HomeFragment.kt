package com.example.galleryapp.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.example.galleryapp.R
import com.example.galleryapp.databinding.FragmentHomeBinding
import com.example.galleryapp.ui.adapters.ImageListener
import com.example.galleryapp.ui.adapters.ImagesAdapter
import com.example.galleryapp.ui.viewmodels.HomeViewModel
import com.example.galleryapp.ui.viewmodels.HomeViewModelFactory

class HomeFragment : Fragment() {
    private val viewModel by viewModels<HomeViewModel> {
        HomeViewModelFactory()
    }

    private val args by navArgs<HomeFragmentArgs>()

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

        val manager = GridLayoutManager(requireContext(), 3)

        binding.listOfImages.adapter = imageAdapter
        binding.listOfImages.layoutManager = manager

        viewModel.getImages().observe(viewLifecycleOwner, Observer { images ->
            imageAdapter.submitList(images)
        })

//        viewModel.stateOfStackbar().observe(viewLifecycleOwner, Observer { event ->
//            if(event) {
//                Log.d("Snackbar", "Invocked")
//                if (args.message.length > 0) {
//                    Snackbar.make(binding.coordinator, args.message.toString(), Snackbar.LENGTH_LONG).show()
//                }
//
//            }
//        })

        binding.uploadButton.setOnClickListener {
            val action = HomeFragmentDirections.actionHomeFragmentToHomeBottomSheet()
            findNavController().navigate(action)
        }

        return binding.root
    }
}
