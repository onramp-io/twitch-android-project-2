package com.example.voyagerx

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.ViewPager
import coil.load
import com.example.voyagerx.adapters.LaunchCarouselAdapter
import com.example.voyagerx.databinding.FragmentLaunchDetailsBinding
import com.example.voyagerx.repository.model.Launch

/**
 * A simple [Fragment] subclass.
 * Use the [LaunchDetailsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LaunchDetailsFragment : Fragment() {

    private var _binding: FragmentLaunchDetailsBinding? = null
    private val binding get() = _binding!!

    //placeholder data until we get landing page connected
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
            "https://live.staticflickr.com/65535/50631643917_cb7db291d0_o.jpg")
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLaunchDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        displayLaunchDetails()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun displayCarousel() {
        val viewPager :ViewPager = binding.vpLaunchPhotos
        val imageAdapter = LaunchCarouselAdapter(requireContext(),
            launchObj.image_links
        )
        viewPager.adapter = imageAdapter
    }

    private fun displayLaunchDetails() {
        //launchObj.image_links will return an empty list (instead of null) if no photos are available
        if (launchObj.image_links.isNullOrEmpty()) {
            binding.ivDefaultImage.visibility = ImageView.VISIBLE
            hideView(binding.vpLaunchPhotos)
        } else {
            displayCarousel()
        }

        launchObj.mission_name?.let {
            binding.tvMissionName.text = it
        } ?:  run {
            hideView(binding.tvMissionName)
        }

        launchObj.launch_site_long?.let {
            binding.tvSiteName.text = it
        } ?: run {
            hideView(binding.tvSiteName)
        }

        launchObj.launch_date_utc?.let {
            binding.tvLaunchDate.text = it
        } ?: run {
            hideView(binding.tvLaunchDate)
        }

        launchObj.details?.let {
            binding.tvDetailParagraph.text = it
        } ?: run {
            hideView(binding.tvDetailHeader)
            hideView(binding.tvDetailParagraph)
        }

        if (launchObj.article_link.isNullOrBlank() && launchObj.video_link.isNullOrBlank()) {
            hideView(binding.tvLinksHeader)
        }

        if (launchObj.article_link.isNullOrBlank()) {
            hideView(binding.tvArticleLink)
        }

        if (launchObj.video_link.isNullOrBlank()) {
            hideView(binding.tvVideoLink)
        }
    }

    private fun hideView(view: View) {
        view.visibility = View.GONE
    }
}
