package com.example.voyagerx.ui.fragments.landing.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.voyagerx.databinding.LandingPageOverviewCardBinding
import com.example.voyagerx.helpers.LaunchClickListener
import com.example.voyagerx.repository.model.Launch

class LaunchOverviewAdapter(private val listener: LaunchClickListener = LaunchClickListener { }) :
    RecyclerView.Adapter<LaunchOverviewViewHolder>() {
    private var visibleLaunches: List<Launch> = listOf()
    private var allLaunches: List<Launch> = listOf()

    fun initializeList(launches: List<Launch>) {
        allLaunches = launches
        visibleLaunches = launches.map { it.copy() }.toMutableList()
        notifyDataSetChanged()
    }

    fun filter(searchTerm: String?) {
        visibleLaunches = if (searchTerm?.isNotEmpty() == true) {
            allLaunches.filter {
                it.mission_name?.lowercase()?.contains(searchTerm) ?: true ||
                        it.launch_site_long?.lowercase()?.contains(searchTerm) ?: true ||
                        it.launch_year?.lowercase()?.contains(searchTerm) ?: true
            }
        } else {
            allLaunches
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LaunchOverviewViewHolder =
        LaunchOverviewViewHolder(
            LandingPageOverviewCardBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )

    override fun onBindViewHolder(holder: LaunchOverviewViewHolder, position: Int) {
        val launch = visibleLaunches[position]
        holder.bind(launch)
        holder.itemView.setOnClickListener {
            listener.onClick(launch)
        }
    }

    override fun getItemCount(): Int = visibleLaunches.size
}