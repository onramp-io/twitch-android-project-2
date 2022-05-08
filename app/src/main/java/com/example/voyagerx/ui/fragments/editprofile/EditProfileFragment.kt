package com.example.voyagerx.ui.fragments.editprofile

import android.os.Bundle
import android.text.Editable
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.commit
import com.example.voyagerx.R
import com.example.voyagerx.databinding.FragmentEditProfileBinding
import com.example.voyagerx.repository.UserRepository
import com.example.voyagerx.repository.model.Launch
import com.example.voyagerx.repository.model.User
import com.example.voyagerx.ui.fragments.userprofileac.ProfileFragment
import com.example.voyagerx.util.SharedPreferencesManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class EditProfileFragment : Fragment() {
    @Inject
    lateinit var userRepository: UserRepository
    private lateinit var binding: FragmentEditProfileBinding
    private val SharedPreferencesManager by lazy {SharedPreferencesManager(requireContext())}

    private val userWord = "User"


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentEditProfileBinding.inflate(inflater, container, false)

        setProfileBackgroundWallpaperAndFontColors(SharedPreferencesManager.getBackgroundWallpaper())

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setCancelButton()
        setUserInfoInTextFields()

    }

    private fun setCancelButton() {
        binding.tvCancelChanges.setOnClickListener {
                parentFragmentManager.beginTransaction().replace(R.id.frame, ProfileFragment()).commit()
        }
    }

    private fun setUserInfoInTextFields() {
        val usersName: String? = userRepository.getCurrentUser()?.name
        val userBio: String? = userRepository.getCurrentUser()?.bio
        val usersLocation: String? = userRepository.getCurrentUser()?.location

        fun String.toEditable(): Editable = Editable.Factory.getInstance().newEditable(this)
        //show user initial in avatar
        if (usersName.isNullOrEmpty()) {
            binding.tvUserInitial.text = userWord.first().uppercaseChar().toString()
        } else {
            val firstLetterUserName: Char = usersName
                .toString()
                .first()
                .uppercaseChar()
            binding.tvUserInitial.text = firstLetterUserName.toString()
        }

        //show user name in text box
        if (usersName.isNullOrEmpty()) {
            binding.editNameField.text = userWord.toEditable()
        } else {
            binding.editNameField.text = usersName.toString().toEditable()
        }

        //show user bio in text box
        if (userBio.isNullOrEmpty()) {
            binding.editBio.text = "".toEditable()
        } else {
            binding.editBio.text = userBio.toString().toEditable()
        }

        //show user location in text box
        if (usersLocation.isNullOrEmpty()) {
            binding.editLocationField.text = "".toEditable()
        } else {
            binding.editLocationField.text = usersLocation.toString().toEditable()
        }

        saveInfoInDatabase()

    }



    private fun saveInfoInDatabase() {
        val newUserDetails = User(
            userRepository.getCurrentUser()?.id!!.toInt(),
            userRepository.getCurrentUser()?.email.toString(),
            userRepository.getCurrentUser()?.password.toString(),
            userRepository.getCurrentUser()?.name.toString(),
            userRepository.getCurrentUser()?.location.toString(),
            userRepository.getCurrentUser()?.bio.toString(),
            userRepository.getCurrentUser()?.favoriteLaunches
        )

        binding.btnSave.setOnClickListener {
            if ((userRepository.getCurrentUser()?.name) != binding.editNameField.text.toString()) {
                newUserDetails.name = binding.editNameField.text.toString()
            }
            if ((userRepository.getCurrentUser()?.bio) != binding.editBio.text.toString()) {
                newUserDetails.bio = binding.editBio.text.toString()
            }
            if ((userRepository.getCurrentUser()?.location) != binding.editLocationField.text.toString()) {
                newUserDetails.location = binding.editLocationField.text.toString()
            }

            CoroutineScope(Dispatchers.IO).launch {
                    userRepository.updateUser(newUserDetails)
                }


            parentFragmentManager.beginTransaction()
                .replace(R.id.frame, ProfileFragment())
                .commit()
        }
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

        } else {
            profileLayout.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.white))
            binding.tvBioLabel.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
            binding.tvUserInitial.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
            binding.tvCancelChanges.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
            binding.tvEditProfileLabel.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
            binding.tvLocationLabel.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
            binding.tvNameLabel.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))

        }
    }

}