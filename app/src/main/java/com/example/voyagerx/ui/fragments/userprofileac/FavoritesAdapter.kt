package com.example.voyagerx.ui.fragments.userprofileac

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.voyagerx.R
import com.example.voyagerx.databinding.FavoritesOverviewCardBinding
import com.example.voyagerx.databinding.LandingPageOverviewCardBinding
import com.example.voyagerx.helpers.LaunchClickListener
import com.example.voyagerx.repository.model.User
import com.example.voyagerx.ui.fragments.landing.list.LaunchOverviewAdapter
import com.example.voyagerx.ui.fragments.landing.list.LaunchOverviewViewHolder
import com.example.voyagerx.ui.fragments.userprofileac.FavoritesAdapter.*
import com.example.voyagerx.util.SharedPreferencesManager
import java.util.*
import kotlin.concurrent.schedule


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
        val context: Context = holder.itemView.context
        holder.setTextSize(SharedPreferencesManager(context).getFontSize())
        val favoriteLaunch = user!!.favoriteLaunches!![position]

        if (itemCount == 0) {
            //do nothing in the adapter
        } else {
            //holder.binding.launchOverviewCardMissionName.text = favoriteLaunch.mission_name.toString()
            //holder.binding.launchOverviewCardSiteName.text = favoriteLaunch.launch_site_long.toString()
            //holder.binding.launchOverviewCardYear.text = favoriteLaunch.launch_year.toString()
            holder.bind(favoriteLaunch)
            holder.setTextSize(SharedPreferencesManager(context).getFontSize())
            holder.itemView.findViewById<CardView>(R.id.launch_overview_card_view).setOnClickListener {
                Timer().schedule(LaunchOverviewAdapter.NAV_DELAY) {
                    listener.onClick(favoriteLaunch)
                }
            }
        }

    }

}