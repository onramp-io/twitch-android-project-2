package com.example.voyagerx.ui.fragments.settings


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import com.example.voyagerx.R
import com.example.voyagerx.databinding.FragmentSettingsBinding
import com.example.voyagerx.util.FontSizeUtility
import com.example.voyagerx.util.SharedPreferencesManager


class SettingsFragment : Fragment() {
    private lateinit var binding: FragmentSettingsBinding
    private val SharedPreferencesManager by lazy { SharedPreferencesManager(requireContext()) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSettingsBinding.inflate(inflater, container, false)


        getSharedPrefs()
        createFontSizesAdapter()
        setFontSize()
        setBackgroundWallpaper()
        setNotifications()

        return binding.root
    }

    private fun getSharedPrefs() {
        binding.switchAppearance.isChecked = SharedPreferencesManager.getBackgroundWallpaper()
        binding.switchNotify.isChecked = SharedPreferencesManager.getLaunchNotifications()
        binding.autoCompleteTextView.setText(SharedPreferencesManager.getTextInDropdown())

    }

    //used to populate font size dropdown with large, medium, and small selections
    private fun createFontSizesAdapter() {
        val fontSizes = resources.getStringArray(R.array.font_sizes)
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.drop_down_font_sizes, fontSizes)
        binding.autoCompleteTextView.setAdapter(arrayAdapter)
    }

    private fun setFontSize() {

        //ignore - needs to be updated with styles when time permits
/*        binding.autoCompleteTextView.setOnClickListener {
            when (binding.autoCompleteTextView.text.toString()) {
                "Large" -> context?.let { large -> FontSizeUtility().adjustFontScale(large, 1.0F) }
                "Medium" -> context?.let { medium -> FontSizeUtility().adjustFontScale(medium,0.9F) }
                "Small" -> context?.let { small -> FontSizeUtility().adjustFontScale(small, 0.8F) }
            }
        }*/

        binding.autoCompleteTextView.setOnClickListener {
            when (binding.autoCompleteTextView.text.toString()) {
                "Large" -> {
                    SharedPreferencesManager.setTextInDropdown("Large")
                }
                "Medium" -> {
                    SharedPreferencesManager.setTextInDropdown("Medium")
                }
                "Small" -> {
                    SharedPreferencesManager.setTextInDropdown("Small")
                }
            }
        }
    }

    private fun setBackgroundWallpaper() {
        binding.switchAppearance.setOnCheckedChangeListener { _, isChecked ->
            if (binding.switchAppearance.isChecked) {
                SharedPreferencesManager.setBackgroundWallpaper(isChecked)
                Log.d("wallPaperSwitch", isChecked.toString())
            } else {
                SharedPreferencesManager.setBackgroundWallpaper(isChecked)
                Log.d("wallPaperSwitch", isChecked.toString())
            }
        }
    }

    private fun setNotifications() {
        binding.switchNotify.setOnCheckedChangeListener { _, isChecked ->
            if (binding.switchNotify.isChecked) {
                SharedPreferencesManager.setLaunchNotifications(isChecked)
                Log.d("notificationsSwitch", isChecked.toString())
            } else {
                SharedPreferencesManager.setBackgroundWallpaper(isChecked)
                Log.d("notificationsSwitch", isChecked.toString())
            }
        }
    }

}