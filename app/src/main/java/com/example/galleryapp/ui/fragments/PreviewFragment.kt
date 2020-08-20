package com.example.galleryapp.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.galleryapp.R
import com.example.galleryapp.databinding.FragmentPreviewBinding
import com.example.galleryapp.ui.viewmodels.HomeViewModel
import com.example.galleryapp.ui.viewmodels.HomeViewModelFactory

//upload
class PreviewFragment : DialogFragment() {
    lateinit var binding: FragmentPreviewBinding

    private val viewModel: HomeViewModel by viewModels {
        HomeViewModelFactory()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(layoutInflater, R.layout.fragment_preview, container, false)

        viewModel.uploadingInProgress.observe(viewLifecycleOwner, Observer {
            Log.d("PickTest", it.toString())

            val action = PreviewFragmentDirections.actionPreviewFragmentToHomeFragment()
            findNavController().navigate(action)
        })
        return binding.root
    }
}