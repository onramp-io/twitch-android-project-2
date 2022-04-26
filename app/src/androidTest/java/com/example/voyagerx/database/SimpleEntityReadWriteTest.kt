package com.example.voyagerx.database

import android.content.Context
import android.util.Log
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.voyagerx.repository.database.UserDao
import com.example.voyagerx.repository.database.VoyagerXDatabase
import com.example.voyagerx.repository.model.User
import org.hamcrest.CoreMatchers.equalTo
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException
import java.lang.Exception
import kotlin.jvm.Throws

@RunWith(AndroidJUnit4::class)
class SimpleEntityReadWriteTest {

    private lateinit var userDao: UserDao
    private lateinit var db: VoyagerXDatabase

    @Before
    fun createDb(){
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, VoyagerXDatabase::class.java).build()
        userDao = db.userDao()
        Log.d("db", "running")
    }

    @After
    @Throws(IOException::class)
    fun closeDb(){
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun writeNoProfileUserAndReadInList(){
        val user = User(1, "test email", "test password", "", "", "", null)
        userDao.insertNoProfile("test email", "test password")
        val byEmail = userDao.findUserByEmail("test email")
        assertThat(byEmail, equalTo(user))
    }

}