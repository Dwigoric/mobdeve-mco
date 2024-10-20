package com.mobdeve.group3.mco.post

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mobdeve.group3.mco.R

class PostAdapter(private val data: ArrayList<Post>): RecyclerView.Adapter<PostViewHolder>() {
    override fun getItemViewType(position: Int): Int {
        return if (data[position].imageId != null) {
            R.layout.item_post // Layout for posts with images
        } else {
            R.layout.item_post_nophoto // Layout for posts without images
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(viewType, parent, false)
        return PostViewHolder(view)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.bind(data.get(position))
    }

    override fun getItemCount(): Int {
        return data.size
    }
}