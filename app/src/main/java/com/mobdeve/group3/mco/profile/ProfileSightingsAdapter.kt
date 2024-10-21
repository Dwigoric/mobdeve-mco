package com.mobdeve.group3.mco.profile

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.mobdeve.group3.mco.R
import com.mobdeve.group3.mco.Sighting

class ProfileSightingsAdapter(private val data: ArrayList<Sighting>) :
    Adapter<ProfileSightingsViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileSightingsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_my_sightings, parent, false)
        return ProfileSightingsViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProfileSightingsViewHolder, position: Int) {
        holder.bindData(data.get(position))
    }

    override fun getItemCount(): Int {
        return data.size
    }
}