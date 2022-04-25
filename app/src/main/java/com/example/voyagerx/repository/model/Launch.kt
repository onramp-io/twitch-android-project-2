package com.example.voyagerx.repository.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Launch(
    @PrimaryKey
    val id: String,

    val mission_name: String,
    val launch_site_long: String,
    val launch_date_utc: String,
    val details: String,

    val article_link: String,
    val video_link: String,
    val image_links: List<String>
)
