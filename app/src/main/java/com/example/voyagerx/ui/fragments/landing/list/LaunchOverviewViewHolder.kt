package com.example.voyagerx.ui.fragments.landing.list

import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.RoundedCornersTransformation
import coil.transform.Transformation
import com.example.voyagerx.R
import com.example.voyagerx.databinding.LandingPageOverviewCardBinding
import com.example.voyagerx.repository.model.Launch
import kotlin.math.round

class LaunchOverviewViewHolder(private val binding: LandingPageOverviewCardBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: Launch) {
        binding.launchOverviewCardMissionName.text = item.mission_name
        binding.launchOverviewCardSiteName.text = item.launch_site_long
        binding.launchOverviewCardYear.text = item.launch_year
        binding.launchImage.scaleType = ImageView.ScaleType.CENTER_CROP
        binding.launchImage.load(item.image_links?.getOrNull(0)) {
            transformations(RoundedCornersTransformation(10f,10f,10f,10f))
            fallback(R.drawable.ic_rocket_launch_24)
        }
    }
}