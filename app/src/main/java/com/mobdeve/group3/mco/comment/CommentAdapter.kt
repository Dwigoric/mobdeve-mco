package com.mobdeve.group3.mco.comment

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.mobdeve.group3.mco.R

class CommentAdapter(
    var comments: MutableList<Comment>,
    private val onDeleteClick: (Comment) -> Unit  // Callback function
) :Adapter<CommentViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_comment, parent, false)
        return CommentViewHolder(view)
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        val comment = comments[position]
        holder.bind(comment, onDeleteClick)  // Pass the callback to the ViewHolder
    }

    override fun getItemCount(): Int = comments.size

    fun updateComments(newComments: List<Comment>) {
        comments.addAll(newComments)
        notifyDataSetChanged()
    }
}


