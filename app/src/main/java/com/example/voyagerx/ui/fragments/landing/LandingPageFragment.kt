package com.example.voyagerx.ui.fragments.landing

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.voyagerx.databinding.FragmentLandingPageBinding


class LandingPageFragment : Fragment() {
    private lateinit var binding: FragmentLandingPageBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentLandingPageBinding.inflate(inflater)

        return binding.root
    }
}