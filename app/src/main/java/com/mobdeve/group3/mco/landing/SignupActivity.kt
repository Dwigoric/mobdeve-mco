package com.mobdeve.group3.mco.landing

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.mobdeve.group3.mco.MainActivity
import com.mobdeve.group3.mco.R
import com.mobdeve.group3.mco.db.UsersAPI

class SignupActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private val TAG = "SignupActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        auth = Firebase.auth

        // Handle register button click
        val registerButton: Button = findViewById(R.id.btnRegister2)
        registerButton.setOnClickListener {
            auth.createUserWithEmailAndPassword(
                findViewById<EditText>(R.id.etEmail2).text.toString(),
                findViewById<EditText>(R.id.etPassword2).text.toString()
            ).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Registration successful
                    Log.d(TAG, "createUserWithEmail:success")

                    // Create user data
                    val user = hashMapOf(
                        "email" to findViewById<EditText>(R.id.etEmail2).text.toString() as Any,
                        "username" to findViewById<EditText>(R.id.etUsername).text.toString() as Any,
                        "bio" to "" as Any,
                        "avatar" to "" as Any
                    )

                    // Add user data to Firestore
                    UsersAPI.getInstance().addUser(auth.currentUser?.uid!!, user)

                    // Navigate to MainActivity after registration
                    val intent = Intent(this, MainActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                    finish() // Optionally close SignupActivity
                } else {
                    // Registration failed
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)

                    // Show toast
                    Toast.makeText(
                        baseContext, "Registration failed: ${task.exception?.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}
