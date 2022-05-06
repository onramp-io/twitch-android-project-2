package com.example.voyagerx.ui.fragments.landing.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.voyagerx.data.LaunchDetailFields
import com.example.voyagerx.databinding.LandingPageOverviewCardBinding
import com.example.voyagerx.helpers.LaunchClickListener
import com.example.voyagerx.repository.model.Launch

class LaunchOverviewAdapter(
    private val listener: LaunchClickListener = LaunchClickListener { },
    private val filterCallback: (Int) -> Unit
) :
    RecyclerView.Adapter<LaunchOverviewViewHolder>() {
    private var visibleLaunches: List<Launch> = listOf()
    private var allLaunches: List<Launch> = listOf()
    private val filters: MutableMap<String, String> = mutableMapOf()
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

    private fun fieldMatchesFilter(field: String, launch: Launch, match: String) = when (field) {
        LaunchDetailFields.launchSite -> launch.launch_site_long == match
        else -> true
    }

    // checks if item matches any of the selected filters or contains the search term
    private fun filterAll() {
        visibleLaunches = allLaunches
            .asSequence()
            .filter { launch ->
                filters.isEmpty() || filters.entries.firstOrNull {
                    fieldMatchesFilter(it.key, launch, it.value)
                } != null
            }
            .filter { filterBySearchTerm(it) }
            .toList()
        notifyDataSetChanged()
        filterCallback(itemCount)
    }

    fun updateSearchTerm(searchTerm: String?) {
        this.searchTerm = searchTerm ?: ""
        filterAll()
    }

    fun addFilter(field: String, match: String) {
        filters[field] = match
        filterAll()
    }

    fun removeFilter(field: String) {
        filters.remove(field)
        filterAll()
    }

    fun hasFilter(field: String): Boolean = filters.contains(field)

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