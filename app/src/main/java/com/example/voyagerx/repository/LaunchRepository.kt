package com.example.voyagerx.repository

import apolloClient
import com.example.rocketreserver.LaunchListQuery

class LaunchRepository {

    suspend fun getLaunches() : List<LaunchListQuery.Launch?>? {
        try {
            return apolloClient.query(
                LaunchListQuery()).execute().data?.launches
        } catch (exception: Exception) {
            return null
        }
    }

//    convert API result into custom Launch data model
//    private fun convertLaunches(launches: List<LaunchListQuery.Launch>): List<Launch>? {
//        val customLaunches = launches.map{ launch ->
//            Launch(
//                launch.id.toInt(),
//                launch.mission_name,
//                launch.launch_site?.site_name_long,
//                launch.launch_date_utc,
//                launch.details,
//                launch.links.article_link,
//                launch.links?.video_link,
//                launch.links?.flickr_images
//            )
//        }
//        return customLaunches
//    }

////    insert launches into launch table
//    private fun insertLaunches{
//
//    }

}
