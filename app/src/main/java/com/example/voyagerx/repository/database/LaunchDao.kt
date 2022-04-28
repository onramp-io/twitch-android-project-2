package com.example.voyagerx.repository.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.voyagerx.repository.model.Launch

@Dao
interface LaunchDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(launch: Launch)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(launches: List<Launch>)

    @Query("SELECT * FROM Launch")
    fun getAll(): List<Launch>

    @Query("SELECT * FROM Launch WHERE id = :id")
    fun findLaunchById(id: String): Launch
}