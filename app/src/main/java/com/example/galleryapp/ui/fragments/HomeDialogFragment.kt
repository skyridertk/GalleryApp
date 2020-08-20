package com.example.galleryapp.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
        binding =
            DataBindingUtil.inflate(layoutInflater, R.layout.fragment_dialog, container, false)

        binding.Yes.setOnClickListener {
            Log.d("PickTest", "Pressed ${args.item}")
            viewModel.deleteImage(args.item)
            dismiss()
        }

        binding.button4.setOnClickListener {
            Toast.makeText(context, "No", Toast.LENGTH_SHORT).show()
            dismiss()
        }
        return binding.root
    }
}