package com.mobdeve.group3.mco.catalogue

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mobdeve.group3.mco.R

class CatalogueViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val imgCatalogue: ImageView = itemView.findViewById(R.id.imgCategory)
    private val txtCategoryName: TextView = itemView.findViewById(R.id.txtCategoryName)

    fun bindData(catalogueSpecies: CatalogueSpecies) {
        imgCatalogue.setImageResource(catalogueSpecies.imageId)
        txtCategoryName.text = catalogueSpecies.name
    }
}