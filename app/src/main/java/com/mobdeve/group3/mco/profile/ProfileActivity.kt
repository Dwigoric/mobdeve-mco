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
import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.mobdeve.group3.mco.MainActivity
import com.mobdeve.group3.mco.R
import com.mobdeve.group3.mco.catalogue.CatalogueActivity
import com.mobdeve.group3.mco.databinding.ActivityProfileBinding
import com.mobdeve.group3.mco.db.SightingsAPI
import com.mobdeve.group3.mco.db.UsersAPI
import com.mobdeve.group3.mco.landing.LandingActivity
import com.mobdeve.group3.mco.sighting.Sighting


class ProfileActivity : AppCompatActivity() {
    private val sightingsList = ArrayList<Sighting>()
    private lateinit var recyclerView: RecyclerView
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val viewBinding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        auth = Firebase.auth

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
            UsersAPI.getInstance().getUser(auth.currentUser!!.uid) { author ->
                sightings.forEach { sightingData ->
                    val sightingId = sightingData["id"] as String
                    val postingDate =
                        (sightingData["postingDate"] as? Timestamp)?.toDate()?.toString() ?: ""
                    val animalName = (sightingData["commonName"] as? String) ?: ""
                    val scientificName = (sightingData["scientificName"] as? String) ?: ""
                    val location = sightingData["location"] as? String ?: ""
                    val sightDate =
                        (sightingData["sightDate"] as? Timestamp)?.toDate()?.toString() ?: ""
                    val imageUri =
                        (sightingData["imageId"] as? String).let { if (it != null) Uri.parse(it) else null }
                    val groupSize = sightingData["groupSize"] as? Int ?: 0
                    val distance = sightingData["distance"] as? Float ?: 0.0f
                    val observerType = sightingData["observerType"] as? String ?: ""
                    val sightingTime = sightingData["sightingTime"] as? String ?: ""

                    val sighting = Sighting(
                        id = sightingId,
                        userHandler = author.get("username") as String,
                        userIcon = if (author.get("avatar") != null) Uri.parse(author.get("avatar") as String) else null,
                        postingDate = postingDate,
                        animalName = animalName,
                        scientificName = scientificName,
                        location = location,
                        sightDate = sightDate,
                        imageUri = imageUri,
                        groupSize = groupSize,
                        distance = distance,
                        observerType = observerType,
                        sightingTime = sightingTime
                    )
                    this.sightingsList.add(sighting)
                }
                this.recyclerView.adapter?.notifyDataSetChanged()
            }
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
