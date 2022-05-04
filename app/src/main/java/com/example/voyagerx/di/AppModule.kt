package com.example.voyagerx.di

import android.content.Context
import androidx.room.Room
import com.example.voyagerx.repository.SpaceXApiService
import com.example.voyagerx.repository.database.VoyagerXDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext applicationContext: Context) =
        Room.databaseBuilder(
            applicationContext,
            VoyagerXDatabase::class.java, "voyagerx_db"
        ).build()

    @Singleton
    @Provides
    fun provideApiService(): SpaceXApiService = SpaceXApiService()
}