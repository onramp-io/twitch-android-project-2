package com.example.voyagerx.repository.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.voyagerx.repository.model.User
import com.example.voyagerx.repository.model.Launch

@Database(entities = [User::class, Launch::class], version = 1)
@TypeConverters(Converters::class)
abstract class VoyagerXDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao

    abstract fun launchDao(): LaunchDao

}