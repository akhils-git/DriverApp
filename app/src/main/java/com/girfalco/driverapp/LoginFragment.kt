package com.girfalco.driverapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.girfalco.driverapp.databinding.FragmentLoginBinding
import com.girfalco.driverapp.viewmodel.LoginViewModel
import com.girfalco.driverapp.viewmodel.LoginViewModelFactory
import com.girfalco.driverapp.network.model.LoginResponse
import android.util.Base64
import android.content.Intent
import com.girfalco.driverapp.HomeActivity
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import androidx.lifecycle.lifecycleScope

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val viewModel: LoginViewModel by viewModels { LoginViewModelFactory() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        setupLoginForm()
    }
    
    private fun setupLoginForm() {
        binding.loginButton.setOnClickListener {
            performLogin()
        }
    }
    
    private fun performLogin() {
        val username = binding.usernameEditText.text.toString().trim()
        val password = binding.passwordEditText.text.toString().trim()
        
        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(context, "Please enter username and password", Toast.LENGTH_SHORT).show()
            return
        }
        
        // Call real login API via ViewModel
        val email = username
        // Convert password to Base64
        val encodedPassword = Base64.encodeToString(password.toByteArray(Charsets.UTF_8), Base64.NO_WRAP)
        val mobile = "1" // hardcoded as requested
        val fcmToken = "ZHVtbXl0b2tlbg==" // hardcoded Base64 for 'dummytoken'

        viewModel.login(email, encodedPassword, mobile, fcmToken)

        // Observe state
        // Use viewLifecycleOwner.lifecycle to be lifecycle aware
        viewModel.state
            .also { flow ->
                // collect in a lifecycle-aware way
            }

        // Simple observation using a coroutine scope on viewLifecycleOwner
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.state.collect { state ->
                when (state) {
                    is com.girfalco.driverapp.viewmodel.LoginUiState.Loading -> {
                        // optionally show loading UI
                    }
                    is com.girfalco.driverapp.viewmodel.LoginUiState.Error -> {
                        Toast.makeText(context, state.message, Toast.LENGTH_LONG).show()
                    }
                    is com.girfalco.driverapp.viewmodel.LoginUiState.Success -> {
                        Toast.makeText(context, "Login Successful!", Toast.LENGTH_SHORT).show()
                        navigateToHome(state.response)
                    }
                    else -> {
                        // idle
                    }
                }
            }
        }
    }

    private fun navigateToHome(response: LoginResponse) {
        // Serialize response to JSON and pass to HomeActivity
        val json = Json.encodeToString(response)
        val intent = Intent(requireContext(), HomeActivity::class.java)
        intent.putExtra("LOGIN_RESPONSE_JSON", json)
        startActivity(intent)
        // finish current activity so back doesn't return to login
        activity?.finish()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}