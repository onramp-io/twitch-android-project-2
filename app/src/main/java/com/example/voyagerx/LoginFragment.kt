package com.example.voyagerx

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.lifecycle.lifecycleScope
import com.example.voyagerx.databinding.FragmentLoginBinding
import com.example.voyagerx.repository.UserRepository
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

/**
 * Login Fragment in Login Activity
 */
@AndroidEntryPoint
class LoginFragment : Fragment() {

    @Inject
    lateinit var userRepository: UserRepository

    private lateinit var binding: FragmentLoginBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentLoginBinding.inflate(inflater, container, false)

        binding.loginButton.setOnClickListener{
            loginAccount()
            val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view?.windowToken, 0)
        }

        return binding.root
    }

    private fun loginAccount(){
        val email = binding.email.text.toString()
        val password = binding.password.text.toString()

        lifecycleScope.launch{
            try {
                userRepository.logIn(email, password)
                startActivity(Intent(activity, MainActivity::class.java))
            }catch (e: Exception){
                activity?.let {
                    Snackbar.make(
                        it.findViewById(R.id.loginActivity),
                        e.message ?: "Error",
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}