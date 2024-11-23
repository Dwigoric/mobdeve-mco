package com.mobdeve.group3.mco.post

import android.net.Uri
import com.mobdeve.group3.mco.comment.Comment
import java.util.UUID

data class Post(
    val id: String = UUID.randomUUID().toString(),
    val userHandler: String,
    val userIcon: Int,
    val postingDate: String,
    val animalName: String,
    val scientificName: String,
    val location: String,
    val sightDate: String,
    val imageId: Uri?,
    val groupSize: Int,
    val distance: Float,
    val observerType: String,
    val sightingTime: String,
    var score: Int = 0,
    var hasUpvoted: Boolean = false,
    var hasDownvoted: Boolean = false,
    private val _comments: MutableList<Comment> = mutableListOf()
) {
    val comments: List<Comment> get() = _comments

    fun addComment(comment: Comment) {
        if (comment.postId == this.id) {
            _comments.add(comment)
        }
    }

    fun removeComment(comment: Comment) {
        _comments.remove(comment)
    }
}
