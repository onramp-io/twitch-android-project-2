package com.example.voyagerx.presenters

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.core.content.ContextCompat.startActivity
import com.example.voyagerx.LaunchDetailsFragment
import com.example.voyagerx.R
import com.example.voyagerx.repository.UserRepository
import com.example.voyagerx.repository.model.Launch
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class LaunchDetailsPresenter(private var userRepository: UserRepository, var view: LaunchDetailsFragment, private val context: Context) :
    CoroutineScope
{
    private val job = Job() // keeps track of the state of a coroutine/cancel it
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job //dispatcher decides which thread the coroutine will run on
    lateinit var launchObj: Launch
    var currUser = userRepository.getCurrentUser()

    fun favoriteLaunch() {
        launch{
            try {
                var currUser = userRepository.getCurrentUser()
                if (currUser != null) {
                    userRepository.insertUserFavoriteLaunch(launchObj, currUser!!)
                    view.displayFavoriteSnackBar(context.getString(R.string.favorite_launch_msg, launchObj.mission_name))
                    view.displayFavorite(userRepository.getCurrentUser()!!, launchObj)
                } else {
                    view.displayLoginPopup()
                }
            } catch (e: Exception) {
                Log.d("user", "$e")
                view.displayErrorSnackBar()
            }
        }
    }

    fun unfavoriteLaunch() {
        launch {
            try {
                var currUser = userRepository.getCurrentUser()
                if (currUser != null) {
                    userRepository.removeUserFavoriteLaunch(launchObj, currUser!!)
                    view.displayFavoriteSnackBar(context.getString(R.string.unfavorite_launch_msg, launchObj.mission_name))
                    view.displayFavorite(userRepository.getCurrentUser()!!, launchObj)
                } else {
                    view.displayLoginPopup()
                }
            } catch (e: java.lang.Exception) {
                view.displayErrorSnackBar()
            }
        }
    }

    fun shareLaunch() {
        val share = Intent.createChooser(Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(
                Intent.EXTRA_TEXT, "Check out this SpaceX Launch!\n\n" +
                        if (!launchObj.mission_name.isNullOrEmpty()) "${launchObj.mission_name}\n\n" else "" +
                                if (!launchObj.details.isNullOrEmpty()) "${launchObj.details}\n\n" else "" +
                                        if (!launchObj.video_link.isNullOrEmpty()) "Watch the launch video:\n ${launchObj.video_link}" else ""
            )
            type = "text/plain"
        }, null)
        startActivity(context, share, null)
    }

    fun handleLinkClick(link : String?) {
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
        startActivity(context, browserIntent, null)
    }

    fun setDetailsLaunchObj(incomingLaunch : Launch) {
        launchObj = incomingLaunch
    }
}
