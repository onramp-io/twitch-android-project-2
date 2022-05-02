package com.example.voyagerx.repository

import com.example.voyagerx.repository.database.VoyagerXDatabase
import com.example.voyagerx.repository.model.User
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    private val database: VoyagerXDatabase
) {

    fun insertUser(user: User){
        database.userDao().insert(user)
    }

    fun getUserByEmail(email: String): User?{
        return database.userDao().findUserByEmail(email)
    }
}