package com.example.voyagerx.ui.fragments.landing.translators

import com.example.voyagerx.repository.model.Launch
import com.example.voyagerx.ui.fragments.landing.LaunchOverviewData

object LaunchOverviewTranslator {
    fun translate(result: List<Launch?>) =
        result.map {
            LaunchOverviewData(
                it?.mission_name ?: "Unknown Mission",
                it?.launch_site_long ?: "Unknown Site",
                it?.launch_year ?: "Unknown Year")
        }
}