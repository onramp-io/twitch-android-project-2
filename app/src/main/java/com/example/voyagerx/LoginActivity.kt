package com.example.voyagerx

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.voyagerx.databinding.ActivityLoginBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backButton.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

        // NOTE: when directing to login activity, add extra to intent to direct to correct page ("view" = "register" or "login")
        val intendedView = intent.getStringExtra(getString(R.string.intended_login_view))
        changeView(intendedView?:getString(R.string.title_login))

    }

    private fun changeView(intendedView: String){
        val switchLoginRegisterButton = binding.switchLoginRegisterButton
        val title = binding.loginFlowTitle

        when(intendedView){
            getString(R.string.title_login) -> {
                changeFragmentView(LoginFragment())
                title.text = getString(R.string.title_login)
                switchLoginRegisterButton.text = getString(R.string.title_register)
                switchLoginRegisterButton.setOnClickListener{
                    changeView(getString(R.string.title_register))
                }
            }
            getString(R.string.title_register) -> {
                changeFragmentView(RegisterFragment())
                title.text = getString(R.string.title_register)
                switchLoginRegisterButton.text = getString(R.string.title_login)
                switchLoginRegisterButton.setOnClickListener{
                    changeView(getString(R.string.title_login))
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