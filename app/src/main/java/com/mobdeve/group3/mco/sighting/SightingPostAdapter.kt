package com.mobdeve.group3.mco.sighting

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mobdeve.group3.mco.R

class SightingPostAdapter(private val data: ArrayList<Sighting>) :
    RecyclerView.Adapter<SightingPostViewHolder>() {
    override fun getItemViewType(position: Int): Int {
        return if (data[position].imageUri != null) {
            R.layout.item_post // Layout for posts with images
        } else {
            R.layout.item_post_nophoto // Layout for posts without images
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SightingPostViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(viewType, parent, false)
        return SightingPostViewHolder(view)
    }

    override fun onBindViewHolder(holder: SightingPostViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }

    fun updatePost(position: Int, updatedSighting: Sighting) {
        data[position] = updatedSighting
        notifyItemChanged(position)
    }
}