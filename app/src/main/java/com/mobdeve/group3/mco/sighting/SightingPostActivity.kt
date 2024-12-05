package com.mobdeve.group3.mco.sighting

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mobdeve.group3.mco.R
import com.mobdeve.group3.mco.databinding.ActivityMainBinding

class SightingPostActivity : AppCompatActivity() {
    private val sightingList = ArrayList<Sighting>()
    private lateinit var recyclerView: RecyclerView
    private lateinit var sightingPostAdapter: SightingPostAdapter  // Store the adapter reference
    private lateinit var editSightingActivityLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val viewBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        editSightingActivityLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == RESULT_OK) {
                    val sightingId = result.data?.getStringExtra("SIGHTING_ID")

                    if (sightingId != null) {
                        val updatedSighting = Sighting(
                            id = sightingId,
                            userHandler = result.data?.getStringExtra("userHandler") ?: "Unknown",
                            userIcon = Uri.parse(result.data?.getStringExtra("userIcon") ?: ""),
                            postingDate = result.data?.getStringExtra("postingDate") ?: "",
                            animalName = result.data?.getStringExtra(AddSightingActivity.COMMON_NAME_KEY)
                                ?: "",
                            scientificName = result.data?.getStringExtra(AddSightingActivity.SCIENTIFIC_NAME_KEY)
                                ?: "",
                            location = result.data?.getStringExtra(AddSightingActivity.LOCATION_KEY)
                                ?: "",
                            sightDate = result.data?.getStringExtra(AddSightingActivity.SIGHTING_DATE_KEY)
                                ?: "",
                            sightingTime = result.data?.getStringExtra(AddSightingActivity.SIGHTING_TIME_KEY)
                                ?: "",
                            imageId = result.data?.getStringExtra(AddSightingActivity.IMAGE_ID_KEY),
                            groupSize = result.data?.getIntExtra(
                                AddSightingActivity.GROUP_SIZE_KEY,
                                0
                            ) ?: 0,
                            distance = result.data?.getFloatExtra(
                                AddSightingActivity.DISTANCE_KEY,
                                0.0f
                            ) ?: 0.0f,
                            observerType = result.data?.getStringExtra(AddSightingActivity.OBSERVER_TYPE_KEY)
                                ?: "",
                            isOwnedByCurrentUser = true
                        )

                        // Find and update sighting in the list
                        val position = sightingList.indexOfFirst { it.id == sightingId }
                        if (position != -1) {
                            sightingList[position] = updatedSighting
                            sightingPostAdapter.notifyItemChanged(position)
                        }
                    }
                }
            }

        this.recyclerView = findViewById(R.id.rcvMainPosts)

        sightingPostAdapter = SightingPostAdapter(this.sightingList, editSightingActivityLauncher)
        recyclerView.adapter = sightingPostAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)
    }
}
