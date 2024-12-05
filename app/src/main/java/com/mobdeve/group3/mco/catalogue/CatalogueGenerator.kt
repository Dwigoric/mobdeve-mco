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
                    R.drawable.c_pelagic_thresher_shark,
                    "Notable for its long, whip-like tail that is as long as its body, streamlined body, metallic purple to bluish-gray color on top, and white underside. Grows up to 3 meters in length.",
                    "Open ocean species often found in tropical and subtropical waters. Frequent sightings around the Philippines' Monad Shoal, particularly in Malapascua Island.",
                    "Controls populations of small fish and squid, maintaining balance in pelagic ecosystems."
                )
            )
            species.add(
                CatalogueSpecies(
                    "2",
                    "Tiger Shark",
                    "Galocerdo cuvier",
                    R.drawable.c_tiger_shark,
                    "Stocky build with distinct vertical stripes on its body, large blunt nose, and serrated teeth. Can grow up to 5 meters in length.",
                    "Found in coastal and open waters; migrates across the Philippine seas.",
                    "Scavenger and apex predator that consumes a wide variety of prey, maintaining ecological balance."
                )
            )
            species.add(
                CatalogueSpecies(
                    "3",
                    "Blacktip Reef Shark",
                    "Carcharhinus melanopterus",
                    R.drawable.c_blacktip_reef_shark,
                    "Medium-sized shark with distinctive black tips on its dorsal, pectoral, and tail fins. Pale grayish-brown back with a white belly. Typically 1.6 meters in length.",
                    "Prefers shallow, coastal waters, lagoons, and coral reefs. Found throughout the Philippines, including Tubbataha Reef.",
                    "Apex predator that helps regulate the populations of reef fish, supporting coral reef health."
                )
            )
            species.add(
                CatalogueSpecies(
                    "4",
                    "Whitetip Reef Shark",
                    "Triaenodon obesus",
                    R.drawable.c_whitetip_reef_shark,
                    "Slender body with a broad head, distinctive white tips on its dorsal and tail fins, grayish-brown coloration, and small round eyes. Typically 1.6 meters long.",
                    "Lives near coral reefs, lagoons, and caves. Found in areas like Apo Reef and Tubbataha Reef in the Philippines.",
                    "A nocturnal predator that helps keep reef ecosystems balanced by preying on overabundant fish and invertebrates."
                )
            )
            species.add(
                CatalogueSpecies(
                    "5",
                    "Dugong",
                    "Dugong dugon",
                    R.drawable.c_dugong,
                    "Large, rotund body with paddle-like flippers and a dolphin-like tail. Grayish-brown in color, with sparse body hair. Grows up to 3 meters and weighs around 300-500 kg.",
                    "Found in shallow coastal areas with seagrass meadows, including Palawan, Bohol, and Sarangani Bay.",
                    "Grazes on seagrass, helping to maintain the health of seagrass meadows that serve as nurseries for other marine species."
                )
            )
            species.add(
                CatalogueSpecies(
                    "6",
                    "Whale Shark",
                    "Rhincodon typus",
                    R.drawable.c_whale_shark,
                    "The largest fish species, growing up to 12 meters long. Has a gray-blue body with white spots and a broad, flat head.",
                    "Migrates through warm, tropical oceans; commonly sighted in Donsol and Oslob, Cebu in the Philippines.",
                    "Filter-feeder that helps regulate plankton populations and contributes to nutrient cycling in marine ecosystems."
                )
            )
            species.add(
                CatalogueSpecies(
                    "7",
                    "Spinner Dolphin",
                    "Stenella longirostris",
                    R.drawable.c_spinner_dolphin,
                    "Small, sleek body with a long, thin snout. Gray coloration with a darker dorsal side and lighter belly. Known for spinning leaps out of the water.",
                    "Found in offshore tropical waters and sometimes near coastal areas in the Philippines. Frequently seen in Bohol Sea.",
                    "Predatory control on small schooling fish and squid, contributing to the balance of the marine food web."
                )
            )
            species.add(
                CatalogueSpecies(
                    "8",
                    "Indo-Pacific Bottlenose Dolphin",
                    "Tursiops aduncus",
                    R.drawable.c_indo_pacific_bottlenose_dolphin,
                    "Robust body, slightly shorter and thicker snout than other dolphins. Dark gray back and lighter belly, often with spotting in adults. Around 2.6 meters in length.",
                    "Found in shallow coastal areas, estuaries, and coral reefs. Observed in Palawan and the Visayan seas.",
                    "Controls fish populations, aiding in ecosystem stability."
                )
            )
            species.add(
                CatalogueSpecies(
                    "9",
                    "Giant Manta",
                    "Mobula birostris",
                    R.drawable.c_giant_manta,
                    "Enormous wingspan reaching up to 7 meters. Triangular pectoral fins, black dorsal side, and white belly with distinctive spot patterns.",
                    "Found in tropical and subtropical waters, including Tubbataha Reef and Bohol Sea. Prefers open water near coral reefs.",
                    "Filter-feeder that consumes plankton, contributing to nutrient cycling and the health of marine ecosystems."
                )
            )
            species.add(
                CatalogueSpecies(
                    "10",
                    "Green Sea Turtle",
                    "Chelonia mydas",
                    R.drawable.c_green_sea_turtle,
                    "Oval-shaped shell that is olive to dark brown with a creamy yellow underside. Grows up to 1.5 meters in length.",
                    "Found in coral reefs, seagrass beds, and mangroves across the Philippines, particularly in Tawi-Tawi and Apo Island.",
                    "Grazes on seagrass, preventing overgrowth and aiding in the ecosystem's health."
                )
            )
            species.add(
                CatalogueSpecies(
                    "11",
                    "Hawksbill Sea Turtle",
                    "Eretmochelys imbricata",
                    R.drawable.c_hawksbill_turtle,
                    "Small, narrow head with a pointed beak and a beautifully patterned shell resembling a tortoiseshell. Typically grows up to 1 meter in length.",
                    "Lives in coral reefs, rocky coasts, and lagoons in the Philippines, including Palawan and Tubbataha Reef.",
                    "Consumes sponges, helping to maintain coral reef health by preventing sponge overgrowth."
                )
            )
            species.add(
                CatalogueSpecies(
                    "12",
                    "Olive Ridley Turtle",
                    "Lepidochelys olivacea",
                    R.drawable.c_olive_ridley_turtle,
                    "Small, olive-green shell with a nearly circular shape. Usually 0.6–0.7 meters long.",
                    "Found in warm coastal waters and nesting beaches across the Philippines, including the shores of Bataan and Palawan.",
                    "Helps control jellyfish populations and contributes to nutrient cycling through nesting activities."
                )
            )

            return species
        }
    }
}