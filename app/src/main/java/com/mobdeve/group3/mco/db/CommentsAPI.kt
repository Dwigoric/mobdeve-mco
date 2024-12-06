package com.mobdeve.group3.mco.db

import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.DocumentReference
import java.util.Date

class CommentsAPI {
    private val dbHelper = DbHelper.getInstance()
    private val auth = Firebase.auth

    companion object {
        private var instance: CommentsAPI? = null

        fun getInstance(): CommentsAPI {
            if (instance == null) {
                instance = CommentsAPI()
            }
            return instance as CommentsAPI
        }
    }

    private constructor()

    fun addComment(sightingId: String, content: String, callback: (String) -> Unit) {
        val data = dbHelper.constructData(
            "userId" to auth.currentUser!!.uid,
            "sightingId" to sightingId,
            "content" to content,
            "commentTime" to Date().toString()
        )

        dbHelper.addDocument("comments", data) { commentId ->
            val commentRef = dbHelper.getDocumentReference("comments", commentId)

            if (commentRef != null) {
                addCommentToSighting(sightingId, commentRef) { success ->
                    if (success) {
                        callback(commentId)
                    } else {
                        Log.e("Comments", "Failed to update sighting with new comment reference.")
                    }
                }
            } else {
                Log.e("Comments", "Failed to get DocumentReference for comment.")
            }
        }
    }

    fun addCommentToSighting(sightingId: String, commentRef: DocumentReference, callback: (Boolean) -> Unit) {
        dbHelper.getDocument("sightings", sightingId) { sightingData ->
            // Retrieve the existing comments array
            val comments = sightingData["comments"] as? ArrayList<DocumentReference> ?: ArrayList()

            comments.add(commentRef)

            dbHelper.updateDocument(
                "sightings", sightingId, hashMapOf("comments" to comments)
            ) { success ->
                callback(success)
            }
        }
    }



    fun getComments(sightingId: String, callback: (ArrayList<HashMap<String, Any?>>) -> Unit) {
        dbHelper.getDocumentsWhere("comments", "sightingId", sightingId) { comments ->
            callback(comments)
        }
    }

    fun deleteComment(commentId: String, callback: (Boolean) -> Unit) {
        dbHelper.deleteDocument("comments", commentId) { success ->
            callback(success)
        }
    }
}