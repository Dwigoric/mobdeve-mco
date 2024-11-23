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

    fun addComment(sightingId: String, content: String): String {
        val data = dbHelper.constructData(
            "userId" to auth.currentUser!!.uid,
            "sightingId" to sightingId,
            "content" to content
        )

        return dbHelper.addDocument("comments", data)
    }

    fun getComments(sightingId: String): ArrayList<HashMap<String, Any>> {
        return dbHelper.getDocumentsWhere("comments", "sightingId", sightingId)
    }

    fun deleteComment(commentId: String): Boolean {
        return dbHelper.deleteDocument("comments", commentId)
    }
}