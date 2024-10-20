package com.mobdeve.group3.mco

class SightingGenerator {
    companion object {
        fun generateData(): ArrayList<Sighting> {
            val tempList = ArrayList<Sighting>()
            tempList.add(
                Sighting(
                    "1",
                    "Thresher Shark",
                    "Alopias vulpinus",
                    "Moalboal, Cebu",
                    "2021-07-01",
                    "A thresher shark was spotted swimming near the shore.",
                    R.drawable.thresher_shark
                )
            )
            tempList.add(
                Sighting(
                    "2",
                    "Clownfish",
                    "Amphiprioninae",
                    "Anilao, Batangas",
                    "2021-07-02",
                    "A clownfish was seen swimming in the coral reefs.",
                    R.drawable.nemo
                )
            )

            return tempList
        }
    }
}