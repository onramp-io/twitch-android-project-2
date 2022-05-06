package com.example.voyagerx
import android.graphics.Typeface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.viewpager.widget.ViewPager
import com.example.voyagerx.adapters.LaunchCarouselAdapter
import coil.size.Precision
import coil.size.Scale
import com.example.voyagerx.data.LaunchDetailFields
import com.example.voyagerx.databinding.FragmentLaunchDetailsBinding
import com.example.voyagerx.helpers.DateFormatter
import com.example.voyagerx.repository.UserRepository
import com.example.voyagerx.repository.model.Launch
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 * Use the [LaunchDetailsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class LaunchDetailsFragment : Fragment() {

    private var _binding: FragmentLaunchDetailsBinding? = null
    private val binding get() = _binding!!

    private lateinit var launchObj: Launch

    @Inject
    lateinit var userRepository: UserRepository

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLaunchDetailsBinding.inflate(inflater, container, false)

        launchObj = Launch(
            arguments?.getString(LaunchDetailFields.id)!!,
            arguments?.getString(LaunchDetailFields.missionName),
            arguments?.getString(LaunchDetailFields.launchSite),
            arguments?.getString(LaunchDetailFields.launchDate),
            arguments?.getString(LaunchDetailFields.launchYear),
            arguments?.getString(LaunchDetailFields.details),
            arguments?.getString(LaunchDetailFields.articleLink),
            arguments?.getString(LaunchDetailFields.videoLink),
            arguments?.getStringArray(LaunchDetailFields.imageLinks)?.toList()
        )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        displayImages()
        displayLaunchDetails()
        displayFavorite()
        binding.ivFavorite.setOnClickListener { handleFavoriteClick() }
        binding.ivShare.setOnClickListener { shareLaunch() }
        binding.tvVideoLink.setOnClickListener { handleLinkClick(launchObj.video_link) }
        binding.tvArticleLink.setOnClickListener { handleLinkClick(launchObj.article_link) }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun displayLaunchDetails() {
        launchObj.mission_name?.let {
            binding.tvMissionName.text = it
        } ?: run {
            binding.tvMissionName.let{
                displayNullMessage(getString(R.string.no_mission_msg), it)
                it.textSize = 20F //sets text size to 20 sp
            }
        }

        launchObj.launch_site_long?.let {
            binding.tvSiteName.text = it
        } ?: run {
            displayNullMessage(getString(R.string.no_location_msg), binding.tvSiteName)
        }

        launchObj.launch_date_utc?.let {
            binding.tvLaunchDate.text = DateFormatter.formatLaunchDate(launchObj.launch_date_utc)
        } ?: run {
            displayNullMessage(getString(R.string.no_date_msg), binding.tvLaunchDate)
        }

        launchObj.details?.let {
            binding.tvDetailParagraph.text = it
        } ?: run {
            displayNullMessage(getString(R.string.no_details_msg), binding.tvDetailParagraph)
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


    private fun displayNullMessage(message:String, textView:TextView) {
        textView.setTypeface(null, Typeface.ITALIC)
        textView.text = message
    }

    private fun displayImages() {
        //launchObj.image_links will return an empty list (instead of null) if no photos are available
        if (launchObj.image_links.isNullOrEmpty()) {
            binding.ivDefaultImage.visibility = ImageView.VISIBLE
            hideView(binding.vpLaunchPhotos)
        } else {
            displayCarousel()
        }
    }

    private fun displayCarousel() {
        val viewPager: ViewPager = binding.vpLaunchPhotos
        val imageAdapter = LaunchCarouselAdapter(
            requireContext(),
            launchObj.image_links
        )
        viewPager.adapter = imageAdapter
    }

    private fun shareLaunch() {
        val share = Intent.createChooser(Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(
                Intent.EXTRA_TEXT, "Check out this SpaceX Launch!\n\n" +
                        if (!launchObj.mission_name.isNullOrEmpty()) "${launchObj.mission_name}\n\n" else "" +
                                if (!launchObj.details.isNullOrEmpty()) "${launchObj.details}\n\n" else "" +
                                        if (!launchObj.video_link.isNullOrEmpty()) "Watch the launch video:\n ${launchObj.video_link}" else ""
            )
            type = "text/plain"
        }, null)
        startActivity(share)
    }

    private fun displayFavorite() {
        lifecycleScope.launch { //lifecycle scope is used here since we want to cancel coroutine if fragment is destroyed
            try {
                var currUser = userRepository.getCurrentUser()
                if (currUser?.favoriteLaunches != null) {
                    if (currUser.favoriteLaunches!!.contains(launchObj)) {
                        // changes to ui need to happen on the main thread
                         launch(Dispatchers.Main) {
                            binding.ivFavoriteTrue.visibility = ImageView.VISIBLE
                        }
                    }
                }
            } catch (e: Exception){
                Log.d("user", "there was an error checking favorites $e")
            }
        }
    }

    private fun handleLinkClick(link : String?) {
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
        startActivity(browserIntent)
    }


    private fun handleFavoriteClick() {
        var currUser = userRepository.getCurrentUser()
        (if (currUser == null) {
            showLoginPopup()
            }
        )
    }

    private fun showLoginPopup() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.account_needed_title))
            .setMessage(getString(R.string.need_account_msg))
            .setNeutralButton(resources.getString(R.string.cancel)) { _, _ ->
                //default closes the window
            }
            .setNegativeButton(getString(R.string.register)) { _, _ ->
                val intent = Intent(activity, LoginActivity::class.java).apply {
                    putExtra(getString(R.string.intended_login_view), getString(R.string.title_register))
                }
                startActivity(intent)
            }
            .setPositiveButton(getString(R.string.log_in)) { _, _ ->
                val intent = Intent(activity, LoginActivity::class.java).apply {
                    putExtra(getString(R.string.intended_login_view), getString(R.string.title_login))
                }
                startActivity(intent)
            }
            .show()
    }
}
