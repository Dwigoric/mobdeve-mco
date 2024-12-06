package com.mobdeve.group3.mco.comment

import android.graphics.BitmapFactory
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.mobdeve.group3.mco.R
import com.mobdeve.group3.mco.db.UsersAPI
import com.mobdeve.group3.mco.storage.ImagesAPI

class CommentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val imgCommentUsername: ImageView = itemView.findViewById(R.id.imgCommentUsername)
    private val txtCommentUsername: TextView = itemView.findViewById(R.id.txtCommentUsername)
    private val txtCommentTime: TextView = itemView.findViewById(R.id.txtCommentTime)
    private val txtCommentContent: TextView = itemView.findViewById(R.id.txtCommentContent)

    fun bind(comment: Comment, onDeleteClick: (Comment) -> Unit) {
        val userId = comment.userHandler

        UsersAPI.getInstance().getUser(userId) { userData ->
            val username = userData["username"] as? String ?: "Unknown User"
            txtCommentUsername.text = username

            ImagesAPI.getInstance().getProfileImage(userId) { imgBytes ->
                if (imgBytes != null) {
                    val imgBitmap = BitmapFactory.decodeByteArray(imgBytes, 0, imgBytes.size)
                    imgCommentUsername.setImageBitmap(imgBitmap)
                } else {
                    imgCommentUsername.setImageResource(R.drawable.profpic) // Fallback image
                }
            }
        }

        txtCommentTime.text = comment.commentTime
        txtCommentContent.text = comment.content

        // Set long press listener to delete comment
        itemView.setOnLongClickListener {
            onDeleteClick(comment)  // Trigger the delete action
            true
        }
    }
}

