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
import com.example.voyagerx.ui.fragments.landing.list.LaunchOverviewAdapter
import com.example.voyagerx.helpers.LaunchClickListener
import com.example.voyagerx.repository.model.Launch


class LandingPageFragment :
    Fragment() {
    private lateinit var binding: FragmentLandingPageBinding
    private lateinit var launches: List<Launch>

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
    }

    private fun setupList() {
        showSpinner()

        val adapter = LaunchOverviewAdapter(LaunchClickListener {
            Log.i("LandingPageFragment", "$it.missionName clicked.")
        })
        binding.listing.list.adapter = adapter

        viewLifecycleOwner.lifecycleScope.launchWhenResumed {
            val result = LaunchRepository.getLaunches()
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