package com.mobdeve.group3.mco

import android.content.Intent
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val userHandle: TextView = itemView.findViewById(R.id.txtUsername)
    private val postingDate: TextView = itemView.findViewById(R.id.txtPostTime)
    private val userIcon: ImageView = itemView.findViewById(R.id.imgProfPic)
    private val score: TextView = itemView.findViewById(R.id.txtPostScore)
    private val txtSightingName: TextView = itemView.findViewById(R.id.txtSightingName)
    private val txtSightingNameScientific: TextView = itemView.findViewById(R.id.txtSightingNameScientific)
    private val txtSighingPlace: TextView = itemView.findViewById(R.id.txtSightingPlace)
    private val txtSightingDate: TextView = itemView.findViewById(R.id.txtSightingDate)
    private val description: TextView = itemView.findViewById(R.id.txtSeaMoreDetails)
    private val imgSighting: ImageView? = itemView.findViewById(R.id.imgSightingPhoto)
    private val noPhotoText: TextView? = itemView.findViewById(R.id.no_photo_text) // Ensure this exists in the item_post_nophoto layout
    private val btnUpvote: ImageButton = itemView.findViewById(R.id.btnUpvote)
    private val btnDownvote: ImageButton = itemView.findViewById(R.id.btnDownvote)

    fun bind(post: Post) {
        userHandle.text = post.userHandler
        userIcon.setImageResource(post.userIcon)
        score.text = post.score.toString()
        postingDate.text = "Posted on ${post.postingDate}"
        txtSightingName.text = post.animalName
        txtSightingNameScientific.text = post.scientificName
        txtSighingPlace.text = post.location
        txtSightingDate.text = "Seen on ${post.sightDate}"
        description.text = post.description

        // Store the imageId in a local variable
        val imageId = post.imageId

        if (imageId != null) {
            imgSighting?.setImageResource(imageId)
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
                post.score--;
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
                post.score--;
            } else {
                post.score++;
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
    }
}
