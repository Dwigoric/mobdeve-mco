package com.mobdeve.group3.mco.db

import com.google.firebase.Firebase
import com.google.firebase.auth.auth

class VotesAPI {
    private val dbHelper = DbHelper.getInstance()
    private val auth = Firebase.auth

    companion object {
        private var instance: VotesAPI? = null

        fun getInstance(): VotesAPI {
            if (instance == null) {
                instance = VotesAPI()
            }
            return instance as VotesAPI
        }
    }

    private constructor()

    fun setVote(sightingId: String, isUpvote: Boolean): String {
        val existingVote = dbHelper.getDocumentsWhereMultiple(
            "votes", hashMapOf(
                "userId" to auth.currentUser!!.uid,
                "sightingId" to sightingId
            )
        )

        if (existingVote.isNotEmpty()) {
            val vote = existingVote[0]
            vote["isUpvote"] = isUpvote
            dbHelper.updateDocument("votes", vote["id"] as String, vote)
            return vote["id"] as String
        } else {
            return dbHelper.addDocument(
                "votes",
                hashMapOf(
                    "userId" to auth.currentUser!!.uid,
                    "sightingId" to sightingId,
                    "isUpvote" to isUpvote
                )
            )
        }
    }

    fun countVotes(sightingId: String): Int {
        val votes = dbHelper.getDocumentsWhere("votes", "sightingId", sightingId)
        var count = 0

        for (vote in votes) {
            if (vote["isUpvote"] as Boolean) {
                count++
            } else {
                count--
            }
        }

        return count
    }

    fun removeVote(sightingId: String): Boolean {
        val votes = dbHelper.getDocumentsWhereMultiple(
            "votes", hashMapOf(
                "userId" to auth.currentUser!!.uid,
                "sightingId" to sightingId
            )
        )

        if (votes.isEmpty()) {
            return false
        }

        return dbHelper.deleteDocument("votes", votes[0]["id"] as String)
    }
}