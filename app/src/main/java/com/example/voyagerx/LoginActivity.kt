package com.example.voyagerx

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.voyagerx.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // NOTE: when directing to login activity, add extra to intent to direct to correct page ("view" = "register" or "login")
        val intendedView = intent.getStringExtra("view")
        changeView(intendedView?:"login")

    }

    private fun changeView(intendedView: String){
        val switchLoginRegisterButton = binding.switchLoginRegisterButton
        val title = binding.loginFlowTitle

        when(intendedView){
            "login" -> {
                changeFragmentView(LoginFragment())
                title.text = "Login"
                switchLoginRegisterButton.text = "Register"
                switchLoginRegisterButton.setOnClickListener{
                    changeView("register")
                }
            }
            "register" -> {
                changeFragmentView(RegisterFragment())
                title.text = "Register"
                switchLoginRegisterButton.text = "Login"
                switchLoginRegisterButton.setOnClickListener{
                    changeView("login")
                }
            }
        }
    }

    private fun changeFragmentView(desiredFragment: Fragment){
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainerView, desiredFragment)
            .commit()
    }

}