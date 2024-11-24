package com.mobdeve.group3.mco.sighting

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.widget.addTextChangedListener
import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.DocumentReference
import com.mobdeve.group3.mco.R
import com.mobdeve.group3.mco.databinding.ActivityAddSightingBinding
import com.mobdeve.group3.mco.db.SightingsAPI
import com.mobdeve.group3.mco.db.UsersAPI
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Locale

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityAddSightingBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        imageView = viewBinding.imgSelectedPhoto
        auth = Firebase.auth

        // Set listeners for input fields
        viewBinding.etCommonName.addTextChangedListener { commonName = it.toString() }
        viewBinding.etSpecies.addTextChangedListener { scientificName = it.toString() }
        viewBinding.etGroupType.addTextChangedListener { groupSize = it.toString().toInt() }
        viewBinding.etDistance.addTextChangedListener { distance = it.toString().toFloat() }
        viewBinding.etLocation.addTextChangedListener { location = it.toString() }
        viewBinding.etObserver.addTextChangedListener { observerType = it.toString() }
        viewBinding.etSightingDate.addTextChangedListener { sightingDate = it.toString() }
        viewBinding.etSightingTime.addTextChangedListener { sightingTime = it.toString() }

        viewBinding.btnPost.setOnClickListener {
            val userId = auth.currentUser?.uid ?: return@setOnClickListener

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
                // Prepare sighting data
                val sightingData = hashMapOf<String, Any>(
                    "commonName" to commonName,
                    "scientificName" to scientificName,
                    "groupSize" to groupSize,
                    "distance" to "${distance}km",
                    "location" to location,
                    "observerType" to observerType,
                    "sightTime" to (combinedSightTime ?: Timestamp.now()),
                    "postingDate" to Timestamp.now(),
                    "imageUri" to (imageUri?.toString() ?: ""),
                    "author" to authorRef, // DocumentReference
                    "comments" to listOf<DocumentReference>() // Empty list for comments
                )

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
                    returnIntent.putExtra("IMAGE_URI", imageUri.toString())

                    setResult(RESULT_OK, returnIntent)
                    finish()
                }
            }
        }

        // Initialize the ActivityResultLauncher for Camera
        cameraResultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == RESULT_OK) {
                imageView.setImageURI(imageUri)
                imageView.visibility = View.VISIBLE
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
            var photoFile: File? = null
            try {
                photoFile = createImageFile()
            } catch (ex: IOException) {
                Log.e("AddSightingActivity", "Error occurred while creating the file", ex)
            }
            if (photoFile != null) {
                imageUri = FileProvider.getUriForFile(this, "com.mobdeve.fileprovider", photoFile)
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
                cameraResultLauncher.launch(cameraIntent)
            }
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

    // Function to create a file for the photo to be saved
    @Throws(IOException::class)
    private fun createImageFile(): File {
        val imageFileName = "JPEG_" + System.currentTimeMillis() + ".jpg"
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(imageFileName, ".jpg", storageDir)
    }
}
