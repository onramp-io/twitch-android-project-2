package com.example.voyagerx

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import coil.load
import coil.size.Precision
import coil.size.Scale
import com.example.voyagerx.data.LaunchDetailBundle
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

    private lateinit var launchObj: Launch

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLaunchDetailsBinding.inflate(inflater, container, false)

        launchObj = Launch(
            arguments?.getString(LaunchDetailBundle.id)!!,
            arguments?.getString(LaunchDetailBundle.missionName),
            arguments?.getString(LaunchDetailBundle.launchSite),
            arguments?.getString(LaunchDetailBundle.launchDate),
            arguments?.getString(LaunchDetailBundle.launchYear),
            arguments?.getString(LaunchDetailBundle.details),
            arguments?.getString(LaunchDetailBundle.articleLink),
            arguments?.getString(LaunchDetailBundle.videoLink),
            arguments?.getStringArray(LaunchDetailBundle.imageLinks)?.toList()
        )

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

    private fun displayLaunchDetails() {
        //launchObj.image_links will return an empty list (instead of null) if no photos are available
        if (!launchObj.image_links.isNullOrEmpty()) {
            var imgUrl = launchObj.image_links?.elementAt(0)
            binding.ivLaunchPictures.let {
                it.scaleType = ImageView.ScaleType.FIT_CENTER
                it.load(imgUrl)
            }
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
