package com.example.voyagerx.ui.fragments.landing.list

import androidx.recyclerview.widget.RecyclerView
import com.example.voyagerx.databinding.LandingPageOverviewCardBinding
import com.example.voyagerx.repository.model.Launch

class LaunchOverviewViewHolder(private val binding: LandingPageOverviewCardBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: Launch) {
        binding.launchOverviewCardMissionName.text = item.mission_name
        binding.launchOverviewCardSiteName.text = item.launch_site_long
        binding.launchOverviewCardYear.text = item.launch_year
    }
}