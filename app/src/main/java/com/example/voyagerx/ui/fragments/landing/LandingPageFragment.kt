package com.example.voyagerx.ui.fragments.landing

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.lifecycleScope
import com.example.voyagerx.LaunchDetailsFragment
import com.example.voyagerx.R
import com.example.voyagerx.data.LaunchDetailBundle
import com.example.voyagerx.databinding.FragmentLandingPageBinding
import com.example.voyagerx.helpers.LaunchClickListener
import com.example.voyagerx.repository.LaunchRepository
import com.example.voyagerx.repository.model.Launch
import com.example.voyagerx.ui.fragments.landing.list.LaunchOverviewAdapter
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class LandingPageFragment: Fragment() {

    @Inject
    lateinit var launchRepository: LaunchRepository

    private lateinit var binding: FragmentLandingPageBinding
    private lateinit var launches: List<Launch>
    private lateinit var adapter: LaunchOverviewAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentLandingPageBinding.inflate(inflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        hideNetworkError()

        if (!this::launches.isInitialized) {
            setupList()
        }

        binding.filters.search.setOnQueryTextListener(object : OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                adapter.filter(p0?.lowercase())
                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                adapter.filter(p0?.lowercase())
                return false
            }
        })
    }

    private fun navigateToLaunchDetails(launch: Launch) {
        val bundle = Bundle()
        bundle.apply {
            putString(LaunchDetailBundle.id, launch.id)
            putString(LaunchDetailBundle.missionName, launch.mission_name)
            putString(LaunchDetailBundle.launchSite, launch.launch_site_long)
            putString(LaunchDetailBundle.launchDate, launch.launch_date_utc)
            putString(LaunchDetailBundle.launchYear, launch.launch_year)
            putString(LaunchDetailBundle.details, launch.details)
            putString(LaunchDetailBundle.articleLink, launch.article_link)
            putString(LaunchDetailBundle.videoLink, launch.video_link)
            putStringArray(LaunchDetailBundle.imageLinks, launch.image_links?.toTypedArray())
        }

        val launchDetailsFragment = LaunchDetailsFragment()
        launchDetailsFragment.arguments = bundle

        // Button animation then fragment transition?
        parentFragmentManager.beginTransaction()
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            .replace(R.id.frame, launchDetailsFragment)
            .commit()
    }

    private fun setupList() {
        showSpinner()

        // Add button press animation
        adapter = LaunchOverviewAdapter(LaunchClickListener(this::navigateToLaunchDetails))
        binding.listing.list.adapter = adapter

        viewLifecycleOwner.lifecycleScope.launchWhenResumed {
            val result = launchRepository.getLaunches()
            hideSpinner()

            if (result != null) {
                launches = result.filterNotNull()
                adapter.initializeList(launches)
                setListHeaderText(launches.size)
            } else {
                showNetworkError()
            }
        }
    }

    private fun setListHeaderText(amount: Int) {
        binding.listing.header.text = resources.getString(R.string.launch_listing_header, amount)
        binding.listing.header.visibility = View.VISIBLE
    }

    private fun showSpinner() {
        binding.listing.spinner.visibility = View.VISIBLE
    }

    private fun hideSpinner() {
        binding.listing.spinner.visibility = View.INVISIBLE
    }

    private fun showNetworkError() {
        binding.error.root.visibility = View.VISIBLE
        binding.listing.root.visibility = View.INVISIBLE
        binding.welcome.visibility = View.INVISIBLE
        binding.filters.root.visibility = View.INVISIBLE
    }

    private fun hideNetworkError() {
        binding.error.root.visibility = View.INVISIBLE
        binding.listing.root.visibility = View.VISIBLE
        binding.welcome.visibility = View.VISIBLE
        binding.filters.root.visibility = View.VISIBLE
    }
}