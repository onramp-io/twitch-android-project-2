package com.example.voyagerx

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.lifecycleScope
import com.example.voyagerx.R
import com.example.voyagerx.databinding.FragmentLaunchDetailsBinding
import com.example.voyagerx.databinding.FragmentRegisterBinding
import com.example.voyagerx.repository.UserRepository
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Register Fragment for Login Activity
 */
@AndroidEntryPoint
class RegisterFragment : Fragment() {

    @Inject
    lateinit var userRepository: UserRepository

    lateinit var binding: FragmentRegisterBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegisterBinding.inflate(inflater, container, false)

        binding.registerButton.setOnClickListener{
            registerAccount()
            val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view?.windowToken, 0)
        }
        return binding.root
    }

    private fun registerAccount(){
        val email = binding.email.text.toString()
        val password = binding.password.text.toString()
        val confirmPassword = binding.confirmPassword.text.toString()

        lifecycleScope.launch {
            try {
                validate(email, password, confirmPassword)
                userRepository.register(email, password)
            } catch (e: Exception) {
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

    private fun validate(email: String, password: String, confirmPassword: String){
        if(email.isBlank() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            throw Exception(requireContext().getString(R.string.invalid_email_error))
        }

        if(password.isBlank() || password.length < 6){
            throw Exception(requireContext().getString(R.string.invalid_password_error))
        }

        if(password != confirmPassword){
            throw Exception(requireContext().getString(R.string.invalid_confirm_password_error))
        }
    }

}