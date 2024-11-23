package com.mobdeve.group3.mco.sighting

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
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

        this.recyclerView = findViewById(R.id.rcvMainPosts)

        sightingPostAdapter = SightingPostAdapter(this.sightingList)
        recyclerView.adapter = sightingPostAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)
    }
}
