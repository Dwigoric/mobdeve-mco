package com.mobdeve.group3.mco.catalogue

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.mobdeve.group3.mco.databinding.SpeciesInfoBinding

class SpeciesActivity : AppCompatActivity() {

    companion object {
        const val KEY_SPECIES_ID = "KEY_SPECIES_ID"
        const val KEY_SPECIES_COMMON_NAME = "KEY_SPECIES_COMMON_NAME"
        const val KEY_SPECIES_SCIENTIFIC_NAME = "KEY_SPECIES_SCIENTIFIC_NAME"
        const val KEY_SPECIES_IMAGE = "KEY_SPECIES_IMAGE"
        const val KEY_PHYSICAL_CHARACTERISTICS = "KEY_PHYSICAL_CHARACTERISTICS"
        const val KEY_HABITAT = "KEY_HABITAT"
        const val KEY_ECOSYSTEM_ROLE = "KEY_ECOSYSTEM_ROLE"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val viewBinding = SpeciesInfoBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        val commonName = intent.getStringExtra(KEY_SPECIES_COMMON_NAME)
        val scientificName = intent.getStringExtra(KEY_SPECIES_SCIENTIFIC_NAME)
        val speciesImage = intent.getIntExtra(KEY_SPECIES_IMAGE, 0)
        val physicalCharacteristics = intent.getStringExtra(KEY_PHYSICAL_CHARACTERISTICS)
        val habitat = intent.getStringExtra(KEY_HABITAT)
        val ecosystemRole = intent.getStringExtra(KEY_ECOSYSTEM_ROLE)

        viewBinding.txtCatalogueCommonName.text = commonName
        viewBinding.txtCatalogueScientificName.text = scientificName
        viewBinding.imgBanner.setImageResource(speciesImage)
        viewBinding.txtAppearance.text = physicalCharacteristics
        viewBinding.txtHabitat.text = habitat
        viewBinding.txtRole.text = ecosystemRole
    }
}