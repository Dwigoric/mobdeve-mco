package com.mobdeve.group3.mco.landing

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.mobdeve.group3.mco.MainActivity
import com.mobdeve.group3.mco.R
import com.mobdeve.group3.mco.databinding.ActivityLandingBinding

class LandingActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        auth = Firebase.auth

        val viewBinding = ActivityLandingBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        val btnLogIn = findViewById<Button>(R.id.btnLogIn)
        val btnRegister = findViewById<Button>(R.id.btnRegister)

        btnLogIn.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        btnRegister.setOnClickListener {
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onStart() {
        super.onStart()
        if (auth.currentUser != null) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}

