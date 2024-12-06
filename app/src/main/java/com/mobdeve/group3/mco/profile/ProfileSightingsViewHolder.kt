package com.mobdeve.group3.mco.profile

import android.graphics.BitmapFactory
import android.net.Uri
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.mobdeve.group3.mco.R
import com.mobdeve.group3.mco.sighting.Sighting
import com.mobdeve.group3.mco.storage.ImagesAPI

class ProfileSightingsViewHolder(itemView: View) : ViewHolder(itemView) {
    private val imgSighting: ImageView = itemView.findViewById(R.id.imgSighting)
    private val txtSightingName: TextView = itemView.findViewById(R.id.txtSightingName)
    private val txtSightingNameScientific: TextView =
        itemView.findViewById(R.id.txtSightingNameScientific)
    private val txtSighingPlace: TextView = itemView.findViewById(R.id.txtSightingPlace)

    fun bindData(sighting: Sighting) {
        setImage(sighting.imageId)
        txtSightingName.text = sighting.animalName
        txtSightingNameScientific.text = sighting.scientificName
        txtSighingPlace.text = sighting.location
    }

    private fun setImage(imageId: Uri?) {
        if (imageId != null) {
            ImagesAPI.getInstance().getSightingImage(imageId.toString()) { image ->
                imgSighting.setImageBitmap(BitmapFactory.decodeByteArray(image, 0, image.size))
            }
        }
    }
}