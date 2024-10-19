package com.mobdeve.group3.mco

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView.ViewHolder

class ProfileSightingsViewHolder(itemView: View) : ViewHolder(itemView) {
    private val imgSighting: ImageView = itemView.findViewById(R.id.imgSighting)
    private val txtSightingName: TextView = itemView.findViewById(R.id.txtSightingName)
    private val txtSightingNameScientific: TextView =
        itemView.findViewById(R.id.txtSightingNameScientific)
    private val txtSighingPlace: TextView = itemView.findViewById(R.id.txtSightingPlace)
    private val txtSightingDate: TextView = itemView.findViewById(R.id.txtSightingDate)

    fun bindData(sighting: Sighting) {
        imgSighting.setImageResource(sighting.imageId)
        txtSightingName.text = sighting.name
        txtSightingNameScientific.text = sighting.scientificName
        txtSighingPlace.text = sighting.location
        txtSightingDate.text = "Seen on ${sighting.date}"
    }
}