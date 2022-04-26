package com.example.voyagerx.repository.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.voyagerx.repository.model.User

@Dao
interface UserDao {

    @Insert
    fun insert(user: User)

    @Query("INSERT INTO User (email, password) VALUES(:email, :password)")
    fun insertNoProfile(email: String, password: String)

    @Query("SELECT * FROM User WHERE email = :email")
    fun findUserByEmail(email: String): User
}