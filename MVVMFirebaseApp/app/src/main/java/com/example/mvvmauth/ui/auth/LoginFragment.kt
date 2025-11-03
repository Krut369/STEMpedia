package com.example.mvvmauth.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.mvvmauth.databinding.FragmentLoginBinding
import com.example.mvvmauth.viewmodel.AuthViewModel


class LoginFragment : Fragment() {

    // ViewBinding for type-safe view access
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    // ViewModel instance - using viewModels() delegate for lifecycle-aware creation
    // The 'by viewModels()' ensures ViewModel survives configuration changes
    private val authViewModel: AuthViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupClickListeners()
        observeAuthState()
    }


    private fun setupClickListeners() {
        binding.apply {
            loginButton.setOnClickListener {
                val email = emailEditText.text.toString().trim()
                val password = passwordEditText.text.toString().trim()
                
                authViewModel.login(email, password)
            }

            registerButton.setOnClickListener {
                val email = emailEditText.text.toString().trim()
                val password = passwordEditText.text.toString().trim()
                
                authViewModel.register(email, password)
            }
        }
    }


    private fun observeAuthState() {
        authViewModel.authState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is AuthViewModel.AuthState.Idle -> {
                    hideLoading()
                }

                is AuthViewModel.AuthState.Loading -> {
                    showLoading()
                }

                is AuthViewModel.AuthState.Success -> {
                    hideLoading()
                    Toast.makeText(requireContext(), "Authentication successful!", Toast.LENGTH_SHORT).show()
                    
                    (activity as? LoginActivity)?.navigateToMainActivity()
                }

                is AuthViewModel.AuthState.Error -> {
                    hideLoading()
                    Toast.makeText(requireContext(), state.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }


    private fun showLoading() {
        binding.apply {
            progressBar.visibility = View.VISIBLE
            loginButton.isEnabled = false
            registerButton.isEnabled = false
            emailEditText.isEnabled = false
            passwordEditText.isEnabled = false
        }
    }


    private fun hideLoading() {
        binding.apply {
            progressBar.visibility = View.GONE
            loginButton.isEnabled = true
            registerButton.isEnabled = true
            emailEditText.isEnabled = true
            passwordEditText.isEnabled = true
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

