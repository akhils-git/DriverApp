package com.girfalco.driverapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.girfalco.driverapp.databinding.FragmentLoginBinding
import com.girfalco.driverapp.viewmodel.LoginViewModel
import com.girfalco.driverapp.viewmodel.LoginViewModelFactory
import com.girfalco.driverapp.network.model.LoginResponse
import android.util.Base64
import android.content.Intent
import com.girfalco.driverapp.HomeActivity
import com.girfalco.driverapp.utils.ToastType
import com.girfalco.driverapp.utils.ToastUtils
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import com.girfalco.driverapp.network.AuthTokenStore
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
        observeViewModel()
    }
    
    private fun setupLoginForm() {
        binding.loginButton.setOnClickListener {
            performLogin()
        }
    }

    private fun observeViewModel() {
        // Observe state from onViewCreated - this is the correct lifecycle-aware approach
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.state.collect { state ->
                when (state) {
                    is com.girfalco.driverapp.viewmodel.LoginUiState.Loading -> {
                        // optionally show loading UI
                    }
                    is com.girfalco.driverapp.viewmodel.LoginUiState.Error -> {
                        ToastUtils.showCustomToast(requireContext(), state.message, ToastType.ERROR)
                    }
                    is com.girfalco.driverapp.viewmodel.LoginUiState.Success -> {
                        // DEFINITIVE FIX: The success toast is now only shown in HomeActivity.
                        // No toast should be shown here.
                        navigateToHome(state.response)
                    }
                    else -> {
                        // idle
                    }
                }
            }
        }
    }
    
    private fun performLogin() {
        val username = binding.usernameEditText.text.toString().trim()
        val password = binding.passwordEditText.text.toString().trim()
        
        if (username.isEmpty() || password.isEmpty()) {
            ToastUtils.showCustomToast(requireContext(), "Please enter username and password", ToastType.ERROR)
            return
        }
        
        // Call real login API via ViewModel
        val email = username
        // Convert password to Base64
        val encodedPassword = Base64.encodeToString(password.toByteArray(Charsets.UTF_8), Base64.NO_WRAP)
        val mobile = "1" // hardcoded as requested
        val fcmToken = "ZHVtbXl0b2tlbg==" // hardcoded Base64 for 'dummytoken'

        viewModel.login(email, encodedPassword, mobile, fcmToken)
    }

    private fun navigateToHome(response: LoginResponse) {
        // Serialize response to JSON and pass to HomeActivity
        val json = Json.encodeToString(response)
        // Debug: log the serialized JSON so we can confirm the server userID is present
        android.util.Log.d("LoginFragment", "LOGIN_RESPONSE_JSON: $json")
        // Store the token in the AuthTokenStore so Retrofit will attach it to future requests
        response.token?.let { AuthTokenStore.token = it }
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
