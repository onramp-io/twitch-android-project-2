package com.example.voyagerx.repository

import android.content.Context
import android.util.Log
import apolloClient
import com.example.rocketreserver.LaunchListQuery
import com.example.voyagerx.repository.database.VoyagerXDatabase
import com.example.voyagerx.repository.model.Launch
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LaunchRepository @Inject constructor(
    private val database: VoyagerXDatabase
){

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
                launch.id?: uniqueId(),
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

    private fun uniqueId():String = UUID.randomUUID().toString()

    //insert launches into launch table
    private fun insertLaunches(launches: List<Launch>){
        database.launchDao().insertAll(launches)
    }

}
