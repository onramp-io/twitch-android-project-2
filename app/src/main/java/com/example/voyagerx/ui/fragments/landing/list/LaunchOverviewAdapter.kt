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
    // map of an item field to a list of matches to filter on
    private val filters: MutableMap<String, MutableList<String>> = mutableMapOf()
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

    private fun fieldMatchesFilters(field: String, launch: Launch, matches: List<String>) =
        when (field) {
            LaunchDetailFields.launchSite -> matches.isEmpty() ||
                    matches.contains(launch.launch_site_long)
            else -> true
        }

    // checks if item matches any of the selected filters or contains the search term
    private fun filterAll() {
        visibleLaunches = allLaunches
            .asSequence()
            .filter { launch ->
                filters.isEmpty() || filters.entries.firstOrNull {
                    fieldMatchesFilters(it.key, launch, it.value)
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
        if (filters[field] == null) {
            filters[field] = mutableListOf(match)
        } else {
            filters[field]?.add(match)
        }
        filterAll()
    }

    fun removeFilter(field: String, match: String) {
        filters[field]?.remove(match)
        filterAll()
    }

    fun hasFilter(field: String, match: String): Boolean = filters[field]?.contains(match) ?: false

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

    fun getSiteFilters(): List<String> = allLaunches
        .asSequence()
        .map { it.launch_site_long }
        .distinct()
        .filterNot {
            filters[LaunchDetailFields.launchSite]?.contains(it) ?: false
        }
        .filterNotNull()
        .toList()

    override fun getItemCount(): Int = visibleLaunches.size
}