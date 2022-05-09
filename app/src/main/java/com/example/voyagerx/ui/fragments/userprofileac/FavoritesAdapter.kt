package com.example.voyagerx.ui.fragments.userprofileac

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.voyagerx.databinding.FavoritesOverviewCardBinding
import com.example.voyagerx.databinding.LandingPageOverviewCardBinding
import com.example.voyagerx.helpers.LaunchClickListener
import com.example.voyagerx.repository.model.User
import com.example.voyagerx.ui.fragments.landing.list.LaunchOverviewViewHolder
import com.example.voyagerx.ui.fragments.userprofileac.FavoritesAdapter.*


class FavoritesAdapter(
    private val user: User?,
    private val listener: LaunchClickListener = LaunchClickListener { }
) :
    RecyclerView.Adapter<LaunchOverviewViewHolder>() {

    override fun getItemCount(): Int {
        return user?.favoriteLaunches?.size ?: 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LaunchOverviewViewHolder =
        LaunchOverviewViewHolder(
            LandingPageOverviewCardBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )


    override fun onBindViewHolder(holder: LaunchOverviewViewHolder, position: Int) {
        val favoriteLaunch = user!!.favoriteLaunches!![position]

        if (itemCount == 0) {
            //do nothing in the adapter
        } else {
            //holder.binding.launchOverviewCardMissionName.text = favoriteLaunch.mission_name.toString()
            //holder.binding.launchOverviewCardSiteName.text = favoriteLaunch.launch_site_long.toString()
            //holder.binding.launchOverviewCardYear.text = favoriteLaunch.launch_year.toString()
            holder.bind(favoriteLaunch)
            holder.itemView.setOnClickListener {
                listener.onClick(favoriteLaunch)
            }
        }

    }


}