package com.example.voyagerx.ui.fragments.landing.list

import android.os.Build
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.RoundedCornersTransformation
import coil.transform.Transformation
import com.example.voyagerx.R
import com.example.voyagerx.databinding.LandingPageOverviewCardBinding
import com.example.voyagerx.helpers.DateFormatter
import com.example.voyagerx.repository.model.Launch
import kotlin.math.round

class LaunchOverviewViewHolder(private val binding: LandingPageOverviewCardBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: Launch) {
        binding.launchOverviewCardMissionName.text = item.mission_name
        binding.launchOverviewCardSiteName.text = item.launch_site_long
        binding.launchOverviewCardYear.text = DateFormatter.formatLaunchDate(item.launch_date_utc)
        binding.launchImage.scaleType = ImageView.ScaleType.CENTER_CROP
        // placeholder() doesn't support transformations
        // https://github.com/coil-kt/coil/issues/37
        val rounded = RoundedCornersTransformation(10f,10f,10f,10f)
        item.image_links?.getOrNull(0).let {
            when (it) {
                null -> {
                    binding.launchImage.load(R.drawable.spacex_logo) {
                        transformations(rounded)
                    }
                }
                else -> {
                    binding.launchImage.load(it) {
                        transformations(rounded)
                    }
                }
            }
        }
    }

    fun setTextSize(textSize: String) {
        when (textSize) {
            "Large" -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    binding.launchOverviewCardMissionName.setTextAppearance(R.style.rvMissionName)
                    binding.launchOverviewCardSiteName.setTextAppearance(R.style.rvSiteName)
                    binding.launchOverviewCardYear.setTextAppearance(R.style.rvDate)
                }
            }
            "Medium" -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    binding.launchOverviewCardMissionName.setTextAppearance(R.style.rvMissionName_Medium)
                    binding.launchOverviewCardSiteName.setTextAppearance(R.style.rvSiteName_Medium)
                    binding.launchOverviewCardYear.setTextAppearance(R.style.rvDate_Medium)
                }
            }
            else -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    binding.launchOverviewCardMissionName.setTextAppearance(R.style.rvMissionName_Small)
                    binding.launchOverviewCardSiteName.setTextAppearance(R.style.rvSiteName_Small)
                    binding.launchOverviewCardYear.setTextAppearance(R.style.rvDate_Small)
                }
            }
        }
    }
}

