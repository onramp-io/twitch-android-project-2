package com.example.voyagerx.repository

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.example.voyagerx.R
import com.example.voyagerx.repository.database.VoyagerXDatabase
import com.example.voyagerx.repository.model.Launch
import com.example.voyagerx.repository.model.User
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception
import java.nio.file.attribute.UserPrincipal
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.log

@Singleton
class UserRepository @Inject constructor(
    @ApplicationContext private val context: Context,
    private val database: VoyagerXDatabase
) {

    fun insertUser(user: User){
        database.userDao().insert(user)
    }

    fun getUserByEmail(email: String): User?{
        return database.userDao().findUserByEmail(email)
    }

    suspend fun insertUserFavoriteLaunch(launch: Launch, user: User){
        withContext(Dispatchers.IO) {
            if (user.favoriteLaunches == null) {
                database.userDao().updateUserFavoriteLaunches(user.id, mutableListOf(launch))
            } else {
                val newFavLaunches = user.favoriteLaunches
                newFavLaunches?.add(launch)
                database.userDao().updateUserFavoriteLaunches(user.id, newFavLaunches)
            }
        }
    }

    suspend fun removeUserFavoriteLaunch(launch: Launch, user: User){
        withContext(Dispatchers.IO) {
            val newFavLaunches = user.favoriteLaunches
            newFavLaunches?.remove(launch)
            database.userDao().updateUserFavoriteLaunches(user.id, newFavLaunches)
        }
    }

    suspend fun updateUser(user: User){
        withContext(Dispatchers.IO) {
            database.userDao().update(user)
        }
    }

    suspend fun logIn(email: String, password: String){
        withContext(Dispatchers.IO) {
            val user = getUserByEmail(email)
                ?: throw Exception(context.getString(R.string.email_not_found_error))

            if (user.password != password) {
                throw Exception(context.getString(R.string.incorrect_password_error))
            }

            val sharedPref = getSharedPreferences()
            with(sharedPref.edit()) {
                putString(context.getString(R.string.curr_user), convertUserToJson(user))
            }
        }
    }

    fun logOut(){
        val sharedPref = getSharedPreferences()
        with (sharedPref.edit()) {
            putString(context.getString(R.string.curr_user), null)
        }
    }

    suspend fun register(email: String, password: String){
        withContext(Dispatchers.IO) {
            if (getUserByEmail(email) != null) {
                throw Exception(context.getString(R.string.email_already_registered_error))
            }

            database.userDao().insertNoProfile(email, password)
            logIn(email, password)
        }
    }

    fun getCurrentUser(): User?{
        val sharedPref = getSharedPreferences()
        val userJson = sharedPref.getString(context.getString(R.string.curr_user), null)

        return convertJsonToUser(userJson)
    }

    private fun convertUserToJson(user: User): String{
        val gson = Gson()
        return gson.toJson(user)
    }

    private fun convertJsonToUser(userJson: String?): User?{
        if(userJson == null){
            return null
        }

        val gson = Gson()
        return gson.fromJson(userJson, object: TypeToken<User>(){}.type)
    }

    private fun getSharedPreferences(): SharedPreferences{
        return context.getSharedPreferences(context.getString(R.string.user_preference), Context.MODE_PRIVATE)
    }
}