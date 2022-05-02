package com.example.voyagerx.di

import androidx.test.espresso.matcher.ViewMatchers.assertThat
import com.example.voyagerx.repository.UserRepository
import com.example.voyagerx.repository.model.User
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.hamcrest.CoreMatchers.equalTo
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@HiltAndroidTest
class InjectionTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var userRepo: UserRepository

    @Test
    fun addUserToDbTest(){
        hiltRule.inject()
        val user = User(1, "test email", "test password", null, null,null, null)
        userRepo.insertUser(user)

        val byEmail = userRepo.getUserByEmail("test email")
        assertThat(byEmail, equalTo(user))
    }
}