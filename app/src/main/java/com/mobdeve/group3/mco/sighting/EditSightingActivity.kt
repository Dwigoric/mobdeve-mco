package com.mobdeve.group3.mco.sighting

import android.Manifest
import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.mobdeve.group3.mco.R
import com.mobdeve.group3.mco.catalogue.CatalogueGenerator
import com.mobdeve.group3.mco.databinding.ActivityEditSightingBinding
import com.mobdeve.group3.mco.db.SightingsAPI
import com.mobdeve.group3.mco.db.UsersAPI
import com.mobdeve.group3.mco.storage.ImagesAPI
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class EditSightingActivity : AppCompatActivity() {
    private lateinit var viewBinding: ActivityEditSightingBinding
    private lateinit var auth: FirebaseAuth
    private var imageUri: Uri? = null
    private var imageId: String? = null
    private var postId: String = ""

    private lateinit var cameraResultLauncher: ActivityResultLauncher<Intent>
    private lateinit var galleryResultLauncher: ActivityResultLauncher<Intent>
    private var sightingDate: String = ""
    private var sightingTime: String = ""
    private val REQUEST_CAMERA = 1
    private val categories = CatalogueGenerator.generateCatalogueSpecies()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityEditSightingBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        auth = Firebase.auth

        val intent = intent
        postId = intent.getStringExtra("POST_ID") ?: ""
        viewBinding.etCommonName.setText(intent.getStringExtra(AddSightingActivity.COMMON_NAME_KEY))
        viewBinding.etSpecies.setText(intent.getStringExtra(AddSightingActivity.SCIENTIFIC_NAME_KEY))

        // Extract common names for dropdown
        val commonNames = categories.map { it.commonName }

        // Set up AutoCompleteTextView with adapter
        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, commonNames)
        viewBinding.etCommonName.setAdapter(adapter)

        // Show dropdown when the field is clicked
        viewBinding.etCommonName.setOnTouchListener { _, _ ->
            viewBinding.etCommonName.showDropDown()
            false
        }

        viewBinding.etCommonName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!s.isNullOrEmpty()) {
                    viewBinding.etCommonName.showDropDown()
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })


        // Set listener for item selection
        viewBinding.etCommonName.setOnItemClickListener { parent, _, position, _ ->
            val selectedCommonName = parent.getItemAtPosition(position) as String
            val selectedSpecies = categories.find { it.commonName == selectedCommonName }
            viewBinding.etSpecies.setText(
                selectedSpecies?.scientificName ?: ""
            ) // Auto-fill scientific name
        }


        viewBinding.etGroupType.setText(
            intent.getIntExtra(AddSightingActivity.GROUP_SIZE_KEY, 0).toString()
        )
        viewBinding.etDistance.setText(
            intent.getFloatExtra(AddSightingActivity.DISTANCE_KEY, 0.0f).toString()
        )
        viewBinding.etLocation.setText(intent.getStringExtra(AddSightingActivity.LOCATION_KEY))
        viewBinding.etObserver.setText(intent.getStringExtra(AddSightingActivity.OBSERVER_TYPE_KEY))
        val sightingDateString = intent.getStringExtra(AddSightingActivity.SIGHTING_DATE_KEY)
        val formattedDate = if (!sightingDateString.isNullOrEmpty()) {
            try {
                val originalDate =
                    SimpleDateFormat("EEE MMM dd HH:mm:ss zzzz yyyy", Locale.getDefault()).parse(
                        sightingDateString
                    )
                SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(originalDate)
            } catch (e: ParseException) {
                sightingDateString
            }
        } else {
            ""
        }

        viewBinding.etSightingDate.setText(formattedDate)

        val sightingTimeString = intent.getStringExtra(AddSightingActivity.SIGHTING_TIME_KEY)
        val formattedTime = if (!sightingTimeString.isNullOrEmpty()) {
            try {
                val originalTime =
                    SimpleDateFormat("EEE MMM dd HH:mm:ss zzzz yyyy", Locale.getDefault()).parse(
                        sightingTimeString
                    )
                SimpleDateFormat("HH:mm", Locale.getDefault()).format(originalTime)
            } catch (e: ParseException) {
                sightingTimeString
            }
        } else {
            ""
        }

        viewBinding.etSightingTime.setText(formattedTime)

        val existingImage = intent.getStringExtra(AddSightingActivity.IMAGE_ID_KEY)
        if (!existingImage.isNullOrEmpty()) {
            ImagesAPI.getInstance().getSightingImage(existingImage) { byteArray ->
                val bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
                viewBinding.imgSelectedPhoto.setImageBitmap(bitmap)
                viewBinding.imgSelectedPhoto.visibility = View.VISIBLE
            }
        }

        // Set OnClickListener for date and time pickers here
        viewBinding.etSightingDate.setOnClickListener {
            showDatePickerDialog()
        }

        viewBinding.etSightingTime.setOnClickListener {
            showTimePickerDialog()
        }

        // Handle "Update" Button
        viewBinding.btnSavePost.setOnClickListener {
            // Disable the button to prevent multiple clicks
            viewBinding.btnSavePost.isEnabled = false
            // Indicate that the button is loading
            viewBinding.btnSavePost.text = getString(R.string.EditingSighting)

            updateSighting()
        }

        cameraResultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == RESULT_OK) {
                val imageBitmap = result.data?.getParcelableExtra<Bitmap>("data")

                if (imageBitmap != null) {
                    viewBinding.imgSelectedPhoto.setImageBitmap(imageBitmap)
                    viewBinding.imgSelectedPhoto.visibility = View.VISIBLE
                }
            }
        }

        galleryResultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == RESULT_OK) {
                imageUri = result.data?.data
                viewBinding.imgSelectedPhoto.setImageURI(imageUri)
                viewBinding.imgSelectedPhoto.visibility = View.VISIBLE
            }
        }

        // Handle photo options (similar to AddSightingActivity)
        viewBinding.btnAddPhoto.setOnClickListener {
            showPhotoOptionsMenu(it)
        }
    }

    private fun updateSighting() {
        Log.d("UpdateSighting", "imageUri: $imageUri")

        if (imageUri != null) {
            // Handling the case when an image is selected from URI (like gallery or camera)
            val imgBytes = contentResolver.openInputStream(imageUri!!)?.readBytes()
            if (imgBytes == null) {
                updateSightingWithImage()
            } else {
                ImagesAPI.getInstance().deleteSightingImage(
                    intent.getStringExtra(AddSightingActivity.IMAGE_ID_KEY)!!
                ) {}
                ImagesAPI.getInstance().putSightingImage(imgBytes) { imageId ->
                    this.imageId = imageId
                    updateSightingWithImage()
                }
            }
        } else if (viewBinding.imgSelectedPhoto.drawable != null && viewBinding.imgSelectedPhoto.visibility == View.VISIBLE) {
            Log.d(
                "UpdateSighting",
                "Drawable is not null: ${viewBinding.imgSelectedPhoto.drawable}"
            )

            // Ensure the drawable is a BitmapDrawable and contains a non-null bitmap
            val drawable = viewBinding.imgSelectedPhoto.drawable
            if (drawable is BitmapDrawable) {
                val bitmap = drawable.bitmap
                if (bitmap != null) {
                    ImagesAPI.getInstance()
                        .deleteSightingImage(intent.getStringExtra(AddSightingActivity.IMAGE_ID_KEY)!!) {}
                    ImagesAPI.getInstance().putSightingImage(
                        ImagesAPI.getByteArrayFromBitmap(bitmap)
                    ) { imageId ->
                        this.imageId = imageId
                        updateSightingWithImage()
                    }
                } else {
                    // Handle the case where the bitmap is null (if necessary, show an error or provide a fallback)
                    Log.d("UpdateSighting", "Bitmap is null, unable to proceed with image upload.")
                    updateSightingWithImage()
                }
            } else {
                // Handle the case where the drawable is not a BitmapDrawable
                Log.d(
                    "UpdateSighting",
                    "Drawable is not a BitmapDrawable, unable to proceed with image upload."
                )
                updateSightingWithImage()
            }
        } else {
            updateSightingWithImage()
        }
    }


    private fun updateSightingWithImage() {
        val commonName = viewBinding.etCommonName.text.toString()
        val scientificName = viewBinding.etSpecies.text.toString()
        val groupSize = viewBinding.etGroupType.text.toString().toIntOrNull() ?: 0
        val distance = viewBinding.etDistance.text.toString().toFloatOrNull() ?: 0.0f
        val location = viewBinding.etLocation.text.toString()
        val observerType = viewBinding.etObserver.text.toString()
        val sightingDate = viewBinding.etSightingDate.text.toString()
        val sightingTime = viewBinding.etSightingTime.text.toString()

        // Combine sighting date and time
        val combinedSightTime = SimpleDateFormat("MM/dd/yyyy hh:mm", Locale.getDefault()).parse(
            "$sightingDate $sightingTime"
        )?.let { Timestamp(it) }

        // Fetch user details (username and avatar URL)
        val userId = auth.currentUser?.uid ?: return

        UsersAPI.getInstance().getUser(userId) { userData ->
            val username = userData["username"] as? String ?: "Unknown User"
            val avatarUrl = userData["avatar"] as? String ?: ""

            // Fetch sighting details
            SightingsAPI.getInstance().getSighting(postId) { sightingData ->
                val postingDate = sightingData["postingDate"] as? Timestamp ?: Timestamp.now()

                // Prepare updated sighting data
                val updatedSightingData = hashMapOf<String, Any?>(
                    "commonName" to commonName,
                    "scientificName" to scientificName,
                    "groupSize" to groupSize,
                    "distance" to "${distance}km",
                    "location" to location,
                    "observerType" to observerType,
                    "sightTime" to (combinedSightTime ?: Timestamp.now()),
                    "imageId" to imageId,
                    "userHandler" to username,
                    "userIcon" to avatarUrl,
                    "postingDate" to postingDate
                )

                // Update the sighting using the API
                SightingsAPI.getInstance().updateSighting(postId, updatedSightingData) { success ->
                    if (success) {
                        val returnIntent = Intent().apply {
                            putExtra("SIGHTING_ID", postId)
                            putExtra("COMMON_NAME_KEY", commonName)
                            putExtra("SCIENTIFIC_NAME_KEY", scientificName)
                            putExtra("GROUP_SIZE_KEY", groupSize)
                            putExtra("DISTANCE_KEY", distance)
                            putExtra("LOCATION_KEY", location)
                            putExtra("OBSERVER_TYPE_KEY", observerType)
                            putExtra("SIGHTING_DATE_KEY", sightingDate)
                            putExtra("SIGHTING_TIME_KEY", sightingTime)
                            putExtra("IMAGE_ID_KEY", imageId)  // Make sure imageId is included
                            putExtra("userHandler", username)
                            putExtra("userIcon", avatarUrl)
                            putExtra("postingDate", postingDate)
                        }

                        setResult(RESULT_OK, returnIntent)
                        Toast.makeText(this, "Sighting updated successfully", Toast.LENGTH_SHORT)
                            .show()

                        // Only finish once everything is done
                        finish()
                    } else {
                        Toast.makeText(this, "Failed to update sighting", Toast.LENGTH_SHORT).show()
                    }
                }
            }
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

