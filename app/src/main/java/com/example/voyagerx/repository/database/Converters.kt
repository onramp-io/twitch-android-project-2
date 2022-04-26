package com.example.voyagerx.repository.database

import androidx.room.TypeConverter
import com.example.voyagerx.repository.model.Launch
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {

    @TypeConverter
    fun fromStringtoListOfLaunch(value: String?): List<Launch>?{
        if(value == null){
            return null
        }
        val gson = Gson()
        val listType = object: TypeToken<List<Launch>>(){}.type
        return gson.fromJson(value, listType)
    }

    @TypeConverter
    fun fromListOfLaunchToString(list: List<Launch>?): String?{
        if(list == null){
            return null
        }
        val gson = Gson()
        return gson.toJson(list)
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