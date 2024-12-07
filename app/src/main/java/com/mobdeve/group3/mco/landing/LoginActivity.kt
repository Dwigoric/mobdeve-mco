package com.mobdeve.group3.mco.landing

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.mobdeve.group3.mco.R

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private val TAG = "LoginActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = Firebase.auth

        val loginButton: Button = findViewById(R.id.btnLogin2)
        val signUpText: TextView = findViewById(R.id.txtSignUp)

        loginButton.setOnClickListener {
            auth.signInWithEmailAndPassword(
                findViewById<TextView>(R.id.etEmail).text.toString(),
                findViewById<TextView>(R.id.etPassword).text.toString()
            ).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Login successful
                    Log.d(TAG, "signInWithEmail:success")

                    // Navigate to MainActivity after login
                    currentFocus?.clearFocus()
                    finish()
                } else {
                    // Login failed
                    Log.w(TAG, "signInWithEmail:failure", task.exception)

                    // Show toast
                    Toast.makeText(
                        baseContext, "Login failed: ${task.exception?.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        signUpText.setOnClickListener {
            // Navigate to SignupActivity
            val signUpIntent = Intent(this, SignupActivity::class.java)
            startActivity(signUpIntent)
            finish()
        }
    }
}

