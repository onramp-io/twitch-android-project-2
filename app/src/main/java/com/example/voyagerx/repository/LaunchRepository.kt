package com.example.voyagerx.repository

import android.util.Log
import apolloClient
import com.example.rocketreserver.LaunchListQuery
import com.example.voyagerx.repository.model.Launch


object LaunchRepository {

    suspend fun getLaunches() : List<Launch?>? {
        Log.d("Repository", "attempting to fetch launch data")
        return try {
            val apiResult = apolloClient.query(LaunchListQuery()).execute().data?.launches
            if (apiResult.isNullOrEmpty()) {
                null //if we receive empty data
            } else {
                convertLaunches(apiResult!!) //if we receive launch data
            }

        } catch (exception: Exception) {
            null //if we receive an error
        }
    }

//  convert API result into custom Launch data model
    private fun convertLaunches(launches: List<LaunchListQuery.Launch?>): List<Launch?> {
        val customLaunches = launches.map{ launch ->
            launch?.let {
            Launch(
                launch.id!!,
                launch.mission_name,
                launch.launch_site?.site_name_long,
                launch.launch_date_utc.toString(),
                launch.launch_year,
                launch.details,
                launch.links?.article_link,
                launch.links?.video_link,
                launch.links?.flickr_images
            )}
        }
        return customLaunches
    }

//    insert launches into launch table
//    private fun insertLaunches{
//
//    }

}
