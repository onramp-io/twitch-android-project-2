package com.example.voyagerx.ui.fragments.landing

import android.content.res.Resources
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.fragment.app.Fragment
import com.example.voyagerx.R
import com.example.voyagerx.databinding.FragmentLandingPageBinding


class LandingPageFragment : Fragment() {
    private lateinit var binding: FragmentLandingPageBinding

    companion object {
        const val ANDROID = "android"
        const val DIMEN = "dimen"
        const val STATUS_BAR_HEIGHT = "status_bar_height"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentLandingPageBinding.inflate(inflater)

        // expand recycler view to fill rest of screen after layout is measured
        val listener = object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                expandList()
                binding.landingPageConstraintLayout
                    .viewTreeObserver
                    .removeOnGlobalLayoutListener(this)
            }
        }

        binding.landingPageConstraintLayout.viewTreeObserver.addOnGlobalLayoutListener(listener)

        return binding.root
    }

    /**
     * Set a minimum height for the recycler view, taking into account app/status bars and margins.
     * Doing it programmatically as it seems as though this value will depend on device height.
     */
    private fun expandList() {
        val layoutHeight = binding.root.height
        val searchHeight = convertDpToPx(resources.getDimension(R.dimen.landing_search_height))
        val welcomeCardHeight = convertDpToPx(
            resources.getDimension(R.dimen.landing_welcome_card_height))
        val bottomNavHeight = convertDpToPx(
            requireActivity().findViewById<View>(R.id.bottomNavigationView).height.toFloat())
        val sectionMargin = convertDpToPx(resources.getDimension(R.dimen.landing_section_margin))
        val appBarHeight = with(TypedValue().also {
            requireContext().theme.resolveAttribute(
                android.R.attr.actionBarSize, it, true)}) {
            TypedValue.complexToDimensionPixelSize(this.data, resources.displayMetrics)
        }

        val layoutParams = binding.landingPageLaunchListing.layoutParams
        layoutParams.height = layoutHeight + bottomNavHeight + appBarHeight +
                getStatusBarHeight() - searchHeight - welcomeCardHeight - (sectionMargin * 3)
        binding.landingPageLaunchListing.layoutParams = layoutParams
    }

    private fun convertDpToPx(dp: Float): Int = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp,
            resources.displayMetrics
        ).toInt()

    private fun getStatusBarHeight(): Int {
        val resourceId = resources.getIdentifier(STATUS_BAR_HEIGHT, DIMEN, ANDROID)
        if (resourceId > 0) {
            return resources.getDimensionPixelSize(resourceId)
        }
        return 0
    }
}