package com.mobdeve.group3.mco.comment

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mobdeve.group3.mco.R

class CommentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val imgCommentUsername: ImageView = itemView.findViewById(R.id.imgCommentUsername)
    private val txtCommentUsername: TextView = itemView.findViewById(R.id.txtCommentUsername)
    private val txtCommentTime: TextView = itemView.findViewById(R.id.txtCommentTime)
    private val txtCommentContent: TextView = itemView.findViewById(R.id.txtCommentContent)

    fun bind(comment: Comment) {
        imgCommentUsername.setImageResource(comment.userIcon)
        txtCommentUsername.text = comment.userHandler
        txtCommentTime.text = comment.commentTime
        txtCommentContent.text = comment.content
    }
}
