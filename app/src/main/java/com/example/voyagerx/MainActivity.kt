package com.example.voyagerx

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.voyagerx.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity() {
    private lateinit var bottomNavView : BottomNavigationView
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        bottomNavView = binding.bottomNavigationView

        val landingPageFragment = LandingPageFragment()
        val profileFragment = ProfileFragment()
        val settingsFragment = SettingsFragment()

        changeFragmentView(landingPageFragment) //setting initial view to landing page as you only get here after bypassing login/register screens

        bottomNavView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.landingPageFragment -> changeFragmentView(landingPageFragment)
                R.id.profileFragment -> changeFragmentView(profileFragment)
                R.id.settingsFragment -> changeFragmentView(settingsFragment)
            }
            true
        }
    }

    private fun changeFragmentView(desiredFragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.frame, desiredFragment)
            commit()
        }
}