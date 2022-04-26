package com.example.voyagerx.repository.database

import android.util.Log
import androidx.room.TypeConverter
import com.example.voyagerx.repository.model.Launch
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {

    @TypeConverter
    fun fromStringToListOfLaunch(value: String?): List<Launch>?{
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

        Log.d("launchlist", listOfLaunch.toString())
        return listOfLaunch.toList()
    }

    @TypeConverter
    fun fromListOfLaunchToString(list: List<Launch>?): String?{
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

    @TypeConverter
    fun fromStringToListOfString(value: String?): List<String>?{
        if(value == null){
            return null
        }
        val gson = Gson()
        val listType = object: TypeToken<List<String>>(){}.type
        return gson.fromJson(value, listType)
    }

    @TypeConverter
    fun fromListOfStringToString(list: List<String>?): String?{
        if(list == null){
            return null
        }
        val gson = Gson()
        return gson.toJson(list)
    }
}