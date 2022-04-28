package com.example.voyagerx

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

    private var launchObj : Launch = Launch(
        "1",
        "Thaicom6",
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        displayLaunchDetails()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLaunchDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun displayLaunchDetails() {
        launchObj.mission_name?.let {
            binding.tvMissionName.text = it
        } ?:  run {
            binding.tvMissionName.visibility = View.GONE
        }

        launchObj.launch_site_long?.let {
            binding.tvSiteName.text = it
        } ?: run {
            binding.tvSiteName.visibility = View.GONE
        }

        launchObj.launch_date_utc?.let {
            binding.tvLaunchDate.text = it
        } ?: run {
                binding.tvLaunchDate.visibility = View.GONE
        }

        launchObj.details?.let {
            binding.tvDetailParagraph.text = it
        } ?: run {
            binding.tvDetailHeader.visibility = View.GONE
            binding.tvDetailParagraph.visibility = View.GONE
        }

        if (launchObj.article_link.isNullOrBlank() && launchObj.video_link.isNullOrBlank()) {
            binding.tvLinksHeader.visibility = View.GONE
        }

        launchObj.article_link.isNullOrBlank().let {
            binding.tvArticleLink.visibility = View.GONE
        }

        launchObj.video_link.isNullOrBlank().let {
            binding.tvVideoLink.visibility = View.GONE
        }
    }


}