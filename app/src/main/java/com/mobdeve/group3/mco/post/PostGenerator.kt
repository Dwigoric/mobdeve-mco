package com.mobdeve.group3.mco.post

import android.net.Uri
import com.mobdeve.group3.mco.R

class PostGenerator {
    companion object {
        fun generateData(): ArrayList<Post> {
            val tempList = ArrayList<Post>()

            // Post 1: Thresher Shark with image
            tempList.add(
                Post(
                    "ynapixel",
                    R.drawable.profpic,
                    "2021-08-01",
                    "Thresher Shark",
                    "Alopias vulpinus",
                    "Moalboal, Cebu",
                    "2021-07-01",
                    Uri.parse("android.resource://com.mobdeve.group3.mco/drawable/thresher_shark")
                )
            )

            // Post 2: Clownfish with image
            tempList.add(
                Post(
                    "ynapixel",
                    R.drawable.profpic,
                    "2021-08-01",
                    "Clownfish",
                    "Amphiprioninae",
                    "Anilao, Batangas",
                    "2021-07-02",
                    Uri.parse("android.resource://com.mobdeve.group3.mco/drawable/nemo")
                )
            )

            // Post 3: Clownfish with image (different user)
            tempList.add(
                Post(
                    "mrtnrjn",
                    R.drawable.lelouch,
                    "2021-03-01",
                    "Clownfish",
                    "Amphiprioninae",
                    "Anilao, Batangas",
                    "2021-02-02",
                    Uri.parse("android.resource://com.mobdeve.group3.mco/drawable/nemo")
                )
            )

            // Post 4: Clownfish with image (same as Post 3)
            tempList.add(
                Post(
                    "mrtnrjn",
                    R.drawable.lelouch,
                    "2021-03-01",
                    "Clownfish",
                    "Amphiprioninae",
                    "Anilao, Batangas",
                    "2021-02-02",
                    Uri.parse("android.resource://com.mobdeve.group3.mco/drawable/nemo")
                )
            )

            // Post 5: Mysterious creature without image
            tempList.add(
                Post(
                    "noimage_user",
                    R.drawable.profpic,
                    "2024-10-19",
                    "Mysterious Creature",
                    "Unknown",
                    "Deep Sea",
                    "2024-10-18",
                    null
                )
            )

            // Post 6: Sea Turtle with image
            tempList.add(
                Post(
                    "seaturtle_lover",
                    R.drawable.profpic,
                    "2023-09-12",
                    "Sea Turtle",
                    "Chelonioidea",
                    "Tubbataha Reefs",
                    "2023-09-10",
                    Uri.parse("android.resource://com.mobdeve.group3.mco/drawable/sea_turtle")
                )
            )

            // Post 7: Dugong without image
            tempList.add(
                Post(
                    "wildlifeenthusiast",
                    R.drawable.profpic,
                    "2022-12-01",
                    "Dugong",
                    "Dugong dugon",
                    "Palawan, Philippines",
                    "2022-11-29",
                    null // No image
                )
            )

            // Post 8: Whale Shark with image
            tempList.add(
                Post(
                    "oceanadventurer",
                    R.drawable.profpic,
                    "2024-06-25",
                    "Whale Shark",
                    "Rhincodon typus",
                    "Donsol, Sorsogon",
                    "2024-06-23",
                    Uri.parse("android.resource://com.mobdeve.group3.mco/drawable/whale_shark")
                )
            )

            // Post 9: Hammerhead Shark with image
            tempList.add(
                Post(
                    "sharklover",
                    R.drawable.profpic,
                    "2024-05-14",
                    "Hammerhead Shark",
                    "Sphyrnidae",
                    "Tubbataha Reefs",
                    "2024-05-12",
                    Uri.parse("android.resource://com.mobdeve.group3.mco/drawable/hammerhead_shark")
                )
            )

            // Post 10: Octopus without image
            tempList.add(
                Post(
                    "marinebiologist",
                    R.drawable.profpic,
                    "2023-11-22",
                    "Octopus",
                    "Octopoda",
                    "Puerto Galera, Philippines",
                    "2023-11-21",
                    null // No image
                )
            )

            return tempList
        }
    }
}
