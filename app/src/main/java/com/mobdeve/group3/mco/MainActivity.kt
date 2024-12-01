package com.mobdeve.group3.mco

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.ImageButton
import android.widget.PopupMenu
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import com.google.firebase.firestore.DocumentReference
import com.mobdeve.group3.mco.catalogue.CatalogueActivity
import com.mobdeve.group3.mco.db.SightingsAPI
import com.mobdeve.group3.mco.landing.LandingActivity
import com.mobdeve.group3.mco.profile.ProfileActivity
import com.mobdeve.group3.mco.sighting.AddSightingActivity
import com.mobdeve.group3.mco.sighting.Sighting
import com.mobdeve.group3.mco.sighting.SightingPostAdapter
import java.util.Date

class MainActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var sightingPostAdapter: SightingPostAdapter
    private lateinit var sightingList: ArrayList<Sighting>
    private lateinit var auth: FirebaseAuth

    private val addSightingActivityLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode != RESULT_OK) return@registerForActivityResult

            // Extract sighting ID and user data
            val sightingId = result.data?.getStringExtra("SIGHTING_ID")
            val userHandler = result.data?.getStringExtra("userHandler") // Fetch username
            val userIcon = result.data?.getStringExtra("userIcon") // Fetch avatar URL

            // Make sure sightingId is not null
            if (sightingId != null) {
                // Extract other sighting data from the result if needed
                val commonName =
                    result.data?.getStringExtra(AddSightingActivity.Companion.COMMON_NAME_KEY)
                val scientificName =
                    result.data?.getStringExtra(AddSightingActivity.Companion.SCIENTIFIC_NAME_KEY)
                val groupSize =
                    result.data?.getIntExtra(AddSightingActivity.Companion.GROUP_SIZE_KEY, 0)
                val distance =
                    result.data?.getFloatExtra(AddSightingActivity.Companion.DISTANCE_KEY, 0.0f)
                val location =
                    result.data?.getStringExtra(AddSightingActivity.Companion.LOCATION_KEY)
                val observerType =
                    result.data?.getStringExtra(AddSightingActivity.Companion.OBSERVER_TYPE_KEY)
                val sightingDate =
                    result.data?.getStringExtra(AddSightingActivity.Companion.SIGHTING_DATE_KEY)
                val sightingTime =
                    result.data?.getStringExtra(AddSightingActivity.Companion.SIGHTING_TIME_KEY)
                val imageUriString = result.data?.getStringExtra("IMAGE_URI")
                val imageUri =
                    if (!imageUriString.isNullOrEmpty()) Uri.parse(imageUriString) else null

                // Add the sighting data to the list
                sightingList.add(
                    0,
                    Sighting(
                        id = sightingId,
                        userHandler = userHandler ?: "Unknown",
                        userIcon = if (!userIcon.isNullOrEmpty()) Uri.parse(userIcon) else null,
                        postingDate = Date().toString(),
                        animalName = commonName ?: "",
                        scientificName = scientificName ?: "",
                        location = location ?: "",
                        sightDate = sightingDate ?: "",
                        imageUri = imageUri,
                        groupSize = groupSize ?: 0,
                        distance = distance ?: 0.0f,
                        observerType = observerType ?: "",
                        sightingTime = sightingTime ?: "",
                        isOwnedByCurrentUser = true,
                        score = 0
                    )
                )

                // Notify the adapter that the data has changed
                sightingPostAdapter.notifyItemInserted(0)

                // Optionally, scroll to the new item
                recyclerView.scrollToPosition(0)
            }
        }

    private val editSightingActivityLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val data = result.data
                val sightingId = data?.getStringExtra("SIGHTING_ID")
                val userHandler = data?.getStringExtra("userHandler")
                val userIcon = data?.getStringExtra("userIcon")
                val postingDate = data?.getStringExtra("postingDate")

                if (sightingId != null) {
                    // Extract other sighting data from the result
                    val commonName = data?.getStringExtra(AddSightingActivity.COMMON_NAME_KEY)
                    val scientificName = data?.getStringExtra(AddSightingActivity.SCIENTIFIC_NAME_KEY)
                    val groupSize = data?.getIntExtra(AddSightingActivity.GROUP_SIZE_KEY, 0)
                    val distance = data?.getFloatExtra(AddSightingActivity.DISTANCE_KEY, 0.0f)
                    val location = data?.getStringExtra(AddSightingActivity.LOCATION_KEY)
                    val observerType = data?.getStringExtra(AddSightingActivity.OBSERVER_TYPE_KEY)
                    val sightingDate = data?.getStringExtra(AddSightingActivity.SIGHTING_DATE_KEY)
                    val sightingTime = data?.getStringExtra(AddSightingActivity.SIGHTING_TIME_KEY)
                    val imageUriString = data?.getStringExtra("IMAGE_URI")
                    val imageUri = if (!imageUriString.isNullOrEmpty()) Uri.parse(imageUriString) else null

                    // Create an updated Sighting object
                    val updatedSighting = Sighting(
                        id = sightingId,
                        userHandler = userHandler ?: "Unknown",
                        userIcon = if (!userIcon.isNullOrEmpty()) Uri.parse(userIcon) else null,
                        postingDate = postingDate ?: "",
                        animalName = commonName ?: "",
                        scientificName = scientificName ?: "",
                        location = location ?: "",
                        sightDate = sightingDate ?: "",
                        imageUri = imageUri,
                        groupSize = groupSize ?: 0,
                        distance = distance ?: 0.0f,
                        observerType = observerType ?: "",
                        sightingTime = sightingTime ?: "",
                        isOwnedByCurrentUser = true
                    )

                    // Find the position of the sighting to update
                    val position = sightingList.indexOfFirst { it.id == sightingId }
                    if (position != -1) {
                        sightingList[position] = updatedSighting
                        sightingPostAdapter.notifyItemChanged(position)
                    }
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        auth = Firebase.auth

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        if (auth.currentUser == null) {
            val launchIntent = Intent(applicationContext, LandingActivity::class.java)
            launchIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(launchIntent)
            finish()
        }

        // Set up sort button click listener
        findViewById<ImageButton>(R.id.btnSort).setOnClickListener {
            showSortPopup(it)
        }

        setupRecyclerView()


        val bottomNav: BottomNavigationView = findViewById(R.id.bottom_nav)
        bottomNav.selectedItemId = R.id.nav_home
        bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.nav_home -> {
                    true
                }

                R.id.nav_add -> {
                    val addSightingIntent =
                        Intent(applicationContext, AddSightingActivity::class.java)
                    addSightingActivityLauncher.launch(addSightingIntent)
                    true
                }

                R.id.nav_catalogue -> {
                    val catalogueIntent = Intent(applicationContext, CatalogueActivity::class.java)
                    catalogueIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(catalogueIntent)
                    finish()
                    true
                }

                R.id.nav_profile -> {
                    val profileIntent = Intent(applicationContext, ProfileActivity::class.java)
                    profileIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(profileIntent)
                    finish()
                    true
                }

                else -> false
            }
        }

        // Set a long press listener on the Profile tab for the popup menu
        val profileMenuItem = bottomNav.menu.findItem(R.id.nav_profile)
        findViewById<View>(profileMenuItem.itemId).setOnLongClickListener {
            showLogoutPopup(it) // Show the popup menu
            true
        }
    }

    private fun setupRecyclerView() {
        recyclerView = findViewById(R.id.rcvMainPosts)
        recyclerView.layoutManager = LinearLayoutManager(this)
        sightingList = ArrayList<Sighting>()
        sightingPostAdapter = SightingPostAdapter(sightingList, editSightingActivityLauncher)
        recyclerView.adapter = sightingPostAdapter

        loadSightings()
    }

    private fun loadSightings() {
        val currentUserId = Firebase.auth.currentUser?.uid ?: return
        SightingsAPI.getInstance().getSightings { sightingsData ->
            Log.d("loadSightings", "Sightings fetched: ${sightingsData.size} items")
            sightingList.clear()

            val tempList = ArrayList<Sighting>()

            for (sightingData in sightingsData) {
                val authorRef = sightingData["author"] as? DocumentReference
                if (authorRef == null) {
                    Log.e("loadSightings", "Invalid author reference in sighting: $sightingData")
                    continue
                }

                // Fetch the user data from the author reference
                authorRef.get().addOnSuccessListener { documentSnapshot ->
                    if (documentSnapshot.exists()) {
                        val authorId = documentSnapshot.id
                        val userData = documentSnapshot.data ?: emptyMap<String, Any>()
                        val userHandler = userData["username"] as? String ?: "Unknown User"
                        val userIconUrl = userData["avatar"] as? String ?: ""
                        val userIcon =
                            if (userIconUrl.isNotEmpty()) Uri.parse(userIconUrl) else null

                        val isOwnedByCurrentUser = authorId == currentUserId

                        Log.d(
                            "loadSightings",
                            "Fetched user data: username=$userHandler, avatar=$userIconUrl"
                        )

                        // Check and update the imageUri to null if it's an empty string
                        val imageUriString = (sightingData["imageUri"] as? String)?.takeIf { it.isNotEmpty() }
                        val imageUri = imageUriString?.let { Uri.parse(it) }

                        // Create the Sighting object
                        val sighting = Sighting(
                            id = sightingData["id"] as? String ?: "",
                            userHandler = userHandler,
                            userIcon = userIcon,
                            postingDate = (sightingData["postingDate"] as? Timestamp)?.toDate()
                                ?.toString() ?: "",
                            animalName = sightingData["commonName"] as? String ?: "",
                            scientificName = sightingData["scientificName"] as? String ?: "",
                            location = sightingData["location"] as? String ?: "",
                            sightDate = (sightingData["sightTime"] as? Timestamp)?.toDate()
                                ?.toString() ?: "",
                            imageUri = imageUri,
                            groupSize = (sightingData["groupSize"] as? Long)?.toInt() ?: 0,
                            distance = (sightingData["distance"] as? String)?.replace("km", "")
                                ?.toFloat() ?: 0.0f,
                            observerType = sightingData["observerType"] as? String ?: "",
                            sightingTime = (sightingData["sightTime"] as? Timestamp)?.toDate()
                                ?.toString() ?: "",
                            isOwnedByCurrentUser = isOwnedByCurrentUser
                        )

                        tempList.add(sighting)

                        // Log progress for each sighting
                        Log.d(
                            "loadSightings",
                            "Sighting added to tempList: id=${sighting.id}, animalName=${sighting.animalName}, image=${sighting.imageUri}"
                        )

                        // Update RecyclerView after all sightings are processed
                        if (tempList.size == sightingsData.size) {
                            sightingList.addAll(tempList.sortedByDescending { it.postingDate })
                            sightingPostAdapter.notifyDataSetChanged()
                            Log.d(
                                "loadSightings",
                                "RecyclerView updated with ${sightingList.size} items"
                            )
                        }
                    } else {
                        Log.e("loadSightings", "User not found for author reference: $authorRef")
                    }
                }.addOnFailureListener { exception ->
                    Log.e("loadSightings", "Error fetching user data: ${exception.message}")
                }
            }
        }
    }


    private fun showLogoutPopup(view: View) {
        val popup = PopupMenu(this, view)
        popup.menuInflater.inflate(R.menu.logout_popup_menu, popup.menu)

        popup.setOnMenuItemClickListener { menuItem: MenuItem ->
            when (menuItem.itemId) {
                R.id.menu_logout -> {
                    // Sign out the user
                    auth.signOut()

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

    private fun showSortPopup(view: View) {
        val popupMenu = PopupMenu(this, view)
        menuInflater.inflate(R.menu.sort_options_menu, popupMenu.menu)

        popupMenu.setOnMenuItemClickListener { item: MenuItem ->
            when (item.itemId) {
                R.id.option_sort_score -> {
                    // Sort based on score
                    sortPostsByScore()
                    true
                }

                R.id.option_sort_recency -> {
                    // Sort based on recency
                    sortPostsByRecency()
                    true
                }

                else -> false
            }
        }

        popupMenu.show()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun sortPostsByScore() {
        // Implement sorting logic based on score (Descending order)
        sightingList.sortByDescending { it.score }
        sightingPostAdapter.notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun sortPostsByRecency() {
        // Implement sorting logic based on recency (Descending order)
        sightingList.sortByDescending { it.postingDate }
        sightingPostAdapter.notifyDataSetChanged()
    }

    fun deletePostAtPosition(position: Int) {
        val sightingToDelete = sightingList[position]
        val sightingId =
            sightingToDelete.id

        SightingsAPI.getInstance().deleteSighting(sightingId) { success ->
            if (success) {
                // If deletion from Firestore is successful, update the UI
                sightingList.removeAt(position)
                sightingPostAdapter.notifyItemRemoved(position)
                sightingPostAdapter.notifyItemRangeChanged(position, sightingList.size)

                Toast.makeText(this, "Post deleted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Failed to delete post from database", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }
}