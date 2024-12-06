package com.mobdeve.group3.mco.sighting

import android.Manifest
import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.core.widget.addTextChangedListener
import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.DocumentReference
import com.mobdeve.group3.mco.R
import com.mobdeve.group3.mco.catalogue.CatalogueGenerator
import com.mobdeve.group3.mco.databinding.ActivityAddSightingBinding
import com.mobdeve.group3.mco.db.SightingsAPI
import com.mobdeve.group3.mco.db.UsersAPI
import com.mobdeve.group3.mco.storage.ImagesAPI
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import kotlin.Any
import kotlin.String

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
        const val IMAGE_ID_KEY = "IMAGE_ID_KEY"
    }

    private lateinit var auth: FirebaseAuth
    private var commonName: String = ""
    private var scientificName: String = ""
    private var groupSize: Int = 0
    private var distance: Float = 0.0f
    private var location: String = ""
    private var observerType: String = ""
    private var sightingDate: String = ""
    private var sightingTime: String = ""

    private lateinit var viewBinding: ActivityAddSightingBinding
    private var imageUri: Uri? = null
    private lateinit var imageView: ImageView

    private lateinit var cameraResultLauncher: ActivityResultLauncher<Intent>
    private lateinit var galleryResultLauncher: ActivityResultLauncher<Intent>

    private val REQUEST_CAMERA = 1
    private val categories = CatalogueGenerator.generateCatalogueSpecies()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityAddSightingBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        imageView = viewBinding.imgSelectedPhoto
        auth = Firebase.auth

        // Extract common names for dropdown
        val commonNames = categories.map { it.commonName }
        Log.d("Debug", "Categories: $categories") // Check if categories are populated
        Log.d("Debug", "Common Names: $commonNames") // Log the extracted common names

        // Set up AutoCompleteTextView with adapter
        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, commonNames)
        viewBinding.etCommonName.setAdapter(adapter)
        Log.d("Debug", "Adapter Count: ${adapter.count}") // Check the number of items in the adapter

        // Show dropdown when the field is clicked
        viewBinding.etCommonName.setOnTouchListener { _, _ ->
            viewBinding.etCommonName.showDropDown()
            false
        }

        // Show dropdown as the user types
        viewBinding.etCommonName.addTextChangedListener {
            if (!it.isNullOrEmpty()) {
                viewBinding.etCommonName.showDropDown()
            }
        }

        // Set listener for item selection
        viewBinding.etCommonName.setOnItemClickListener { parent, _, position, _ ->
            commonName = parent.getItemAtPosition(position) as String
            val selectedSpecies = categories.find { it.commonName == commonName }
            scientificName = selectedSpecies?.scientificName ?: ""
            viewBinding.etSpecies.setText(scientificName) // Auto-fill scientific name
        }

        // Set listeners for input fields
        //viewBinding.etCommonName.addTextChangedListener { commonName = it.toString() }
        //viewBinding.etSpecies.addTextChangedListener { scientificName = it.toString() }

        viewBinding.etGroupType.addTextChangedListener { text ->
            if (!text.isNullOrEmpty()) {
                try {
                    // Only parse if there's text
                    groupSize = text.toString().toInt()
                } catch (e: NumberFormatException) {
                    // If the input is invalid, reset the value or handle it
                    groupSize = 0
                }
            } else {
                // Handle the case where the input field is empty (after backspace)
                groupSize = 0
            }
        }

        viewBinding.etDistance.addTextChangedListener { text ->
            if (!text.isNullOrEmpty()) {
                try {
                    // Only parse if there's text
                    distance = text.toString().toFloat()
                } catch (e: NumberFormatException) {
                    // If the input is invalid, reset the value or handle it
                    distance = 0.0F
                }
            } else {
                // Handle the case where the input field is empty (after backspace)
                distance = 0.0F
            }
        }

        viewBinding.etLocation.addTextChangedListener { location = it.toString() }
        viewBinding.etObserver.addTextChangedListener { observerType = it.toString() }

        viewBinding.etSightingDate.setOnClickListener {
            showDatePickerDialog()
        }

        viewBinding.etSightingTime.setOnClickListener {
            showTimePickerDialog()
        }

        viewBinding.btnPost.setOnClickListener {
            val userId = auth.currentUser?.uid ?: return@setOnClickListener
            val postingDate = Timestamp.now()

            // Use UsersAPI to fetch the user reference
            val authorRef = UsersAPI.getInstance().getUserReference(userId)
            if (authorRef == null) {
                Log.e("AddSighting", "Invalid user reference for userId: $userId")
                return@setOnClickListener
            }

            // Combine sighting date and time
            val combinedSightTime = SimpleDateFormat("MM/dd/yyyy hh:mm", Locale.getDefault()).parse(
                "$sightingDate $sightingTime"
            )?.let { Timestamp(it) }

            UsersAPI.getInstance().getUser(userId) { userData ->
                val username = userData["username"] as? String ?: "Unknown User"
                val avatarUrl = userData["avatar"] as? String ?: ""

                fun uploadSighting(imageId: String?) {
                    // Prepare sighting data
                    val sightingData = hashMapOf<String, Any?>(
                        "commonName" to commonName,
                        "scientificName" to scientificName,
                        "groupSize" to groupSize,
                        "distance" to "${distance}km",
                        "location" to location,
                        "observerType" to observerType,
                        "sightTime" to (combinedSightTime ?: Timestamp.now()),
                        "postingDate" to postingDate,
                        "imageId" to imageId,
                        "author" to authorRef,
                        "comments" to listOf<DocumentReference>(),
                        "score" to 0
                    )

                    Log.d("SightingData", sightingData.toString())
                    Log.d("ImageId", imageId.toString())

                    // Add the sighting to Firestore
                    SightingsAPI.getInstance().addSighting(sightingData) { sightingId ->
                        // Return the sightingId back to the previous activity
                        val returnIntent = Intent()
                        returnIntent.putExtra("SIGHTING_ID", sightingId)
                        returnIntent.putExtra("userHandler", username)
                        returnIntent.putExtra("userIcon", avatarUrl)
                        returnIntent.putExtra("COMMON_NAME_KEY", commonName)
                        returnIntent.putExtra("SCIENTIFIC_NAME_KEY", scientificName)
                        returnIntent.putExtra("GROUP_SIZE_KEY", groupSize)
                        returnIntent.putExtra("DISTANCE_KEY", distance)
                        returnIntent.putExtra("LOCATION_KEY", location)
                        returnIntent.putExtra("OBSERVER_TYPE_KEY", observerType)
                        returnIntent.putExtra("SIGHTING_DATE_KEY", sightingDate)
                        returnIntent.putExtra("SIGHTING_TIME_KEY", sightingTime)
                        returnIntent.putExtra("IMAGE_ID_KEY", imageId)
                        returnIntent.putExtra(
                            "postingDate",
                            postingDate.toDate().toString()
                        ) // Simple string format

                        setResult(RESULT_OK, returnIntent)
                        finish()
                    }
                }

                if (imageUri != null) {
                    ImagesAPI.getInstance().putSightingImage(
                        ImagesAPI.getByteArrayFromInputStream(
                            contentResolver.openInputStream(
                                imageUri!!
                            )
                        )
                    ) { uploadedImageId ->
                        uploadSighting(uploadedImageId)
                    }
                } else if (imageView.drawable != null) {
                    ImagesAPI.getInstance().putSightingImage(
                        ImagesAPI.getByteArrayFromBitmap(
                            imageView.drawable.toBitmap()
                        )
                    ) { uploadedImageId ->
                        uploadSighting(uploadedImageId)
                    }
                } else {
                    uploadSighting(null)
                }
            }
        }

        // Initialize the ActivityResultLauncher for Camera
        cameraResultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == RESULT_OK) {
                val imageBitmap = result.data?.getParcelableExtra<Bitmap>("data")

                if (imageBitmap != null) {
                    imageView.setImageBitmap(imageBitmap)
                    imageView.visibility = View.VISIBLE
                }
            }
        }

        // Initialize the ActivityResultLauncher for Gallery
        galleryResultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == RESULT_OK) {
                imageUri = result.data?.data
                imageView.setImageURI(imageUri)
                imageView.visibility = View.VISIBLE
            }
        }

        // Handle "Add Photo" button click to show the photo options menu
        viewBinding.btnAddPhoto.setOnClickListener {
            showPhotoOptionsMenu(it)
        }
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(
            this,
            DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                sightingDate = "$dayOfMonth/${month + 1}/$year"
                viewBinding.etSightingDate.setText(sightingDate)  // Update the EditText
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )

        datePickerDialog.show()

        val datePickerDialogButton = datePickerDialog.getButton(DatePickerDialog.BUTTON_POSITIVE)
        datePickerDialogButton.setTextColor(Color.WHITE)
        val cancelButton = datePickerDialog.getButton(DatePickerDialog.BUTTON_NEGATIVE)
        cancelButton.setTextColor(Color.WHITE)

    }

    private fun showTimePickerDialog() {
        val calendar = Calendar.getInstance()
        val timePickerDialog = TimePickerDialog(
            this,
            TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                sightingTime = String.format("%02d:%02d", hourOfDay, minute)
                viewBinding.etSightingTime.setText(sightingTime)  // Update the EditText
            },
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            true
        )

        timePickerDialog.show()

        val timePickerDialogButton = timePickerDialog.getButton(TimePickerDialog.BUTTON_POSITIVE)
        timePickerDialogButton.setTextColor(Color.WHITE)
        val cancelButton = timePickerDialog.getButton(TimePickerDialog.BUTTON_NEGATIVE)
        cancelButton.setTextColor(Color.WHITE)
    }

    // Function to show the photo options menu
    private fun showPhotoOptionsMenu(view: View) {
        val popupMenu = PopupMenu(this, view)
        menuInflater.inflate(R.menu.photo_options_menu, popupMenu.menu)

        // Set up the menu item click listener
        popupMenu.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.option_camera -> {
                    if (ContextCompat.checkSelfPermission(
                            this, Manifest.permission.CAMERA
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {
                        openCamera()
                    } else {
                        ActivityCompat.requestPermissions(
                            this, arrayOf(Manifest.permission.CAMERA),
                            REQUEST_CAMERA
                        )
                    }
                    true
                }

                R.id.option_gallery -> {
                    openGallery()
                    true
                }

                else -> false
            }
        }

        popupMenu.show()
    }

    // Function to open camera
    private fun openCamera() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (cameraIntent.resolveActivity(packageManager) != null) {
            cameraResultLauncher.launch(cameraIntent)
        }
    }

    // Open gallery with better compatibility
    @SuppressLint("IntentReset")
    private fun openGallery() {
        val galleryIntent = Intent(Intent.ACTION_PICK)
        galleryIntent.type = "image/*"  // Ensures the user picks an image
        galleryResultLauncher.launch(galleryIntent)
    }

    // Handle permission result for camera
    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == REQUEST_CAMERA) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera()
            } else {
                Toast.makeText(
                    this,
                    "Camera permission is required to capture a photo",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}
