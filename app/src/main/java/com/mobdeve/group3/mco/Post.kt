package com.mobdeve.group3.mco

class Post(
    id: String,
    userHandler: String,
    userIcon: Int,
    postingDate: String,
    animalName: String,
    scientificName: String,
    location: String,
    sightDate: String,
    description: String,
    imageId: Int?
) {
    var id = id
        private set

    var postingDate = postingDate
        private set

    var userHandler = userHandler
        private set

    var userIcon = userIcon
        private set

    var animalName = animalName
        private set

    var scientificName = scientificName
        private set

    var location = location
        private set

    var sightDate = sightDate
        private set

    var description = description
        private set

    var imageId = imageId
        private set

    var score : Int = 4
    var hasUpvoted: Boolean = false
    var hasDownvoted: Boolean = false
}