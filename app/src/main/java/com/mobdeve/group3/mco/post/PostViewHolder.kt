package com.mobdeve.group3.mco.post

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.mobdeve.group3.mco.MainActivity
import com.mobdeve.group3.mco.R
import com.mobdeve.group3.mco.comment.CommentsDialogFragment
import com.mobdeve.group3.mco.profile.ProfileActivity

class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    companion object {
        const val COMMON_NAME_KEY = "COMMON_NAME_KEY"
        const val SCIENTIFIC_NAME_KEY = "SCIENTIFIC_NAME_KEY"
        const val GROUP_SIZE_KEY = "GROUP_SIZE_KEY"
        const val DISTANCE_KEY = "DISTANCE_KEY"
        const val LOCATION_KEY = "LOCATION_KEY"
        const val OBSERVER_TYPE_KEY = "OBSERVER_TYPE_KEY"
        const val SIGHTING_DATE_KEY = "SIGHTING_DATE_KEY"
        const val SIGHTING_TIME_KEY = "SIGHTING_TIME_KEY"
        const val IMAGE_URI_KEY = "IMAGE_URI_KEY"
    }

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
        itemView.findViewById(R.id.no_photo_text) // Ensure this exists in the item_post_nophoto layout
    private val btnUpvote: ImageButton = itemView.findViewById(R.id.btnUpvote)
    private val btnDownvote: ImageButton = itemView.findViewById(R.id.btnDownvote)
    private val btnComment: ImageButton = itemView.findViewById(R.id.btnComment)
    private val btnModify: ImageButton = itemView.findViewById(R.id.btnModify)
    private val txtSeaMoreDetails: TextView = itemView.findViewById(R.id.txtSeaMoreDetails)

    fun bind(post: Post) {
        userHandle.text = post.userHandler
        userIcon.setImageResource(post.userIcon)
        score.text = post.score.toString()
        postingDate.text = "Posted on ${post.postingDate}"
        txtSightingName.text = post.animalName
        txtSightingNameScientific.text = post.scientificName
        txtSighingPlace.text = post.location
        txtSightingDate.text = "Seen on ${post.sightDate}"

        // Display image if available
        val imageUri = post.imageId
        if (imageUri != null) {
            imgSighting?.setImageURI(imageUri)
            imgSighting?.visibility = View.VISIBLE
            noPhotoText?.visibility = View.GONE // Hide "No photo" text
        } else {
            imgSighting?.visibility = View.GONE
            noPhotoText?.visibility = View.VISIBLE // Show "No photo" text
        }

        // Set button states based on the post's current voting state
        btnUpvote.setImageResource(
            if (post.hasUpvoted) R.drawable.ic_check_filled else R.drawable.ic_regular_check
        )
        btnDownvote.setImageResource(
            if (post.hasDownvoted) R.drawable.ic_report_filled else R.drawable.ic_regular_report
        )

        // Handle button clicks
        btnUpvote.setOnClickListener {
            if (post.hasDownvoted) {
                post.score += 2 // Increment score by 2
                post.hasDownvoted = false
                btnDownvote.setImageResource(R.drawable.ic_regular_report) // Reset downvote button
            } else if (!post.hasUpvoted) {
                post.score++
            } else {
                post.score--
            }
            post.hasUpvoted = !post.hasUpvoted // Toggle upvoted state
            btnUpvote.setImageResource(
                if (post.hasUpvoted) R.drawable.ic_check_filled else R.drawable.ic_regular_check
            ) // Update button image
            score.text = post.score.toString() // Update score text
        }

        btnDownvote.setOnClickListener {
            if (post.hasUpvoted) {
                post.score -= 2 // Decrement score by 2
                post.hasUpvoted = false
                btnUpvote.setImageResource(R.drawable.ic_regular_check) // Reset upvote button
            } else if (!post.hasDownvoted) {
                post.score--
            } else {
                post.score++
            }
            post.hasDownvoted = !post.hasDownvoted // Toggle downvoted state
            btnDownvote.setImageResource(
                if (post.hasDownvoted) R.drawable.ic_report_filled else R.drawable.ic_regular_report
            ) // Update button image
            score.text = post.score.toString() // Update score text
        }

        // Set OnClickListener for the user icon to redirect to ProfileActivity
        userIcon.setOnClickListener {
            val context = itemView.context
            val profileIntent = Intent(context, ProfileActivity::class.java)
            context.startActivity(profileIntent)
        }

        btnComment.setOnClickListener {
            val dialog = CommentsDialogFragment.newInstance(post)
            dialog.show((itemView.context as AppCompatActivity).supportFragmentManager, "CommentsDialog")
        }

        txtSeaMoreDetails.setOnClickListener {
            val dialogFragment = SeaMoreDialogFragment()

            // Pass data to the dialog using arguments (Bundle)
            val args = Bundle().apply {
                putString("species", post.scientificName)
                putString("commonName", post.animalName)
                putString("groupSize", post.groupSize.toString())
                putString("location", post.location)
                putString("distance", post.distance.toString())
                putString("observerType", post.observerType)
                putString("sightingDate", post.sightDate)
                putString("sightingTime", post.sightingTime)
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
                            val editIntent = Intent(context, EditSightingActivity::class.java)
                            editIntent.putExtra(COMMON_NAME_KEY, post.animalName)
                            editIntent.putExtra(SCIENTIFIC_NAME_KEY, post.scientificName)
                            editIntent.putExtra(GROUP_SIZE_KEY, post.groupSize)
                            editIntent.putExtra(DISTANCE_KEY, post.distance)
                            editIntent.putExtra(LOCATION_KEY, post.location)
                            editIntent.putExtra(OBSERVER_TYPE_KEY, post.observerType)
                            editIntent.putExtra(SIGHTING_DATE_KEY, post.sightDate)
                            editIntent.putExtra(SIGHTING_TIME_KEY, post.sightingTime)
                            editIntent.putExtra(IMAGE_URI_KEY, post.imageId.toString())
                            editIntent.putExtra("POST_ID", post.id)
                            context.startActivity(editIntent)
                        }
                        1 -> {
                            // Handle Delete - show confirmation dialog or delete the post
                            val position = bindingAdapterPosition
                            if (position != RecyclerView.NO_POSITION) {
                                showDeleteConfirmationDialog(post, position)
                            }
                        }
                    }
                }
                .show()
        }
    }

    private fun showDeleteConfirmationDialog(post: Post, position: Int) {
        val builder = AlertDialog.Builder(itemView.context)
        builder.setTitle("Delete Post")
            .setMessage("Are you sure you want to delete this post?")
            .setPositiveButton("Yes") { _, _ ->
                deletePost(post, position)
            }
            .setNegativeButton("No", null)
            .show()
    }

    private fun deletePost(post: Post, position: Int) {
        val activity = itemView.context as MainActivity
        activity.deletePostAtPosition(position)
    }
}
