package com.example.voyagerx.repository.model

import androidx.room.Entity

@Entity
data class FavoriteLaunches(
    val launch_id: String,
    val user_id: Int
)
