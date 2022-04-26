package com.example.voyagerx.repository.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Launch(
    @PrimaryKey
    val id: String,

    @ColumnInfo(defaultValue = "")
    val mission_name: String,
    @ColumnInfo(defaultValue = "")
    val launch_site_long: String,
    @ColumnInfo(defaultValue = "")
    val launch_date_utc: String,
    @ColumnInfo(defaultValue = "")
    val launch_year: String,
    @ColumnInfo(defaultValue = "")
    val details: String,

    @ColumnInfo(defaultValue = "")
    val article_link: String,
    @ColumnInfo(defaultValue = "")
    val video_link: String,
    val image_links: List<String>?
)
