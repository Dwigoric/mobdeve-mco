package com.mobdeve.group3.mco.catalogue

import android.content.Intent
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
        txtCategoryName.text = catalogueSpecies.commonName

        itemView.setOnClickListener {
            val speciesIntent = Intent(itemView.context, SpeciesActivity::class.java)
            speciesIntent.putExtra(
                SpeciesActivity.KEY_SPECIES_COMMON_NAME,
                catalogueSpecies.commonName
            )
            speciesIntent.putExtra(
                SpeciesActivity.KEY_SPECIES_SCIENTIFIC_NAME,
                catalogueSpecies.scientificName
            )
            speciesIntent.putExtra(SpeciesActivity.KEY_SPECIES_IMAGE, catalogueSpecies.imageId)
            speciesIntent.putExtra(
                SpeciesActivity.KEY_PHYSICAL_CHARACTERISTICS,
                catalogueSpecies.physicalCharacteristics
            )
            speciesIntent.putExtra(SpeciesActivity.KEY_HABITAT, catalogueSpecies.habitat)
            speciesIntent.putExtra(
                SpeciesActivity.KEY_ECOSYSTEM_ROLE,
                catalogueSpecies.ecosystemRole
            )

            itemView.context.startActivity(speciesIntent)
        }
    }
}