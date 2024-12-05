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
                    "Alopias pelagicus",
                    R.drawable.c_pelagic_thresher_shark
                )
            )
            species.add(
                CatalogueSpecies(
                    "2",
                    "Tiger Shark",
                    "Galocerdo cuvier",
                    R.drawable.c_tiger_shark
                )
            )
            species.add(
                CatalogueSpecies(
                    "3",
                    "Blacktip Reef Shark",
                    "Carcharhinus melanopterus",
                    R.drawable.c_blacktip_reef_shark
                )
            )
            species.add(
                CatalogueSpecies(
                    "4",
                    "Whitetip Reef Shark",
                    "Triaenodon obesus",
                    R.drawable.c_whitetip_reef_shark
                )
            )
            species.add(
                CatalogueSpecies(
                    "5",
                    "Dugong",
                    "Dugong dugon",
                    R.drawable.c_dugong
                )
            )
            species.add(
                CatalogueSpecies(
                    "6",
                    "Whale Shark",
                    "Rhincodon typus",
                    R.drawable.c_whale_shark
                )
            )
            species.add(
                CatalogueSpecies(
                    "7",
                    "Spinner Dolphin",
                    "Stenella longirostris",
                    R.drawable.c_spinner_dolphin
                )
            )
            species.add(
                CatalogueSpecies(
                    "8",
                    "Indo-Pacific Bottlenose Dolphin",
                    "Tursiops aduncus",
                    R.drawable.c_indo_pacific_bottlenose_dolphin
                )
            )
            species.add(
                CatalogueSpecies(
                    "9",
                    "Giant Manta",
                    "Mobula birostris",
                    R.drawable.c_giant_manta
                )
            )
            species.add(
                CatalogueSpecies(
                    "10",
                    "Green Sea Turtle",
                    "Chelonia mydas",
                    R.drawable.c_green_sea_turtle
                )
            )
            species.add(
                CatalogueSpecies(
                    "11",
                    "Hawksbill Sea Turtle",
                    "Eretmochelys imbricata",
                    R.drawable.c_hawksbill_turtle
                )
            )
            species.add(
                CatalogueSpecies(
                    "12",
                    "Olive Ridley Turtle",
                    "Lepidochelys olivacea",
                    R.drawable.c_olive_ridley_turtle
                )
            )

            return species
        }
    }
}