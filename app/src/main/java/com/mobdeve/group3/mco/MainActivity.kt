package com.mobdeve.group3.mco

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.mobdeve.group3.mco.catalogue.CatalogueActivity
import com.mobdeve.group3.mco.landing.LandingActivity
import com.mobdeve.group3.mco.post.Post
import com.mobdeve.group3.mco.post.PostAdapter
import com.mobdeve.group3.mco.post.PostGenerator
import com.mobdeve.group3.mco.profile.ProfileActivity
import java.util.Date
import java.util.UUID

class MainActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var postAdapter: PostAdapter
    private lateinit var postList: ArrayList<Post>
    private lateinit var auth: FirebaseAuth

    companion object {
        const val REQUEST_CODE_EDIT_SIGHTING = 1001
    }

    private val addSightingActivityLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode != RESULT_OK) return@registerForActivityResult

            val commonName = result.data?.getStringExtra(AddSightingActivity.COMMON_NAME_KEY)
            val scientificName =
                result.data?.getStringExtra(AddSightingActivity.SCIENTIFIC_NAME_KEY)
            val groupSize = result.data?.getIntExtra(AddSightingActivity.GROUP_SIZE_KEY, 0)
            val distance = result.data?.getFloatExtra(AddSightingActivity.DISTANCE_KEY, 0.0f)
            val location = result.data?.getStringExtra(AddSightingActivity.LOCATION_KEY)
            val observerType =
                result.data?.getStringExtra(AddSightingActivity.OBSERVER_TYPE_KEY)
            val sightingDate =
                result.data?.getStringExtra(AddSightingActivity.SIGHTING_DATE_KEY)
            val sightingTime =
                result.data?.getStringExtra(AddSightingActivity.SIGHTING_TIME_KEY)
            val imageUriString = result.data?.getStringExtra("IMAGE_URI")
            val imageUri = if (!imageUriString.isNullOrEmpty()) Uri.parse(imageUriString) else null

            postList.add(
                0,
                Post(
                    id = UUID.randomUUID().toString(),
                    userHandler = "rosmar",
                    userIcon = R.drawable.profpic,
                    postingDate = Date().toString(),
                    animalName = commonName ?: "",
                    scientificName = scientificName ?: "",
                    location = location ?: "",
                    sightDate = sightingDate ?: "",
                    imageId = imageUri,
                    groupSize = groupSize ?: 0,
                    distance = distance ?: 0.0f,
                    observerType = observerType ?: "",
                    sightingTime = sightingTime ?: ""
                )
            )
            postAdapter.notifyItemInserted(0)
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
        postList = PostGenerator.generateData()
        postAdapter = PostAdapter(postList)
        recyclerView.adapter = postAdapter
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
        postList.sortByDescending { it.score }
        postAdapter.notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun sortPostsByRecency() {
        // Implement sorting logic based on recency (Descending order)
        postList.sortByDescending { it.postingDate }
        postAdapter.notifyDataSetChanged()
    }

    fun deletePostAtPosition(position: Int) {
        // Remove the post from the list
        postList.removeAt(position)

        // Notify the adapter that an item was removed
        postAdapter.notifyItemRemoved(position)
        postAdapter.notifyItemRangeChanged(position, postList.size)

        Toast.makeText(this, "Post deleted", Toast.LENGTH_SHORT).show()
    }

}
