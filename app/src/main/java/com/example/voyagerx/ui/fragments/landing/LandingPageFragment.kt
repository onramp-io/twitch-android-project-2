package com.example.voyagerx.ui.fragments.landing

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.voyagerx.R
import com.example.voyagerx.databinding.FragmentLandingPageBinding
import com.example.voyagerx.repository.LaunchRepository
import com.example.voyagerx.repository.model.Launch
import com.example.voyagerx.ui.fragments.landing.list.LaunchOverviewAdapter
import com.example.voyagerx.ui.fragments.landing.list.LaunchOverviewClickListener
import com.example.voyagerx.ui.fragments.landing.translators.LaunchOverviewTranslator


class LandingPageFragment : Fragment() {
    private val launchOverviewTranslator: LaunchOverviewTranslator = LaunchOverviewTranslator
    private lateinit var binding: FragmentLandingPageBinding
    private var launchData: List<LaunchOverviewData> = listOf()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentLandingPageBinding.inflate(inflater)

        if (launchData.isEmpty()) {
            showSpinner()
        }
        hideNetworkError()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupList()
    }

    private fun setupList() {
        val adapter = LaunchOverviewAdapter(LaunchOverviewClickListener {
            Log.i("LandingPageFragment","$it.missionName clicked.")
        })
        binding.listing.list.adapter = adapter

        viewLifecycleOwner.lifecycleScope.launchWhenResumed {
            val result = LaunchRepository.getLaunches()
            hideSpinner()

            if (result != null) {
                launchData = launchOverviewTranslator.translate(result)
                adapter.initializeList(launchData)
                setListHeaderText(launchData.size)
            } else {
                showNetworkError()
            }
        }
    }

    private fun showSpinner() {
        binding.listing.spinner.visibility = View.VISIBLE
    }

    private fun hideSpinner() {
        binding.listing.spinner.visibility = View.INVISIBLE
    }

    private fun setListHeaderText(amount: Int) {
        binding.listing.header.text = resources.getString(R.string.launch_listing_header, amount)
        binding.listing.header.visibility = View.VISIBLE
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