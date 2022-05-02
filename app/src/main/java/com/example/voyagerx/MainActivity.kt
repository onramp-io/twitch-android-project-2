package com.example.voyagerx

import android.os.Bundle

import androidx.fragment.app.Fragment
import com.example.voyagerx.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import com.example.voyagerx.ui.fragments.landing.LandingPageFragment
import com.example.voyagerx.ui.fragments.userprofileac.ProfileFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var bottomNavView: BottomNavigationView
    lateinit var binding: ActivityMainBinding

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
    }

    private fun changeFragmentView(desiredFragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.frame, desiredFragment)
            commit()
        }

    }
}