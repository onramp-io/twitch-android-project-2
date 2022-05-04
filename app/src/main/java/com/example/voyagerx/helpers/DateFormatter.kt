package com.example.voyagerx.helpers

//singleton - we only need one instance of this class
object DateFormatter {
    fun formatLaunchDate(launchDateUtc: String?): String? {
        val pattern = "T.*".toRegex()
        return launchDateUtc?.replace(pattern, "")
    }
}
