package com.mobdeve.group3.mco.landing

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.mobdeve.group3.mco.R

class SignupActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        // Handle register button click
        val registerButton: Button = findViewById(R.id.btnRegister2)
        registerButton.setOnClickListener {
            // Navigate to LoginActivity after registration
            val intent = Intent(this, LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish() // Optionally close SignupActivity
        }
    }
}
