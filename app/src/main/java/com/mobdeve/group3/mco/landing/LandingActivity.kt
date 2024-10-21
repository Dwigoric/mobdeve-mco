package com.mobdeve.group3.mco.landing

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.mobdeve.group3.mco.R
import com.mobdeve.group3.mco.databinding.ActivityLandingBinding

class LandingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

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
}

