package com.example.galleryapp.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.example.galleryapp.R
import com.example.galleryapp.databinding.FragmentDialogBinding
import com.example.galleryapp.ui.viewmodels.HomeViewModel
import com.example.galleryapp.ui.viewmodels.HomeViewModelFactory

class HomeDialogFragment : DialogFragment() {
    lateinit var binding: FragmentDialogBinding

    val args: HomeDialogFragmentArgs by navArgs<HomeDialogFragmentArgs>()

    private val viewModel by viewModels<HomeViewModel> {
        HomeViewModelFactory()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog?.setTitle("Delete Image")

        binding =
            DataBindingUtil.inflate(layoutInflater, R.layout.fragment_dialog, container, false)

        binding.Yes.setOnClickListener {
            viewModel.deleteImage(args.item)
            dismiss()
        }

        binding.button4.setOnClickListener {
            dismiss()
        }
        return binding.root
    }
}