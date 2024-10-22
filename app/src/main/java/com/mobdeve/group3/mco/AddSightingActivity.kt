package com.mobdeve.group3.mco

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.mobdeve.group3.mco.databinding.ActivityAddSightingBinding

class AddSightingActivity : AppCompatActivity() {
    companion object {
        const val COMMON_NAME_KEY = "COMMON_NAME_KEY"
        const val SCIENTIFIC_NAME_KEY = "SCIENTIFIC_NAME_KEY"
        const val GROUP_SIZE_KEY = "GROUP_SIZE_KEY"
        const val DISTANCE_KEY = "DISTANCE_KEY"
        const val LOCATION_KEY = "LOCATION_KEY"
        const val OBSERVER_TYPE_KEY = "OBSERVER_TYPE_KEY"
        const val SIGHTING_DATE_KEY = "SIGHTING_DATE_KEY"
        const val SIGHTING_TIME_KEY = "SIGHTING_TIME_KEY"
    }

    private var commonName: String = ""
    private var scientificName: String = ""
    private var groupSize: Int = 0
    private var distance: Float = 0.0f
    private var location: String = ""
    private var observerType: String = ""
    private var sightingDate: String = ""
    private var sightingTime: String = ""

    private lateinit var viewBinding: ActivityAddSightingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        viewBinding = ActivityAddSightingBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }

        viewBinding.etCommonName.addTextChangedListener { commonName = it.toString() }
        viewBinding.etSpecies.addTextChangedListener { scientificName = it.toString() }
        viewBinding.etGroupType.addTextChangedListener { groupSize = it.toString().toInt() }
        viewBinding.etDistance.addTextChangedListener { distance = it.toString().toFloat() }
        viewBinding.etLocation.addTextChangedListener { location = it.toString() }
        viewBinding.etObserver.addTextChangedListener { observerType = it.toString() }
        viewBinding.etSightingDate.addTextChangedListener { sightingDate = it.toString() }
        viewBinding.etSightingTime.addTextChangedListener { sightingTime = it.toString() }

        viewBinding.btnPost.setOnClickListener {
            val returnIntent = Intent()
            returnIntent.putExtra(COMMON_NAME_KEY, commonName)
            returnIntent.putExtra(SCIENTIFIC_NAME_KEY, scientificName)
            returnIntent.putExtra(GROUP_SIZE_KEY, groupSize)
            returnIntent.putExtra(DISTANCE_KEY, distance)
            returnIntent.putExtra(LOCATION_KEY, location)
            returnIntent.putExtra(OBSERVER_TYPE_KEY, observerType)
            returnIntent.putExtra(SIGHTING_DATE_KEY, sightingDate)
            returnIntent.putExtra(SIGHTING_TIME_KEY, sightingTime)
            setResult(RESULT_OK, returnIntent)
            finish()
        }
    }
}