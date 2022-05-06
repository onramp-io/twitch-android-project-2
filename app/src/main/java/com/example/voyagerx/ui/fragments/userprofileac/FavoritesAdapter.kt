package com.example.voyagerx.ui.fragments.userprofileac

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.voyagerx.databinding.FavoritesOverviewCardBinding
import com.example.voyagerx.helpers.LaunchClickListener
import com.example.voyagerx.repository.model.User
import com.example.voyagerx.ui.fragments.userprofileac.FavoritesAdapter.*


class FavoritesAdapter(
    private val user: User?,
    private val listener: LaunchClickListener = LaunchClickListener { }
) :
    RecyclerView.Adapter<ViewHolder>() {


    class ViewHolder(val binding: FavoritesOverviewCardBinding) : RecyclerView.ViewHolder(binding.root)

    override fun getItemCount(): Int {
        return user?.favoriteLaunches?.size ?: 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = FavoritesOverviewCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val favoriteLaunch = user!!.favoriteLaunches!![position]

        if (itemCount == 0) {
            //do nothing in the adapter
        } else {
            holder.binding.launchOverviewCardMissionName.text = favoriteLaunch.mission_name.toString()
            holder.binding.launchOverviewCardSiteName.text = favoriteLaunch.launch_site_long.toString()
            holder.binding.launchOverviewCardYear.text = favoriteLaunch.launch_year.toString()

            holder.binding.favoriteItemContainer.setOnClickListener {
                listener.onClick(favoriteLaunch)
            }
        }

    }


}