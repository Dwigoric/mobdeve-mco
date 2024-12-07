package com.mobdeve.group3.mco

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Color
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
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.DocumentReference
import com.mobdeve.group3.mco.catalogue.CatalogueActivity
import com.mobdeve.group3.mco.db.SightingsAPI
import com.mobdeve.group3.mco.db.VotesAPI
import com.mobdeve.group3.mco.landing.LandingActivity
import com.mobdeve.group3.mco.profile.ProfileActivity
import com.mobdeve.group3.mco.sighting.AddSightingActivity
import com.mobdeve.group3.mco.sighting.Sighting
import com.mobdeve.group3.mco.sighting.SightingPostAdapter
import com.mobdeve.group3.mco.storage.ImagesAPI
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var sightingPostAdapter: SightingPostAdapter
    private lateinit var sightingList: ArrayList<Sighting>
    private lateinit var auth: FirebaseAuth

    private val addSightingActivityLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode != RESULT_OK) return@registerForActivityResult

            val sightingId =
                result.data?.getStringExtra("SIGHTING_ID") ?: return@registerForActivityResult
            val sightingImg = result.data?.getStringExtra("imageId")

            val newSighting = Sighting(
                id = sightingId,
                userHandler = result.data?.getStringExtra("userHandler") ?: "Unknown",
                userIcon = result.data?.getStringExtra("userIcon")?.let { Uri.parse(it) },
                postingDate = Date().toString(),
                animalName = result.data?.getStringExtra(AddSightingActivity.COMMON_NAME_KEY) ?: "",
                scientificName = result.data?.getStringExtra(AddSightingActivity.SCIENTIFIC_NAME_KEY)
                    ?: "",
                location = result.data?.getStringExtra(AddSightingActivity.LOCATION_KEY) ?: "",
                sightDate = result.data?.getStringExtra(AddSightingActivity.SIGHTING_DATE_KEY)
                    ?: "",
                imageId = sightingImg?.let { Uri.parse(it) },
                groupSize = result.data?.getIntExtra(AddSightingActivity.GROUP_SIZE_KEY, 0) ?: 0,
                distance = result.data?.getFloatExtra(AddSightingActivity.DISTANCE_KEY, 0.0f)
                    ?: 0.0f,
                observerType = result.data?.getStringExtra(AddSightingActivity.OBSERVER_TYPE_KEY)
                    ?: "",
                sightingTime = result.data?.getStringExtra(AddSightingActivity.SIGHTING_TIME_KEY)
                    ?: "",
                isOwnedByCurrentUser = true,
                score = 0
            )

            // Add to the top of the list
            sightingList.add(0, newSighting)
            sightingPostAdapter.notifyItemInserted(0)

            // Scroll to the new item
            recyclerView.scrollToPosition(0)

            // Dynamically update image if URI was delayed
            if (sightingImg.isNullOrEmpty()) {
                // Listen for the image upload completion
                SightingsAPI.getInstance().getImageUriForSighting(sightingId) { imageUri ->
                    if (imageUri != null) {
                        val index = sightingList.indexOfFirst { it.id == sightingId }
                        if (index >= 0) {
                            sightingList[index].imageId = Uri.parse(imageUri)
                            sightingPostAdapter.notifyItemChanged(index)
                        }
                    }
                }
            }
        }


    private val editSightingActivityLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val data = result.data
                val sightingId = data?.getStringExtra("SIGHTING_ID")
                val userHandler = data?.getStringExtra("userHandler")
                val userIcon = data?.getStringExtra("userIcon")
                val sightingImg = data?.getStringExtra("IMAGE_ID_KEY")

                if (sightingId != null) {
                    // Create the updated sighting object with the new data from the result
                    val updatedSighting = Sighting(
                        id = sightingId,
                        userHandler = userHandler ?: "Unknown",
                        userIcon = if (!userIcon.isNullOrEmpty()) Uri.parse(userIcon) else null,
                        postingDate = "Posting on " + SimpleDateFormat(
                            "EEE MMM dd yyyy HH:mm",
                            Locale.ENGLISH
                        ).format(Date()),
                        animalName = data.getStringExtra(AddSightingActivity.COMMON_NAME_KEY)
                            ?: "",
                        scientificName = data.getStringExtra(AddSightingActivity.SCIENTIFIC_NAME_KEY)
                            ?: "",
                        location = data.getStringExtra(AddSightingActivity.LOCATION_KEY) ?: "",
                        sightDate = data.getStringExtra(AddSightingActivity.SIGHTING_DATE_KEY)
                            ?: "",
                        imageId = if (!sightingImg.isNullOrEmpty()) Uri.parse(sightingImg) else null,
                        groupSize = data.getIntExtra(AddSightingActivity.GROUP_SIZE_KEY, 0),
                        distance = data.getFloatExtra(AddSightingActivity.DISTANCE_KEY, 0.0f),
                        observerType = data.getStringExtra(AddSightingActivity.OBSERVER_TYPE_KEY)
                            ?: "",
                        sightingTime = data.getStringExtra(AddSightingActivity.SIGHTING_TIME_KEY)
                            ?: "",
                        isOwnedByCurrentUser = true
                    )

                    // Find the position of the updated sighting in the list
                    val position = sightingList.indexOfFirst { it.id == sightingId }
                    if (position != -1) {
                        // Update the sighting in the list with the new data
                        sightingList[position] = updatedSighting

                        // Notify the adapter that the item has changed
                        sightingPostAdapter.notifyItemChanged(position)

                        // Dynamically update image if URI was delayed
                        if (!sightingImg.isNullOrEmpty()) {
                            // If the image was changed, we should update the image in the list
                            SightingsAPI.getInstance()
                                .getImageUriForSighting(sightingId) { imageUri ->
                                    if (imageUri != null) {
                                        val index =
                                            sightingList.indexOfFirst { it.id == sightingId }
                                        if (index >= 0) {
                                            sightingList[index].imageId = Uri.parse(imageUri)
                                            sightingPostAdapter.notifyItemChanged(index)
                                        }
                                    }
                                }
                        }
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

        val btnSort = findViewById<ImageButton>(R.id.btnSort)

        val nightModeFlags = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        val iconColor = if (nightModeFlags == Configuration.UI_MODE_NIGHT_YES) {
            Color.WHITE
        } else {
            Color.BLACK
        }

        btnSort.setColorFilter(iconColor)

        // Set up sort button click listener
        btnSort.setOnClickListener {
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

        // FloatingActionButton listener
        findViewById<FloatingActionButton>(R.id.nav_add).setOnClickListener {
            val addSightingIntent = Intent(applicationContext, AddSightingActivity::class.java)
            addSightingActivityLauncher.launch(addSightingIntent)
        }

        // Set a long press listener on the Profile tab for the popup menu
        val profileMenuItem = bottomNav.menu.findItem(R.id.nav_profile)
        findViewById<View>(profileMenuItem.itemId).setOnLongClickListener {
            showLogoutPopup(it) // Show the popup menu
            true
        }
    }

    override fun onStart() {
        super.onStart()
        if (auth.currentUser == null) {
            currentFocus?.clearFocus()
            val launchIntent = Intent(applicationContext, LandingActivity::class.java)
            launchIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(launchIntent)
            finish()
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
                        val sightingImgURL = sightingData["imageId"] as? String ?: ""
                        val sightingImg =
                            if (sightingImgURL.isNotEmpty()) Uri.parse(sightingImgURL) else null
                        val isOwnedByCurrentUser = authorId == currentUserId

                        Log.d(
                            "loadSightings",
                            "Fetched user data: username=$userHandler, avatar=$userIconUrl, imageId=$sightingImg"
                        )

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
                            imageId = sightingImg,
                            groupSize = (sightingData["groupSize"] as? Long)?.toInt() ?: 0,
                            distance = (sightingData["distance"] as? String)?.replace("km", "")
                                ?.toFloat() ?: 0.0f,
                            observerType = sightingData["observerType"] as? String ?: "",
                            sightingTime = (sightingData["sightTime"] as? Timestamp)?.toDate()
                                ?.toString() ?: "",
                            isOwnedByCurrentUser = isOwnedByCurrentUser,
                            score = (sightingData["score"] as? Long)?.toInt() ?: 0
                        )

                        tempList.add(sighting)

                        // Log progress for each sighting
                        Log.d(
                            "loadSightings",
                            "Sighting added to tempList: id=${sighting.id}, animalName=${sighting.animalName}, image=${sighting.imageId}"
                        )

                        // Update RecyclerView after all sightings are processed
                        @SuppressLint("NotifyDataSetChanged")
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

                R.id.option_sort_recency2 -> {
                    // Sort based on recency
                    sortPostsByRecency2()
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

    @SuppressLint("NotifyDataSetChanged")
    private fun sortPostsByRecency2() {
        // Implement sorting logic based on recency (Descending order)
        sightingList.sortByDescending { it.sightDate }
        sightingPostAdapter.notifyDataSetChanged()
    }

    fun deletePostAtPosition(position: Int) {
        val sightingToDelete = sightingList[position]
        val sightingId =
            sightingToDelete.id

        VotesAPI.getInstance().removeVotesFromSighting(sightingId) {}
        SightingsAPI.getInstance().getSighting(sightingId) { sightingData ->
            val imageId = sightingData["imageId"] as? String
            if (imageId != null) {
                ImagesAPI.getInstance().deleteSightingImage(imageId) { success ->
                    if (!success) {
                        Log.e(
                            "DeleteSightingImage",
                            "Failed to delete image from storage: $sightingId"
                        )
                    }
                }
            }

            SightingsAPI.getInstance().deleteSighting(sightingId) { success ->
                if (success) {
                    // If deletion from Firestore is successful, update the UI
                    sightingList.removeAt(position)
                    sightingPostAdapter.notifyItemRemoved(position)
                    sightingPostAdapter.notifyItemRangeChanged(position, sightingList.size)

                    Toast.makeText(this, "Post deleted", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(
                        this,
                        "Failed to delete post from database",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
            }
        }
    }
}