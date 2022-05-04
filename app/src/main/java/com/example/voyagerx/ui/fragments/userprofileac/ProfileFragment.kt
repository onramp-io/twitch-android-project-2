package com.example.voyagerx.ui.fragments.userprofileac

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.example.voyagerx.R
import com.example.voyagerx.databinding.FragmentProfileBinding
import com.example.voyagerx.databinding.FragmentSettingsBinding
import com.example.voyagerx.util.SharedPreferencesManager
import com.example.voyagerx.ui.fragments.editprofile.EditProfileFragment


class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private val SharedPreferencesManager by lazy {SharedPreferencesManager(requireContext())}
    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding = FragmentProfileBinding.inflate(inflater, container, false)
        val editProfileBtn : ImageButton = binding.editProfileImageBtn

        editProfileBtn.setOnClickListener {
            parentFragmentManager.beginTransaction().replace(R.id.frame, EditProfileFragment()).commit()
        }

        setProfileBackgroundWallpaper(SharedPreferencesManager.getBackgroundWallpaper())

        return binding.root
    }

    private fun setProfileBackgroundWallpaper(wallpaper: Boolean) {
        val profileLayout: ConstraintLayout = binding.profileLayout
        if (wallpaper) {
            profileLayout.background = ContextCompat.getDrawable(requireContext(), R.drawable.rocket_background)
        } else {
            profileLayout.background = ContextCompat.getDrawable(requireContext(), R.drawable.stars_background)
        }
    }
}