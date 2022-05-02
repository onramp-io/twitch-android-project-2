package com.example.voyagerx.helpers

import com.example.voyagerx.repository.model.Launch

class LaunchClickListener(
    private val listener: (launch: Launch) -> Unit
) {
    fun onClick(launch: Launch) = listener(launch)
}