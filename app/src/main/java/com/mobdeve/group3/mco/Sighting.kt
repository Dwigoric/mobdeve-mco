package com.mobdeve.group3.mco

class Sighting(
    id: String,
    name: String,
    scientificName: String,
    location: String,
    date: String,
    description: String,
    imageId: Int
) {
    var id = id
        private set

    var name = name
        private set

    var scientificName = scientificName
        private set

    var location = location
        private set

    var date = date
        private set

    var description = description
        private set

    var imageId = imageId
        private set
}