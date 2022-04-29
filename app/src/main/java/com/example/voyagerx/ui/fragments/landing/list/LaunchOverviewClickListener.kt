package com.example.voyagerx.ui.fragments.landing.list

import com.example.voyagerx.ui.fragments.landing.data.LaunchOverviewData

class LaunchOverviewClickListener(
    private val listener: (launchOverviewData: LaunchOverviewData) -> Unit
) {
    fun onClick(launchOverviewData: LaunchOverviewData) = listener(launchOverviewData)
}