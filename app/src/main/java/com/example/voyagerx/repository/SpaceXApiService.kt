package com.example.voyagerx.repository

import apolloClient
import com.example.rocketreserver.LaunchListQuery

class SpaceXApiService {

    suspend fun getLaunches(): List<LaunchListQuery.Launch?>?{
        return apolloClient.query(LaunchListQuery()).execute().data?.launches
    }
}