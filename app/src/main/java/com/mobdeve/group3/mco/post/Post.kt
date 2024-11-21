package com.mobdeve.group3.mco.post

import android.net.Uri
import com.mobdeve.group3.mco.comment.Comment
import java.util.UUID

class Post(
    userHandler: String,
    userIcon: Int,
    postingDate: String,
    animalName: String,
    scientificName: String,
    location: String,
    sightDate: String,
    imageId: Uri?
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

    // List of comments associated with this post
    private val _comments: MutableList<Comment> = mutableListOf()
    val comments: List<Comment>
        get() = _comments // Expose as an immutable list

    fun addComment(comment: Comment) {
        if (comment.postId == this.id) {
            _comments.add(comment)
        }
    }

    fun removeComment(comment: Comment) {
        _comments.remove(comment)
    }
}