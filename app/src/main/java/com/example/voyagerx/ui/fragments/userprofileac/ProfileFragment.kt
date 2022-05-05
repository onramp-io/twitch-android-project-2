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
import com.example.voyagerx.repository.model.Launch
import com.example.voyagerx.repository.model.User
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.example.voyagerx.R
import com.example.voyagerx.databinding.FragmentProfileBinding
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

    //temporary launch object stolen from Celina's Details fragment
    private var launchObj : Launch = Launch(
        "1",
        "Thaicom 6",
        "Cape Canaveral Air Force Station Space Launch Complex 40",
        "2020-12-13T17:30:00.000Z",
        "2020",
        "SpaceX will launch Sentinel-6 Michael Freilich into low Earth orbit for NASA, NOAA, ESA, and the European Organization for the Exploitation of Meteorological Satellites aboard a Falcon 9 from SLC-4E, Vandenberg Air Force Station. Sentinel-6(A) is an ocean observation satellite providing radar ocean surface altimetry data and also atmospheric temperature profiles as a secondary mission. The booster for this mission is will land at LZ-4.",
        "https://spaceflightnow.com/2020/11/21/international-satellite-launches-to-extend-measurements-of-sea-level-rise/",
        "https://youtu.be/aVFPzTDCihQ",
        listOf("https://live.staticflickr.com/65535/50630802488_8cc373728e_o.jpg",
            "https://live.staticflickr.com/65535/50631642722_3af8131c6f_o.jpg",
            "https://live.staticflickr.com/65535/50631544171_66bd43eaa9_o.jpg",
            "https://live.staticflickr.com/65535/50631543966_e8035d5cca_o.jpg",
            "https://live.staticflickr.com/65535/50631643257_c214ceee7b_o.jpg",
            "https://live.staticflickr.com/65535/50631643917_cb7db291d0_o.jpg",)
    )
    //placeholder user object to create a user that has a favorited launch
    private var userObj : User = User(
        3,
        "blaaa@faker.com",
        "password",
        "Chris",
        null,
        "Beam me up, Scotty!",
        mutableListOf(launchObj)
    )

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

       //val userLoggedIn = checkIfLoggedIn() - will enable later when login repository is hooked up

        createRecyclerView()
        getUserInfoFromDatabase()
    }

    private fun createRecyclerView() {
        val adapter = FavoritesAdapter(userObj)
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
        val userBio: String? = userObj.bio
        val usersName: String? = userObj.name

        //get first letter of user's name from database; pass to setting function
        if (userObj.name.equals(null)) {
                setInitialInAvatar(userWord.first().uppercaseChar())
        } else {
            val firstLetterUserName: Char = userObj.name
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

//will enable later when login repository is hooked up - need to create login case handling
//private fun checkIfLoggedIn() = loginRepository.isLoggedIn
