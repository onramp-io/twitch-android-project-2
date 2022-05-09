package com.example.voyagerx.ui.fragments.editprofile

import android.os.Bundle
import android.util.Log
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
        setProfileBackgroundWallpaper(SharedPreferencesManager.getBackgroundWallpaper())


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

    private fun setProfileBackgroundWallpaper(wallpaper: Boolean) {
        if (wallpaper) {
            binding.headerCoverImage.setImageResource(R.drawable.stars_background)
        } else {
            binding.headerCoverImage.setImageResource(R.drawable.plain_background)
        }
    }

}