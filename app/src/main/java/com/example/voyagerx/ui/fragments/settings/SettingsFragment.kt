package com.example.voyagerx.ui.fragments.settings

import android.content.Context
import android.content.SharedPreferences
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.appcompat.content.res.AppCompatResources
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import com.example.voyagerx.MainActivity
import com.example.voyagerx.R
import com.example.voyagerx.databinding.FragmentSettingsBinding
import com.example.voyagerx.util.FontSizeUtility
//import com.example.voyagerx.util.SharedPreferencesManager


class SettingsFragment : Fragment() {
    private lateinit var binding: FragmentSettingsBinding
    //private val sharedPreferencesManager: SharedPreferencesManager = SharedPreferencesManager()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSettingsBinding.inflate(inflater, container, false)


        saveSharedPreferences()
        createFontSizesAdapter()
        setFontSize()
        setBackgroundWallpaper()
        return binding.root
    }



    private fun saveSharedPreferences() {
        val switchAppearanceValue: Boolean = binding.switchAppearance.isChecked
        val textSize: String = binding.autoCompleteTextView.text.toString()
        val switchNotifyValue: Boolean = binding.switchNotify.isChecked

        val sharedPreferences: SharedPreferences = requireActivity().
            getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()

        editor.apply {
            putBoolean("APPEARANCE_KEY", switchAppearanceValue)
            putString("TEXT_SIZE_KEY", textSize)
            putBoolean("NOTIFY_KEY", switchNotifyValue)
        }.apply()
    }


    private fun createFontSizesAdapter() {
        val fontSizes = resources.getStringArray(R.array.font_sizes)
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.drop_down_font_sizes, fontSizes)
        binding.autoCompleteTextView.setAdapter(arrayAdapter)
    }

    private fun setFontSize() {
        binding.autoCompleteTextView.setOnClickListener {
            when (binding.autoCompleteTextView.text.toString()) {
                "Large" -> context?.let { large -> FontSizeUtility().adjustFontScale(large, 1.0F) }
                "Medium" -> context?.let { medium -> FontSizeUtility().adjustFontScale(medium,0.9F) }
                "Small" -> context?.let { small -> FontSizeUtility().adjustFontScale(small, 0.8F) }
            }
        }
    }

    private fun setBackgroundWallpaper() {
        val settingsLayout: ConstraintLayout = binding.settingsLayout

        binding.switchAppearance.setOnCheckedChangeListener { _, _ ->
            if (binding.switchAppearance.isChecked) {
                settingsLayout.background =
                    ContextCompat.getDrawable(requireContext(), R.drawable.rocket_background)
            } else {
                settingsLayout.background = ContextCompat.getDrawable(requireContext(), R.drawable.stars_background)
            }
        }
    }



}