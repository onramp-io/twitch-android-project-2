package com.example.voyagerx
import android.graphics.Typeface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.example.voyagerx.presenters.LaunchDetailsPresenter
import com.example.voyagerx.repository.UserRepository
import com.example.voyagerx.repository.model.Launch
import com.example.voyagerx.repository.model.User
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
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
    private lateinit var presenter : LaunchDetailsPresenter
    @Inject
    lateinit var userRepository: UserRepository

    private fun setPresenter(presenter: LaunchDetailsPresenter) {
        this.presenter = presenter
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setPresenter(LaunchDetailsPresenter(userRepository,this, requireContext()))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLaunchDetailsBinding.inflate(inflater, container, false)
        presenter.setDetailsLaunchObj(arguments?.getParcelable(Launch.BUNDLE_KEY)!!)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        displayImages(presenter.launchObj)
        displayLaunchDetails(presenter.launchObj)
        presenter.currUser?.let { displayFavorite(it, presenter.launchObj) }
        binding.ivShare.setOnClickListener { presenter.shareLaunch() }
        binding.tvVideoLink.setOnClickListener { presenter.handleLinkClick(presenter.launchObj.video_link) }
        binding.tvArticleLink.setOnClickListener { presenter.handleLinkClick(presenter.launchObj.article_link) }
        binding.ivFavorite.setOnClickListener { presenter.favoriteLaunch() }
        binding.ivFavoriteTrue.setOnClickListener { presenter.unfavoriteLaunch() }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun displayFavoriteSnackBar(message: String) {
        Snackbar.make(binding.ivFavoriteTrue,
            message,
            Snackbar.LENGTH_SHORT)
            .setAnchorView(activity?.findViewById<BottomNavigationView>(R.id.bottomNavigationView))
            .show()
    }

    fun displayFavorite(currUser : User, launchObj: Launch) {
        lifecycleScope.launch { //lifecycle scope is used here since we want to cancel coroutine if fragment is destroyed
            try {
                if (currUser?.favoriteLaunches != null) {
                    if (currUser.favoriteLaunches!!.contains(launchObj)) {
                        // changes to ui need to happen on the main thread
                        launch(Dispatchers.Main) {
                            showView(binding.ivFavoriteTrue)
                        }
                    } else {
                        launch(Dispatchers.Main) {
                            showView(binding.ivFavorite)
                            hideView(binding.ivFavoriteTrue)
                        }
                    }
                }
            } catch (e: Exception){
                Log.d("user", "there was an error checking favorites $e")
            }
        }
    }

    private fun displayLaunchDetails(launchObj: Launch) {
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

    private fun displayImages(launchObj : Launch) {
        //launchObj.image_links will return an empty list (instead of null) if no photos are available
        if (launchObj.image_links.isNullOrEmpty()) {
            showView(binding.ivDefaultImage)
            hideView(binding.vpLaunchPhotos)
        } else {
            displayCarousel(launchObj)
        }
    }

    private fun displayCarousel(launchObj: Launch) {
        val viewPager: ViewPager = binding.vpLaunchPhotos
        val imageAdapter = LaunchCarouselAdapter(
            requireContext(),
            launchObj.image_links
        )
        viewPager.adapter = imageAdapter
    }

    private fun displayNullMessage(message:String, textView:TextView) {
        textView.setTypeface(null, Typeface.ITALIC)
        textView.text = message
    }

    fun displayLoginPopup() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.account_needed_title))
            .setMessage(getString(R.string.need_account_msg))
            .setNeutralButton(resources.getString(R.string.cancel)) { _, _ ->
                //default closes the window
            }
            .setNegativeButton(getString(R.string.register)) { _, _ ->
                val intent = Intent(activity, LoginActivity::class.java).apply {
                    putExtra(getString(R.string.intended_login_view),
                        getString(R.string.title_register))
                }
                startActivity(intent)
            }
            .setPositiveButton(getString(R.string.log_in)) { _, _ ->
                val intent = Intent(activity, LoginActivity::class.java).apply {
                    putExtra(getString(R.string.intended_login_view),
                        getString(R.string.title_login))
                }
                startActivity(intent)
            }
            .show()
    }

    fun displayErrorSnackBar() {
        Snackbar.make(binding.ivFavoriteTrue, getString(R.string.favorite_error), Snackbar.LENGTH_SHORT)
            .setAnchorView(activity?.findViewById<BottomNavigationView>(R.id.bottomNavigationView))
            .show()
    }

    private fun hideView(view: View) {
        view.visibility = View.GONE
    }

    private fun showView(view: View) {
        view.visibility = View.VISIBLE
    }
}
