package com.example.voyagerx.ui.fragments.landing.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.voyagerx.R
import com.example.voyagerx.ui.fragments.landing.LaunchOverviewData

class LaunchOverviewAdapter(private val listener: LaunchOverviewClickListener) :
    RecyclerView.Adapter<LaunchOverviewViewHolder>() {
    private var launches: List<LaunchOverviewData> = listOf()

    fun initializeList(launches: List<LaunchOverviewData>) {
        this.launches = launches
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LaunchOverviewViewHolder =
        LaunchOverviewViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.landing_page_overview_card, parent, false)
        )

    override fun onBindViewHolder(holder: LaunchOverviewViewHolder, position: Int) {
        val launch = launches[position]
        holder.bind(launch)
        holder.itemView.setOnClickListener {
            listener.onClick(launch)
        }
    }

    override fun getItemCount(): Int = launches.size
}