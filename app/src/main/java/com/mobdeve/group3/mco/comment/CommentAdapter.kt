package com.mobdeve.group3.mco.comment

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.mobdeve.group3.mco.R

class CommentAdapter(private var comments: MutableList<Comment>) : Adapter<CommentViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_comment, parent, false)
        return CommentViewHolder(view)
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        holder.bind(comments[position])
    }

    override fun getItemCount(): Int {
        return comments.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateComments(newComments: List<Comment>) {
        comments.addAll(newComments)
        notifyDataSetChanged()
    }

}


