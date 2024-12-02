package com.mobdeve.group3.mco.sighting

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.recyclerview.widget.RecyclerView
import com.mobdeve.group3.mco.MainActivity
import com.mobdeve.group3.mco.R
import com.mobdeve.group3.mco.comment.CommentsDialogFragment
import com.mobdeve.group3.mco.db.UsersAPI
import com.mobdeve.group3.mco.profile.ProfileActivity
import com.mobdeve.group3.mco.storage.ImagesAPI
import java.text.SimpleDateFormat
import java.util.Locale

class SightingPostViewHolder(
    itemView: View,
    private val editSightingActivityLauncher: ActivityResultLauncher<Intent>
) : RecyclerView.ViewHolder(itemView) {
    private val userHandle: TextView = itemView.findViewById(R.id.txtUsername)
    private val postingDate: TextView = itemView.findViewById(R.id.txtPostTime)
    private val userIcon: ImageView = itemView.findViewById(R.id.imgProfPic)
    private val score: TextView = itemView.findViewById(R.id.txtPostScore)
    private val txtSightingName: TextView = itemView.findViewById(R.id.txtSightingName)
    private val txtSightingNameScientific: TextView =
        itemView.findViewById(R.id.txtSightingNameScientific)
    private val txtSighingPlace: TextView = itemView.findViewById(R.id.txtSightingPlace)
    private val txtSightingDate: TextView = itemView.findViewById(R.id.txtSightingDate)
    private val imgSighting: ImageView? = itemView.findViewById(R.id.imgSightingPhoto)
    private val noPhotoText: TextView? =
        itemView.findViewById(R.id.no_photo_text)
    private val btnUpvote: ImageButton = itemView.findViewById(R.id.btnUpvote)
    private val btnDownvote: ImageButton = itemView.findViewById(R.id.btnDownvote)
    private val btnComment: ImageButton = itemView.findViewById(R.id.btnComment)
    private val btnModify: ImageButton = itemView.findViewById(R.id.btnModify)
    private val txtSeaMoreDetails: TextView = itemView.findViewById(R.id.txtSeaMoreDetails)

    fun bind(sighting: Sighting) {
        userHandle.text = sighting.userHandler
        score.text = sighting.score.toString()
        txtSightingName.text = sighting.animalName
        txtSightingNameScientific.text = sighting.scientificName
        txtSighingPlace.text = sighting.location

        val originalSightDate = sighting.sightDate
        val originalPostDate = sighting.postingDate
        val inputFormat = SimpleDateFormat("EEE MMM dd HH:mm:ss 'GMT'Z yyyy", Locale.ENGLISH)
        val outputFormat = SimpleDateFormat("EEE MMM dd yyyy HH:mm:ss", Locale.ENGLISH)

        UsersAPI.getInstance().getUserWithUsername(sighting.userHandler) { user ->
            val userId = user["id"] as String
            ImagesAPI.getInstance().getProfileImage(userId) { imgBytes ->
                if (imgBytes != null) {
                    val imgBitmap = BitmapFactory.decodeByteArray(imgBytes, 0, imgBytes.size)
                    userIcon.setImageBitmap(imgBitmap)
                } else {
                    userIcon.setImageResource(R.drawable.profpic)
                }
            }
        }

        try {
            val sightDate = inputFormat.parse(originalSightDate)
            val formattedSightDate = outputFormat.format(sightDate)
            val postDate = inputFormat.parse(originalPostDate)
            val formattedPostDate = outputFormat.format(postDate)
            postingDate.text = "Posted on ${formattedPostDate}"
            txtSightingDate.text = "Seen on $formattedSightDate"
        } catch (e: Exception) {
            e.printStackTrace()
            postingDate.text = originalPostDate
            txtSightingDate.text = originalSightDate
        }

        val imageUri = sighting.imageUri
        if (imageUri != null) {
            imgSighting?.setImageURI(imageUri)
            imgSighting?.visibility = View.VISIBLE
            noPhotoText?.visibility = View.GONE // Hide "No photo" text
        } else {
            imgSighting?.visibility = View.GONE
            noPhotoText?.visibility = View.VISIBLE // Show "No photo" text
        }

        // Show or hide the modify button based on ownership
        if (sighting.isOwnedByCurrentUser) {
            btnModify.visibility = View.VISIBLE

            // Adjust txtSightingPlace constraint to btnModify
            val constraintLayout = itemView as ConstraintLayout
            val constraintSet = ConstraintSet()
            constraintSet.clone(constraintLayout)
            constraintSet.connect(
                R.id.txtSightingPlace,
                ConstraintSet.TOP,
                R.id.btnModify,
                ConstraintSet.BOTTOM
            )
            constraintSet.applyTo(constraintLayout)
        } else {
            btnModify.visibility = View.GONE

            // Adjust txtSightingPlace constraint to btnComment
            val constraintLayout = itemView as ConstraintLayout
            val constraintSet = ConstraintSet()
            constraintSet.clone(constraintLayout)
            constraintSet.connect(
                R.id.txtSightingPlace,
                ConstraintSet.TOP,
                R.id.btnComment,
                ConstraintSet.BOTTOM
            )
            constraintSet.applyTo(constraintLayout)
        }

        // Set button states based on the post's current voting state
        btnUpvote.setImageResource(
            if (sighting.hasUpvoted) R.drawable.ic_check_filled else R.drawable.ic_regular_check
        )
        btnDownvote.setImageResource(
            if (sighting.hasDownvoted) R.drawable.ic_report_filled else R.drawable.ic_regular_report
        )

        // Handle button clicks
        btnUpvote.setOnClickListener {
            if (sighting.hasDownvoted) {
                sighting.score += 2 // Increment score by 2
                sighting.hasDownvoted = false
                btnDownvote.setImageResource(R.drawable.ic_regular_report) // Reset downvote button
            } else if (!sighting.hasUpvoted) {
                sighting.score++
            } else {
                sighting.score--
            }
            sighting.hasUpvoted = !sighting.hasUpvoted // Toggle upvoted state
            btnUpvote.setImageResource(
                if (sighting.hasUpvoted) R.drawable.ic_check_filled else R.drawable.ic_regular_check
            ) // Update button image
            score.text = sighting.score.toString() // Update score text
        }

        btnDownvote.setOnClickListener {
            if (sighting.hasUpvoted) {
                sighting.score -= 2 // Decrement score by 2
                sighting.hasUpvoted = false
                btnUpvote.setImageResource(R.drawable.ic_regular_check) // Reset upvote button
            } else if (!sighting.hasDownvoted) {
                sighting.score--
            } else {
                sighting.score++
            }
            sighting.hasDownvoted = !sighting.hasDownvoted // Toggle downvoted state
            btnDownvote.setImageResource(
                if (sighting.hasDownvoted) R.drawable.ic_report_filled else R.drawable.ic_regular_report
            ) // Update button image
            score.text = sighting.score.toString() // Update score text
        }

        // Set OnClickListener for the user icon to redirect to ProfileActivity
        userIcon.setOnClickListener {
            val context = itemView.context
            val profileIntent = Intent(context, ProfileActivity::class.java)
            context.startActivity(profileIntent)
        }

        btnComment.setOnClickListener {
            val dialog = CommentsDialogFragment.newInstance(sighting)
            dialog.show(
                (itemView.context as AppCompatActivity).supportFragmentManager,
                "CommentsDialog"
            )
        }

        txtSeaMoreDetails.setOnClickListener {
            val dialogFragment = SeaMoreDialogFragment()

            // Pass data to the dialog using arguments (Bundle)
            val args = Bundle().apply {
                putString("species", sighting.scientificName)
                putString("commonName", sighting.animalName)
                putString("groupSize", sighting.groupSize.toString())
                putString("location", sighting.location)
                putString("distance", sighting.distance.toString())
                putString("observerType", sighting.observerType)
                putString("sightingDate", sighting.sightDate)
                putString("sightingTime", sighting.sightingTime)
            }

            dialogFragment.arguments = args

            // Show the dialog
            val fragmentManager = (itemView.context as AppCompatActivity).supportFragmentManager
            dialogFragment.show(fragmentManager, "sea_more_dialog")
        }

        btnModify.setOnClickListener {
            val options = arrayOf("Edit", "Delete")
            val builder = AlertDialog.Builder(itemView.context)
            builder.setTitle("Modify Post")
                .setItems(options) { _, which ->
                    when (which) {
                        0 -> {
                            // Handle Edit
                            val context = itemView.context
                            val editIntent =
                                Intent(context, EditSightingActivity::class.java).apply {
                                    putExtra("COMMON_NAME_KEY", sighting.animalName)
                                    putExtra("SCIENTIFIC_NAME_KEY", sighting.scientificName)
                                    putExtra("GROUP_SIZE_KEY", sighting.groupSize)
                                    putExtra("DISTANCE_KEY", sighting.distance)
                                    putExtra("LOCATION_KEY", sighting.location)
                                    putExtra("OBSERVER_TYPE_KEY", sighting.observerType)
                                    putExtra("SIGHTING_DATE_KEY", sighting.sightDate)
                                    putExtra("SIGHTING_TIME_KEY", sighting.sightingTime)
                                    putExtra("IMAGE_URI_KEY", sighting.imageUri.toString())
                                    putExtra("POST_ID", sighting.id)
                                }

                            editSightingActivityLauncher.launch(editIntent)
                        }

                        1 -> {
                            // Handle Delete - show confirmation dialog or delete the post
                            val position = bindingAdapterPosition
                            if (position != RecyclerView.NO_POSITION) {
                                showDeleteConfirmationDialog(sighting, position)
                            }
                        }
                    }
                }
                .show()
        }
    }

    private fun showDeleteConfirmationDialog(sighting: Sighting, position: Int) {
        val builder = AlertDialog.Builder(itemView.context)
        builder.setTitle("Delete Post")
            .setMessage("Are you sure you want to delete this post?")
            .setPositiveButton("Yes") { _, _ ->
                deletePost(sighting, position)
            }
            .setNegativeButton("No", null)
            .show()
    }

    private fun deletePost(sighting: Sighting, position: Int) {
        val activity = itemView.context as MainActivity
        activity.deletePostAtPosition(position)
    }
}
