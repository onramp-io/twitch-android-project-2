package com.example.voyagerx.ui.fragments.userprofileac

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.voyagerx.repository.UserRepository
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentTransaction
import com.example.voyagerx.LaunchDetailsFragment
import com.example.voyagerx.data.LaunchDetailFields
import com.example.voyagerx.helpers.LaunchClickListener
import com.example.voyagerx.repository.model.Launch
import com.example.voyagerx.R
import com.example.voyagerx.databinding.FragmentProfileBinding
import com.example.voyagerx.util.SharedPreferencesManager
import com.example.voyagerx.ui.fragments.editprofile.EditProfileFragment
import com.example.voyagerx.ui.fragments.landing.list.LaunchOverviewAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
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
        setFontSizes()

        return binding.root
    }

    private fun setProfileBackgroundWallpaper(wallpaper: Boolean) {
        if (wallpaper) {
            binding.headerCoverImage.setImageResource(R.drawable.stars_background)
        } else {
            binding.headerCoverImage.setImageResource(R.drawable.plain_background)
        }
    }

    private fun setFontSizes() {
        when {
            SharedPreferencesManager.getTextInDropdown() == "Large" -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    binding.tvNameinProfile.setTextAppearance(R.style.profileFontSize)
                }
            }
            SharedPreferencesManager.getTextInDropdown() == "Medium" -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    binding.tvNameinProfile.setTextAppearance(R.style.profileFontSize_Medium)
                }
            }
            else -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    binding.tvNameinProfile.setTextAppearance(R.style.profileFontSize_Small)
                }
            }
        }

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        createRecyclerView()
        getUserInfoFromDatabase()

    }

    private fun createRecyclerView() {
        val adapter = FavoritesAdapter(userRepository.getCurrentUser(), LaunchClickListener(this::navigateToLaunchDetails))

        binding.rvUserProfileFavorites.layoutManager = LinearLayoutManager(requireContext())
        binding.rvUserProfileFavorites.adapter = adapter

        if (adapter.itemCount == 0) {
            showEmptyFavoritesRVCase()
        } else {
            hideEmptyFavoritesRVCase()
        }
    }

    private fun navigateToLaunchDetails(launch: Launch) {
        val bundle = Bundle()
        bundle.apply {
            putString(LaunchDetailFields.id, launch.id)
            putString(LaunchDetailFields.missionName, launch.mission_name)
            putString(LaunchDetailFields.launchSite, launch.launch_site_long)
            putString(LaunchDetailFields.launchDate, launch.launch_date_utc)
            putString(LaunchDetailFields.launchYear, launch.launch_year)
            putString(LaunchDetailFields.details, launch.details)
            putString(LaunchDetailFields.articleLink, launch.article_link)
            putString(LaunchDetailFields.videoLink, launch.video_link)
            putStringArray(LaunchDetailFields.imageLinks, launch.image_links?.toTypedArray())
        }

        val launchDetailsFragment = LaunchDetailsFragment()
        launchDetailsFragment.arguments = bundle

        // Button animation then fragment transition?
        parentFragmentManager.beginTransaction()
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            .replace(R.id.frame, launchDetailsFragment)
            .commit()
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
        val usersLocation: String? = userRepository.getCurrentUser()?.location
        Log.d("getuserInfo","$usersName")
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

        //get user location from database; pass to setting function
        if (usersLocation.equals(null)) {
            setUsersLocation(getString(R.string.default_location))
        } else {
            setUsersLocation(usersLocation.toString())
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

    private fun setUsersLocation(location: String) {
        binding.tvLocation.text = location
    }



}

