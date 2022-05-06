package com.example.voyagerx.repository

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.example.rocketreserver.LaunchListQuery
import com.example.voyagerx.R
import com.example.voyagerx.repository.database.VoyagerXDatabase
import com.example.voyagerx.repository.model.Launch
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LaunchRepository @Inject constructor(
    private val database: VoyagerXDatabase,
    private val apiService: SpaceXApiService,
    @ApplicationContext private val context: Context
){

    suspend fun getLaunches() : List<Launch?>? {

        val cachedLaunches = getLaunchesFromCache()
        if(cachedLaunches != null){
            Log.d("Repository", "get launch from cache")
            return cachedLaunches
        }

        return try {
            Log.d("Repository", "attempting to fetch launch data")
            val apiResult = apiService.getLaunches()
            if (apiResult.isNullOrEmpty()) {
                //if we receive empty data
                return getLaunchesFromCache()
            } else {
                convertLaunches(apiResult) //if we receive launch data
            }
        } catch (exception: Exception) {
            return getLaunchesFromCache()
        }
    }

    private fun getLaunchesFromCache(): List<Launch>?{
        val sharedPref = getSharedPreferences()
        return convertJsonToLaunches(sharedPref.getString("cached_launches", null))
    }

//  convert API result into custom Launch data model
    private fun convertLaunches(launches: List<LaunchListQuery.Launch?>): List<Launch> {
        val customLaunches = launches.map{ launch ->
            Launch(
                launch?.id?: uniqueId(),
                launch?.mission_name,
                launch?.launch_site?.site_name_long,
                launch?.launch_date_utc.toString(),
                launch?.launch_year,
                launch?.details,
                launch?.links?.article_link,
                launch?.links?.video_link,
                launch?.links?.flickr_images
            )
        }

        GlobalScope.launch{
            insertLaunches(customLaunches)
        }
        addLaunchesToCache(customLaunches)
        return customLaunches
    }

    private fun uniqueId():String = UUID.randomUUID().toString()

    //insert launches into launch table
    private suspend fun insertLaunches(launches: List<Launch>){
        withContext(IO) {
            database.launchDao().insertAll(launches)
        }
    }

    private fun addLaunchesToCache(list: List<Launch>?){
        val sharedPref = getSharedPreferences()
        with (sharedPref.edit()) {
            putString("cached_launches", convertLaunchesToJson(list))
            apply()
        }
    }

    private fun convertLaunchesToJson(list: List<Launch>?): String?{
        if(list == null){
            return null
        }

        val stringListOfLaunch = mutableListOf<String>()
        val gson = Gson()
        for(launch in list) {
            stringListOfLaunch.add(gson.toJson(launch))
        }

        return gson.toJson(stringListOfLaunch)
    }

    private fun convertJsonToLaunches(value: String?): List<Launch>?{
        if(value == null){
            return null
        }

        val gson = Gson()
        val listType = object: TypeToken<List<String>>(){}.type
        val stringListOfLaunch: List<String> = gson.fromJson(value, listType)

        val listOfLaunch = mutableListOf<Launch>()
        for(launchString in stringListOfLaunch){
            listOfLaunch.add(gson.fromJson(launchString, object: TypeToken<Launch>(){}.type))
        }

        return listOfLaunch
    }


    private fun getSharedPreferences(): SharedPreferences {
        return context.getSharedPreferences(context.getString(R.string.user_preference), Context.MODE_PRIVATE)
    }

}
