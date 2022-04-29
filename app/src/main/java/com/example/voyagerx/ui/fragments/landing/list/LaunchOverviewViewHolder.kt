package com.example.voyagerx.ui.fragments.landing.list

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.voyagerx.R
import com.example.voyagerx.ui.fragments.landing.LaunchOverviewData

class LaunchOverviewViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val missionName: TextView =
        itemView.findViewById(R.id.launch_overview_card_mission_name)
    private val siteName: TextView = itemView.findViewById(R.id.launch_overview_card_site_name)
    private val year: TextView = itemView.findViewById(R.id.launch_overview_card_year)

    fun bind(item: LaunchOverviewData) {
        missionName.text = item.missionName
        siteName.text = item.siteName
        year.text = item.year.toString()
    }
}