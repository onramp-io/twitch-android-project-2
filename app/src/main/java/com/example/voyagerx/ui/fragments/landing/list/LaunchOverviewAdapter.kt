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
    private val launchSiteFilters: MutableSet<String> = mutableSetOf()
    private var searchTerm: String = ""

    fun initializeList(launches: List<Launch>) {
        allLaunches = launches
        visibleLaunches = launches.map { it.copy() }.toMutableList()
        notifyDataSetChanged()
    }

    private fun filterBySearchTerm(launch: Launch): Boolean {
        return if (searchTerm.isNotEmpty()) {
            launch.mission_name?.lowercase()?.contains(searchTerm) ?: true ||
                    launch.launch_site_long?.lowercase()?.contains(searchTerm) ?: true ||
                    launch.launch_date_utc?.lowercase()?.contains(searchTerm) ?: true
        } else {
            true
        }
    }

    private fun filterByLaunchSite(launch: Launch): Boolean {
        return launchSiteFilters.isEmpty() || launchSiteFilters.contains(launch.launch_site_long)
    }

    private fun filterAll() {
        visibleLaunches = allLaunches
            .asSequence()
            .filter(this::filterByLaunchSite)
            .filter { filterBySearchTerm(it) }
            .toList()
        notifyDataSetChanged()
    }

    fun updateSearchTerm(searchTerm: String?) {
        this.searchTerm = searchTerm ?: ""
        filterAll()
    }

    fun addLaunchSiteFilter(site: String) {
        launchSiteFilters.add(site)
        filterAll()
    }

    fun removeLaunchSiteFilter(site: String) {
        launchSiteFilters.remove(site)
        filterAll()
    }

    fun hasLaunchSiteFilter(site: String): Boolean = launchSiteFilters.contains(site)

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

    fun getVisibleSiteNames(): List<String> = visibleLaunches
        .asSequence()
        .map { it.launch_site_long }
        .filterNotNull()
        .distinct()
        .toList()

    override fun getItemCount(): Int = visibleLaunches.size
}