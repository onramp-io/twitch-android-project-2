package com.example.voyagerx.repository.database

import androidx.room.Dao
import androidx.room.Insert
import com.example.voyagerx.repository.model.User

@Dao
interface UserDao {

    @Insert
    fun insert(user: User)
}