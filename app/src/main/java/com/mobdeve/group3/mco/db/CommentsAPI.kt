package com.mobdeve.group3.mco.db

import com.google.firebase.Firebase
import com.google.firebase.auth.auth

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
            "content" to content
        )

        dbHelper.addDocument("comments", data) { commentId ->
            callback(commentId)
        }
    }

    fun getComments(sightingId: String, callback: (ArrayList<HashMap<String, Any>>) -> Unit) {
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