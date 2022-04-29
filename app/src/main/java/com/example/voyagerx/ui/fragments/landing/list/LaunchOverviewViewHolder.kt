package com.example.voyagerx.ui.fragments.landing.list

import androidx.recyclerview.widget.RecyclerView
import com.example.voyagerx.databinding.LandingPageOverviewCardBinding
import com.example.voyagerx.ui.fragments.landing.data.LaunchOverviewData

class LaunchOverviewViewHolder(private val binding: LandingPageOverviewCardBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: LaunchOverviewData) {
        binding.launchOverviewCardMissionName.text = item.missionName
        binding.launchOverviewCardSiteName.text = item.siteName
        binding.launchOverviewCardYear.text = item.year
    }
}