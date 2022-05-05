package com.example.voyagerx.ui.fragments.userprofileac

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.voyagerx.R
import com.example.voyagerx.databinding.FragmentProfileBinding
import com.example.voyagerx.repository.UserRepository
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.example.voyagerx.util.SharedPreferencesManager
import com.example.voyagerx.ui.fragments.editprofile.EditProfileFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ProfileFragment : Fragment() {
    @Inject
    lateinit var userRepository: UserRepository
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
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        createRecyclerView()
        getUserInfoFromDatabase()

    }


    private fun createRecyclerView() {
        val adapter = FavoritesAdapter(userRepository.getCurrentUser())
        binding.rvUserProfileFavorites.layoutManager = LinearLayoutManager(requireContext())
        binding.rvUserProfileFavorites.adapter = adapter

        if (adapter.itemCount == 0) {
            showEmptyFavoritesRVCase()
        } else {
            hideEmptyFavoritesRVCase()
        }
    }

    private fun hideEmptyFavoritesRVCase() {
        binding.rvUserProfileFavorites.visibility = View.VISIBLE

        binding.emptyFavesCaseCard.visibility = View.INVISIBLE
        binding.ivRocketShip.visibility = View.INVISIBLE
        binding.tvEmptyFavoritesMsg.visibility = View.INVISIBLE

    }

    private fun showEmptyFavoritesRVCase() {
        binding.rvUserProfileFavorites.visibility = View.INVISIBLE
        binding.ivRocketShip.animate().apply {
            duration = 1000
            rotationXBy(360f)
        }.withEndAction {
            binding.ivRocketShip.animate().apply {
                duration = 1000
                rotationBy(3600f)
            }

        }
    }

    private fun getUserInfoFromDatabase() {
        val userWord = "User"
        val userBio: String? = userRepository.getCurrentUser()?.bio
        val usersName: String? = userRepository.getCurrentUser()?.name

        //get first letter of user's name from database; pass to setting function
        if (userRepository.getCurrentUser()?.name.equals(null)) {
                setInitialInAvatar(userWord.first().uppercaseChar())
        } else {
            val firstLetterUserName: Char = userRepository.getCurrentUser()?.name
                .toString()
                .first()
                .uppercaseChar()

            setInitialInAvatar(firstLetterUserName)
        }

        //get user bio from database; pass to setting function
        if (userBio == null) {
            setUserBio(getString(R.string.no_bio_std_message))
        } else {
            setUserBio(userBio)
        }

        //get user name from database; pass to setting function
        if (usersName.equals(null)) {
            setUsernameInHeader(userWord)
        } else {
            setUsernameInHeader(usersName.toString())
        }
    }

    private fun setUsernameInHeader(usersName: String) {
        if ((usersName[usersName.length - 1]) == ('S') || (usersName[usersName.length - 1]) == ('s')) {
            binding.tvNameinProfile.text = getString(R.string.users_profile_heading_last_char_s, usersName)
        } else {
            binding.tvNameinProfile.text = getString(R.string.users_profile_heading, usersName)
        }
    }

    private fun setInitialInAvatar(initial: Char) {
        binding.tvUserInitial.text = initial.toString()
    }

    private fun setUserBio(bio: String) {
        binding.tvBio.text = bio
    }


}

