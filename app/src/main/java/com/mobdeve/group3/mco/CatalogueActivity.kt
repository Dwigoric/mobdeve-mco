package com.mobdeve.group3.mco

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.mobdeve.group3.mco.databinding.ActivityCatalogueBinding

class CatalogueActivity : AppCompatActivity() {
    private val categories = CategoryGenerator.generateCategories()
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val viewBinding = ActivityCatalogueBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        this.recyclerView = findViewById(R.id.rcvCatalogueCategories)
        this.recyclerView.adapter = CatalogueCategoriesAdapter(this.categories)
        this.recyclerView.layoutManager = GridLayoutManager(this, 2)

        val bottomNav: BottomNavigationView = findViewById(R.id.bottom_nav)
        bottomNav.selectedItemId = R.id.nav_catalogue
        bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.nav_catalogue -> {
                    true
                }

                R.id.nav_profile -> {
                    val profileIntent = Intent(applicationContext, ProfileActivity::class.java)
                    profileIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(profileIntent)
                    finish()
                    true
                }

                R.id.nav_home -> {
                    val homeIntent = Intent(applicationContext, MainActivity::class.java)
                    homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(homeIntent)
                    finish()
                    true
                }

                else -> false
            }
        }
    }
}