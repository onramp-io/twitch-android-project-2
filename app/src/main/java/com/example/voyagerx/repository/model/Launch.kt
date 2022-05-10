package com.example.voyagerx.repository.model

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Launch(
    @PrimaryKey
    val id: String,

    val mission_name: String?,
    val launch_site_long: String?,
    val launch_date_utc: String?,
    val launch_year: String?,
    val details: String?,

    val article_link: String?,
    val video_link: String?,
    val image_links: List<String?>?
) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readString().toString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.createStringArrayList()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(mission_name)
        parcel.writeString(launch_site_long)
        parcel.writeString(launch_date_utc)
        parcel.writeString(launch_year)
        parcel.writeString(details)
        parcel.writeString(article_link)
        parcel.writeString(video_link)
        parcel.writeStringList(image_links)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Launch> {
        const val BUNDLE_KEY = "launch_details"

        override fun createFromParcel(parcel: Parcel): Launch {
            return Launch(parcel)
        }

        override fun newArray(size: Int): Array<Launch?> {
            return arrayOfNulls(size)
        }
    }
}
