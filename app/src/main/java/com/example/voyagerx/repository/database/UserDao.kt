package com.example.voyagerx.repository.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.voyagerx.repository.model.Launch
import com.example.voyagerx.repository.model.User

@Dao
interface UserDao {

    @Insert
    fun insert(user: User)

    @Update
    fun update(user: User)

    @Query("INSERT INTO User (email, password) VALUES(:email, :password)")
    fun insertNoProfile(email: String, password: String)

    @Query("SELECT * FROM User WHERE email = :email")
    fun findUserByEmail(email: String): User

    @Query("UPDATE User SET favoriteLaunches = :favoriteLaunches WHERE id = :userId")
    fun updateUserFavoriteLaunches(userId: Int, favoriteLaunches: List<Launch>?)

}