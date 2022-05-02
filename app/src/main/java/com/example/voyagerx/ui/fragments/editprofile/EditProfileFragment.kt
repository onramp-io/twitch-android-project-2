package com.example.voyagerx.ui.fragments.editprofile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.voyagerx.R
import com.example.voyagerx.databinding.FragmentEditProfileBinding
import com.example.voyagerx.ui.fragments.userprofileac.ProfileFragment

class EditProfileFragment : Fragment() {
    private lateinit var binding: FragmentEditProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentEditProfileBinding.inflate(inflater, container, false)

        setButtonListeners()
        getUserInfoFromDb()


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

}