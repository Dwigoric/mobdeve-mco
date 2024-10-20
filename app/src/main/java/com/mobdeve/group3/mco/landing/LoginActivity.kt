package com.mobdeve.group3.mco.landing

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.mobdeve.group3.mco.MainActivity
import com.mobdeve.group3.mco.R

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val loginButton: Button = findViewById(R.id.btnLogin2)
        val signUpText: TextView = findViewById(R.id.txtSignUp)

        loginButton.setOnClickListener {
            // Navigate to MainActivity after login
            val intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
        }

        signUpText.setOnClickListener {
            // Navigate to SignupActivity
            val signUpIntent = Intent(this, SignupActivity::class.java)
            startActivity(signUpIntent)
        }
    }
}

