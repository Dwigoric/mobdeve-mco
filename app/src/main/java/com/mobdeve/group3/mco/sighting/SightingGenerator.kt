package com.mobdeve.group3.mco.sighting

import android.net.Uri
import com.mobdeve.group3.mco.R

class SightingGenerator {
    companion object {
        fun generateData(): ArrayList<Sighting> {
            val tempList = ArrayList<Sighting>()

            // Post 1: Thresher Shark with image
            tempList.add(
                Sighting(
                    userHandler = "ynapixel",
                    userIcon = R.drawable.profpic,
                    postingDate = "2021-08-01",
                    animalName = "Thresher Shark",
                    scientificName = "Alopias vulpinus",
                    location = "Moalboal, Cebu",
                    sightDate = "2021-07-01",
                    imageUri = Uri.parse("android.resource://com.mobdeve.group3.mco/drawable/thresher_shark"),
                    groupSize = 3,
                    distance = 20.5f,
                    observerType = "Researcher",
                    sightingTime = "14:30"
                )
            )

            // Post 2: Clownfish with image
            tempList.add(
                Sighting(
                    userHandler = "ynapixel",
                    userIcon = R.drawable.profpic,
                    postingDate = "2021-08-01",
                    animalName = "Clownfish",
                    scientificName = "Amphiprioninae",
                    location = "Anilao, Batangas",
                    sightDate = "2021-07-02",
                    imageUri = Uri.parse("android.resource://com.mobdeve.group3.mco/drawable/nemo"),
                    groupSize = 1,
                    distance = 5.0f,
                    observerType = "Amateur",
                    sightingTime = "09:00"
                )
            )

            // Post 3: Clownfish with image (different user)
            tempList.add(
                Sighting(
                    userHandler = "mrtnrjn",
                    userIcon = R.drawable.lelouch,
                    postingDate = "2021-03-01",
                    animalName = "Clownfish",
                    scientificName = "Amphiprioninae",
                    location = "Anilao, Batangas",
                    sightDate = "2021-02-02",
                    imageUri = Uri.parse("android.resource://com.mobdeve.group3.mco/drawable/nemo"),
                    groupSize = 2,
                    distance = 10.0f,
                    observerType = "Diver",
                    sightingTime = "13:45"
                )
            )

            // Post 4: Clownfish with image (same as Post 3)
            tempList.add(
                Sighting(
                    userHandler = "mrtnrjn",
                    userIcon = R.drawable.lelouch,
                    postingDate = "2021-03-01",
                    animalName = "Clownfish",
                    scientificName = "Amphiprioninae",
                    location = "Anilao, Batangas",
                    sightDate = "2021-02-02",
                    imageUri = Uri.parse("android.resource://com.mobdeve.group3.mco/drawable/nemo"),
                    groupSize = 2,
                    distance = 8.0f,
                    observerType = "Diver",
                    sightingTime = "14:00"
                )
            )

            // Post 5: Mysterious creature without image
            tempList.add(
                Sighting(
                    userHandler = "noimage_user",
                    userIcon = R.drawable.profpic,
                    postingDate = "2024-10-19",
                    animalName = "Mysterious Creature",
                    scientificName = "Unknown",
                    location = "Deep Sea",
                    sightDate = "2024-10-18",
                    imageUri = null,
                    groupSize = 0,
                    distance = 0.0f,
                    observerType = "Unknown",
                    sightingTime = "Unknown"
                )
            )

            // Post 6: Sea Turtle with image
            tempList.add(
                Sighting(
                    userHandler = "seaturtle_lover",
                    userIcon = R.drawable.profpic,
                    postingDate = "2023-09-12",
                    animalName = "Sea Turtle",
                    scientificName = "Chelonioidea",
                    location = "Tubbataha Reefs",
                    sightDate = "2023-09-10",
                    imageUri = Uri.parse("android.resource://com.mobdeve.group3.mco/drawable/sea_turtle"),
                    groupSize = 5,
                    distance = 30.0f,
                    observerType = "Diver",
                    sightingTime = "11:15"
                )
            )

            // Post 7: Dugong without image
            tempList.add(
                Sighting(
                    userHandler = "wildlifeenthusiast",
                    userIcon = R.drawable.profpic,
                    postingDate = "2022-12-01",
                    animalName = "Dugong",
                    scientificName = "Dugong dugon",
                    location = "Palawan, Philippines",
                    sightDate = "2022-11-29",
                    imageUri = null,
                    groupSize = 1,
                    distance = 50.0f,
                    observerType = "Tourist",
                    sightingTime = "16:30"
                )
            )

            // Post 8: Whale Shark with image
            tempList.add(
                Sighting(
                    userHandler = "oceanadventurer",
                    userIcon = R.drawable.profpic,
                    postingDate = "2024-06-25",
                    animalName = "Whale Shark",
                    scientificName = "Rhincodon typus",
                    location = "Donsol, Sorsogon",
                    sightDate = "2024-06-23",
                    imageUri = Uri.parse("android.resource://com.mobdeve.group3.mco/drawable/whale_shark"),
                    groupSize = 10,
                    distance = 100.0f,
                    observerType = "Researcher",
                    sightingTime = "07:00"
                )
            )

            // Post 9: Hammerhead Shark with image
            tempList.add(
                Sighting(
                    userHandler = "sharklover",
                    userIcon = R.drawable.profpic,
                    postingDate = "2024-05-14",
                    animalName = "Hammerhead Shark",
                    scientificName = "Sphyrnidae",
                    location = "Tubbataha Reefs",
                    sightDate = "2024-05-12",
                    imageUri = Uri.parse("android.resource://com.mobdeve.group3.mco/drawable/hammerhead_shark"),
                    groupSize = 4,
                    distance = 20.0f,
                    observerType = "Researcher",
                    sightingTime = "12:00"
                )
            )

            // Post 10: Octopus without image
            tempList.add(
                Sighting(
                    userHandler = "marinebiologist",
                    userIcon = R.drawable.profpic,
                    postingDate = "2023-11-22",
                    animalName = "Octopus",
                    scientificName = "Octopoda",
                    location = "Puerto Galera, Philippines",
                    sightDate = "2023-11-21",
                    imageUri = null,
                    groupSize = 2,
                    distance = 15.0f,
                    observerType = "Scientist",
                    sightingTime = "10:30"
                )
            )

            return tempList
        }
    }
}
