package com.mobdeve.group3.mco.profile

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.mobdeve.group3.mco.MainActivity
import com.mobdeve.group3.mco.R
import com.mobdeve.group3.mco.catalogue.CatalogueActivity
import com.mobdeve.group3.mco.databinding.ActivityProfileBinding
import com.mobdeve.group3.mco.db.SightingsAPI
import com.mobdeve.group3.mco.landing.LandingActivity
import com.mobdeve.group3.mco.sighting.Sighting


class ProfileActivity : AppCompatActivity() {
    private val sightingsList = ArrayList<Sighting>()
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val viewBinding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        this.recyclerView = findViewById(R.id.rcvProfileSightings)
        this.recyclerView.adapter = ProfileSightingsAdapter(this.sightingsList)
        this.recyclerView.layoutManager = LinearLayoutManager(this)

        val bottomNav: BottomNavigationView = findViewById(R.id.bottom_nav)
        bottomNav.selectedItemId = R.id.nav_profile

        // Set a long press listener on the Profile tab for the popup menu
        val profileMenuItem = bottomNav.menu.findItem(R.id.nav_profile)
        findViewById<View>(profileMenuItem.itemId).setOnLongClickListener {
            showLogoutPopup(it) // Show the popup menu
            true
        }

        populateSightings()

        bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.nav_catalogue -> {
                    val catalogueIntent = Intent(applicationContext, CatalogueActivity::class.java)
                    catalogueIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(catalogueIntent)
                    finish()
                    true
                }

                R.id.nav_profile -> true
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

    private fun populateSightings() {
        SightingsAPI.getInstance().getUserSightings { sightings ->
            sightings.forEach { sightingData ->
                val sightingId = sightingData["id"] as String
                val userHandler = sightingData["userHandler"] as String
                val userIcon = sightingData["userIcon"] as Uri
                val postingDate = sightingData["postingDate"] as String
                val animalName = sightingData["animalName"] as String
                val scientificName = sightingData["scientificName"] as String
                val location = sightingData["location"] as String
                val sightDate = sightingData["sightDate"] as String
                val imageUri = sightingData["imageId"] as Uri
                val groupSize = sightingData["groupSize"] as Int
                val distance = sightingData["distance"] as Float
                val observerType = sightingData["observerType"] as String
                val sightingTime = sightingData["sightingTime"] as String
                val score = sightingData["score"] as Int
                val hasUpvoted = sightingData["hasUpvoted"] as Boolean
                val hasDownvoted = sightingData["hasDownvoted"] as Boolean

                val sighting = Sighting(
                    id = sightingId,
                    userHandler = userHandler,
                    userIcon = userIcon,
                    postingDate = postingDate,
                    animalName = animalName,
                    scientificName = scientificName,
                    location = location,
                    sightDate = sightDate,
                    imageUri = imageUri,
                    groupSize = groupSize,
                    distance = distance,
                    observerType = observerType,
                    sightingTime = sightingTime,
                    score = score,
                    hasUpvoted = hasUpvoted,
                    hasDownvoted = hasDownvoted
                )

                this.sightingsList.add(sighting)
            }

            this.recyclerView.adapter?.notifyDataSetChanged()
        }
    }

    private fun showLogoutPopup(view: View) {
        val popup = PopupMenu(this, view)
        popup.menuInflater.inflate(R.menu.logout_popup_menu, popup.menu)

        popup.setOnMenuItemClickListener { menuItem: MenuItem ->
            when (menuItem.itemId) {
                R.id.menu_logout -> {
                    // Handle logout and navigate back to LaunchActivity
                    val logoutIntent = Intent(applicationContext, LandingActivity::class.java)
                    logoutIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(logoutIntent)
                    finish()
                    true
                }

                else -> false
            }
        }
        popup.show()
    }
}
