package com.example.voyagerx.repository.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User (
    @PrimaryKey(autoGenerate = true)
    val id: Int,

    val email: String,
    val password: String,

    var name: String,
    var location: String,
    var bio: String,

    var favoriteLaunches: List<Launch>
)