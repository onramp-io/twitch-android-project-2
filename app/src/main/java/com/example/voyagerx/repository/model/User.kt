package com.example.voyagerx.repository.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.voyagerx.repository.database.Converters

@Entity
data class User (
    @PrimaryKey(autoGenerate = true)
    val id: Int,

    val email: String,
    val password: String,

    @ColumnInfo(defaultValue = "")
    var name: String,
    @ColumnInfo(defaultValue = "")
    var location: String,
    @ColumnInfo(defaultValue = "")
    var bio: String,

    var favoriteLaunches: List<Launch>?
)