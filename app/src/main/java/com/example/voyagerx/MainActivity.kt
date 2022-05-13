package com.example.voyagerx

import android.content.Intent
import android.util.Log
import android.content.Context
import android.os.Bundle

import android.provider.ContactsContract
import androidx.fragment.app.Fragment
import com.example.voyagerx.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import com.example.voyagerx.repository.UserRepository
import com.example.voyagerx.ui.fragments.editprofile.EditProfileFragment
import com.example.voyagerx.ui.fragments.landing.LandingPageFragment
import com.example.voyagerx.ui.fragments.settings.SettingsFragment
import com.example.voyagerx.ui.fragments.userprofileac.Profile2Fragment
import com.example.voyagerx.ui.fragments.userprofileac.ProfileFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.example.voyagerx.util.FontSizeUtility
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var bottomNavView: BottomNavigationView
    lateinit var binding: ActivityMainBinding

    // use same instance of fragment during the activity lifecycle
    private val landingPageFragment: LandingPageFragment = LandingPageFragment()

    @Inject
    lateinit var userRepository: UserRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        bottomNavView = binding.bottomNavigationView

        // don't re-attach the fragment again if onSaveInstanceState was called, otherwise
        // onViewCreated will be called twice for this fragment using an empty bundle
        // https://stackoverflow.com/questions/10983396/fragment-oncreateview-and-onactivitycreated-called-twice
        if (savedInstanceState == null) {
            changeFragmentView(landingPageFragment) //setting initial view to landing page as you only get here after bypassing login/register screens
        }

        bottomNavView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.landingPageFragment -> changeFragmentView(landingPageFragment)
                R.id.profileFragment -> if(userRepository.getCurrentUser() == null) showLoginPopup() else changeFragmentView(ProfileFragment())
                R.id.settingsFragment -> changeFragmentView(SettingsFragment())
            }
            true
        }

        setLogInOrLogoutButtonText()
        binding.logInOrOutButton.setOnClickListener {
            setLogInOrOutButtonListener()
        }

    }

    private fun changeFragmentView(desiredFragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.frame, desiredFragment)
            commit()
        }
    }

    private fun setLogInOrLogoutButtonText(){
        val button = binding.logInOrOutButton
        if(userRepository.getCurrentUser() == null){
            button.text = getString(R.string.title_login)
        }else{
            button.text = getString(R.string.title_logout)
        }
    }

    private fun setLogInOrOutButtonListener(){
        if(userRepository.getCurrentUser() == null){
            val intent = Intent(this, LoginActivity::class.java).apply {
                putExtra(getString(R.string.intended_login_view), getString(R.string.title_login))
            }
            startActivity(intent)
        }else{
            userRepository.logOut()

            MaterialAlertDialogBuilder(this@MainActivity)
                .setTitle(getString(R.string.logout_message))
                .setPositiveButton(R.string.confirm_dialog){ _, _ -> }
                .show()

            checkIfLogoutProfileFragment()
            setLogInOrLogoutButtonText()
        }
    }

    private fun checkIfLogoutProfileFragment(){
        val currFragment = supportFragmentManager.findFragmentById(R.id.frame)
        if(currFragment is ProfileFragment || currFragment is EditProfileFragment){
            changeFragmentView(LandingPageFragment())
        }

    }
    
    private fun showLoginPopup() {
        MaterialAlertDialogBuilder(this@MainActivity)
            .setTitle(getString(R.string.account_needed_title))
            .setMessage(getString(R.string.need_account_msg_profile))
            .setNeutralButton(resources.getString(R.string.cancel)) { dialog, which ->
                //default closes the window
            }
            .setNegativeButton(getString(R.string.register)) { dialog, which ->
                val intent = Intent(this, LoginActivity::class.java).apply {
                    putExtra(getString(R.string.intended_login_view), getString(R.string.title_register))
                }
                startActivity(intent)
            }
            .setPositiveButton(getString(R.string.log_in)) { dialog, which ->
                val intent = Intent(this, LoginActivity::class.java).apply {
                    putExtra(getString(R.string.intended_login_view), getString(R.string.title_login))
                }
                startActivity(intent)
            }
            .show()
    }
}