package com.example.voyagerx.ui.fragments.landing.list

import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.voyagerx.R
import com.example.voyagerx.databinding.LandingPageOverviewCardBinding
import com.example.voyagerx.repository.model.Launch

class LaunchOverviewViewHolder(private val binding: LandingPageOverviewCardBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: Launch) {
        binding.launchOverviewCardMissionName.text = item.mission_name
        binding.launchOverviewCardSiteName.text = item.launch_site_long
        binding.launchOverviewCardYear.text = item.launch_year
        binding.launchImage.scaleType = ImageView.ScaleType.FIT_CENTER
        binding.launchImage.load("https://live.staticflickr.com/65535/50630802488_8cc373728e_o.jpg") {
            // placeholder image is the image used
            // when our image url fails to load.
            placeholder(R.drawable.ic_rocket_launch_24)
        }
    }
}