package com.example.mvvmauth.ui.auth

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.mvvmauth.R
import com.example.mvvmauth.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth


class LoginActivity : AppCompatActivity() {

    // ViewBinding for type-safe view access
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Initialize ViewBinding
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Check if user is already logged in
        // If yes, navigate directly to MainActivity
        checkUserSession()
    }


    private fun checkUserSession() {
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser != null) {
            // User is logged in, navigate to MainActivity
            navigateToMainActivity()
        }
    }


    fun navigateToMainActivity() {
        val intent = Intent(this, com.example.mvvmauth.ui.list.MainActivity::class.java)
        // Clear back stack - user shouldn't go back to login after successful auth
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}

