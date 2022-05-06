package com.example.voyagerx.ui.fragments.userprofileac

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.voyagerx.databinding.FavoritesOverviewCardBinding
import com.example.voyagerx.repository.model.User
import com.example.voyagerx.ui.fragments.userprofileac.FavoritesAdapter.*


class FavoritesAdapter(
    val user: User?) :
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
        if (itemCount == 0) {
            //do nothing in the adapter
        } else {
            holder.binding.launchOverviewCardMissionName.text = user!!.favoriteLaunches!![position].mission_name.toString()
            holder.binding.launchOverviewCardSiteName.text = user.favoriteLaunches!![position].launch_site_long.toString()
            holder.binding.launchOverviewCardYear.text = user.favoriteLaunches!![position].launch_year.toString()
        }
    }



}