package com.girfalco.driverapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.girfalco.driverapp.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

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
        
        // Pre-fill for demo purposes
        binding.usernameEditText.setText("admin")
        binding.passwordEditText.setText("admin")
    }
    
    private fun performLogin() {
        val username = binding.usernameEditText.text.toString().trim()
        val password = binding.passwordEditText.text.toString().trim()
        
        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(context, "Please enter username and password", Toast.LENGTH_SHORT).show()
            return
        }
        
        // Validate credentials
        if (username == "admin" && password == "admin") {
            Toast.makeText(context, "Login Successful!", Toast.LENGTH_SHORT).show()
            
            // Navigate to dashboard
            findNavController().navigate(R.id.action_loginFragment_to_dashboardFragment)
        } else {
            Toast.makeText(context, "Invalid credentials. Use admin/admin", Toast.LENGTH_LONG).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}