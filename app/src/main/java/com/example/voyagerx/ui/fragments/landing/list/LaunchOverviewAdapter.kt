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
    val filtersAnyMatch: MutableMap<String, MutableList<String>> = mutableMapOf()
    val filtersAllMatch: MutableMap<String, MutableList<String>> = mutableMapOf()
    var searchTerm: String = ""

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

    private fun fieldMatchesFilters(field: String, launch: Launch, matches: List<String>): Boolean =
        when (field) {
            LaunchDetailFields.launchSite -> launch.launch_site_long in matches
            LaunchDetailFields.articleLink -> launch.article_link?.isNotEmpty() ?: false
            LaunchDetailFields.imageLinks -> launch.image_links?.isNotEmpty() ?: false
            LaunchDetailFields.videoLink -> launch.video_link?.isNotEmpty() ?: false
            else -> true
        }

    // checks if item matches any of the selected filters or contains the search term
    private fun filterAll() {
        visibleLaunches = allLaunches
            .asSequence()
            .filter { launch ->
                // find first filter that item passes and pass item through filter
                filtersAnyMatch.isEmpty() || filtersAnyMatch.entries.firstOrNull { filterToMatches ->
                    filterToMatches.value.isEmpty() ||
                            fieldMatchesFilters(filterToMatches.key, launch, filterToMatches.value)
                } != null
            }
            .filter { launch ->
                // find first filter that item does not pass, otherwise pass item through filter
                filtersAllMatch.isEmpty() || filtersAllMatch.entries.firstOrNull {
                        filterToMatches ->
                    !fieldMatchesFilters(filterToMatches.key, launch, filterToMatches.value)
                } == null
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

    fun addAnyFilter(field: String, match: String) {
        if (filtersAnyMatch[field] == null) {
            filtersAnyMatch[field] = mutableListOf(match)
        } else {
            filtersAnyMatch[field]?.add(match)
        }
        filterAll()
    }

    fun addAllFilter(field: String, match: String) {
        if (filtersAllMatch[field] == null) {
            filtersAllMatch[field] = mutableListOf(match)
        } else {
            filtersAllMatch[field]?.add(match)
        }
        filterAll()
    }

    fun removeAnyFilter(field: String, match: String) {
        filtersAnyMatch[field]?.remove(match)
        if (filtersAnyMatch[field]?.isEmpty() == true) {
            filtersAnyMatch.remove(field)
        }
        filterAll()
    }

    fun removeAllFilter(field: String, match: String) {
        filtersAllMatch[field]?.remove(match)
        if (filtersAllMatch[field]?.isEmpty() == true) {
            filtersAllMatch.remove(field)
        }
        filterAll()
    }

    fun hasAnyFilter(field: String, match: String): Boolean =
        filtersAnyMatch[field]?.contains(match) ?: false

    fun hasAllFilter(field: String, match: String): Boolean =
        filtersAllMatch[field]?.contains(match) ?: false

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
            filtersAnyMatch[LaunchDetailFields.launchSite]?.contains(it) ?: false
        }
        .filterNotNull()
        .toList()

    override fun getItemCount(): Int = visibleLaunches.size
}