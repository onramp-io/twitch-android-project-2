package com.example.voyagerx

import android.content.Intent
import android.util.Log
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.provider.ContactsContract
import androidx.fragment.app.Fragment
import com.example.voyagerx.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import com.example.voyagerx.repository.UserRepository
import com.example.voyagerx.ui.fragments.landing.LandingPageFragment
import com.example.voyagerx.ui.fragments.settings.SettingsFragment
import com.example.voyagerx.ui.fragments.userprofileac.ProfileFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.example.voyagerx.util.FontSizeUtility
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var bottomNavView: BottomNavigationView
    lateinit var binding: ActivityMainBinding

    @Inject
    lateinit var userRepository: UserRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        bottomNavView = binding.bottomNavigationView

        changeFragmentView(LandingPageFragment()) //setting initial view to landing page as you only get here after bypassing login/register screens

        bottomNavView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.landingPageFragment -> changeFragmentView(LandingPageFragment())
                R.id.profileFragment -> changeFragmentView(ProfileFragment())
                R.id.settingsFragment -> changeFragmentView(SettingsFragment())
            }
            true
        }


        isLoggedIn()
        binding.logInOrOutButton.setOnClickListener {
            setLogInOrOutButtonListener(isLoggedIn())
        }

    }

    private fun changeFragmentView(desiredFragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.frame, desiredFragment)
            commit()
        }
    }

    private fun isLoggedIn(): Boolean{
        val button = binding.logInOrOutButton
        return if(userRepository.getCurrentUser() == null){
            button.text = getString(R.string.title_login)
            false
        }else{
            button.text = getString(R.string.title_logout)
            true
        }
    }

    private fun setLogInOrOutButtonListener(isLoggedIn: Boolean){
        if(!isLoggedIn){
            val intent = Intent(this, LoginActivity::class.java).apply {
                putExtra(getString(R.string.intended_login_view), getString(R.string.title_login))
            }
            startActivity(intent)
        }else{
            userRepository.logOut()

            MaterialAlertDialogBuilder(this@MainActivity)
                .setTitle(getString(R.string.logout_message))
                .setPositiveButton(R.string.confrim_dialog){ _, _ -> }
                .show()

            isLoggedIn()
        }
    }

    //temporary function used to update fontScale
    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(FontSizeUtility().adjustFontScale(newBase, 1.0F))
    }
}