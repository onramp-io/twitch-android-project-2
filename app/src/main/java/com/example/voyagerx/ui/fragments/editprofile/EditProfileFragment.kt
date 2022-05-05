package com.example.voyagerx.ui.fragments.editprofile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.example.voyagerx.R
import com.example.voyagerx.databinding.FragmentEditProfileBinding
import com.example.voyagerx.ui.fragments.userprofileac.ProfileFragment
import com.example.voyagerx.util.SharedPreferencesManager

class EditProfileFragment : Fragment() {
    private lateinit var binding: FragmentEditProfileBinding
    private val SharedPreferencesManager by lazy {SharedPreferencesManager(requireContext())}

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentEditProfileBinding.inflate(inflater, container, false)

        setButtonListeners()
        getUserInfoFromDb()
        setProfileBackgroundWallpaperAndFontColors(SharedPreferencesManager.getBackgroundWallpaper())


        return binding.root
    }

    private fun setButtonListeners() {
        binding.tvCancelChanges.setOnClickListener {
                parentFragmentManager.beginTransaction().replace(R.id.frame, ProfileFragment()).commit()
        }

        //code save button on database ticket

    }

    private fun getUserInfoFromDb() {

    }

    private fun setProfileBackgroundWallpaperAndFontColors(wallpaper: Boolean) {
        val profileLayout: ConstraintLayout = binding.profileLayout
        if (wallpaper) {
            profileLayout.background = ContextCompat.getDrawable(requireContext(), R.drawable.stars_background)
            binding.tvBioLabel.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
            binding.tvUserInitial.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
            binding.tvCancelChanges.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
            binding.tvEditProfileLabel.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
            binding.tvLocationLabel.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
            binding.tvNameLabel.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
            binding.tvPasswordLabel.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
        } else {
            profileLayout.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.white))
            binding.tvBioLabel.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
            binding.tvUserInitial.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
            binding.tvCancelChanges.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
            binding.tvEditProfileLabel.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
            binding.tvLocationLabel.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
            binding.tvNameLabel.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
            binding.tvPasswordLabel.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
        }
    }

}