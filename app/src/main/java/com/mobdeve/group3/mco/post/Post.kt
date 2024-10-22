package com.mobdeve.group3.mco.post

import java.util.UUID

class Post(
    userHandler: String,
    userIcon: Int,
    postingDate: String,
    animalName: String,
    scientificName: String,
    location: String,
    sightDate: String,
    imageId: Int?
) {
    val id = UUID.randomUUID().toString()

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

    var imageId = imageId
        private set

    var score: Int = 4
    var hasUpvoted: Boolean = false
    var hasDownvoted: Boolean = false
}