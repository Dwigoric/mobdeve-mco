package com.mobdeve.group3.mco.catalogue

import com.mobdeve.group3.mco.R

class CatalogueGenerator {
    companion object {
        fun generateCatalogueSpecies(): ArrayList<CatalogueSpecies> {
            val species = ArrayList<CatalogueSpecies>()

            species.add(
                CatalogueSpecies(
                    "1",
                    "Pelagic Thresher Shark",
                    R.drawable.c_pelagic_thresher_shark
                )
            )
            species.add(CatalogueSpecies("2", "Tiger Shark", R.drawable.c_tiger_shark))
            species.add(
                CatalogueSpecies(
                    "3",
                    "Blacktip Reef Shark",
                    R.drawable.c_blacktip_reef_shark
                )
            )
            species.add(
                CatalogueSpecies(
                    "4",
                    "Whitetip Reef Shark",
                    R.drawable.c_whitetip_reef_shark
                )
            )
            species.add(CatalogueSpecies("5", "Dugong", R.drawable.c_dugong))
            species.add(CatalogueSpecies("6", "Whale Shark", R.drawable.c_whale_shark))
            species.add(CatalogueSpecies("7", "Spinner Dolphin", R.drawable.c_spinner_dolphin))
            species.add(
                CatalogueSpecies(
                    "8",
                    "Indo-Pacific Bottlenose Dolphin",
                    R.drawable.c_indo_pacific_bottlenose_dolphin
                )
            )
            species.add(CatalogueSpecies("9", "Giant Manta", R.drawable.c_giant_manta))
            species.add(CatalogueSpecies("10", "Green Sea Turtle", R.drawable.c_green_sea_turtle))
            species.add(
                CatalogueSpecies(
                    "11",
                    "Hawksbill Sea Turtle",
                    R.drawable.c_hawksbill_turtle
                )
            )
            species.add(
                CatalogueSpecies(
                    "12",
                    "Olive Ridley Turtle",
                    R.drawable.c_olive_ridley_turtle
                )
            )

            return species
        }
    }
}