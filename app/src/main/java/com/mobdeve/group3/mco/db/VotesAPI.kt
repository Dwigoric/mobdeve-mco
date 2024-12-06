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

    fun getVote(sightingId: String, callback: (Int) -> Unit) {
        dbHelper.getDocumentsWhereMultiple(
            "votes", hashMapOf(
                "userId" to auth.currentUser!!.uid,
                "sightingId" to sightingId
            )
        ) {
            if (it.isNotEmpty()) {
                callback(if (it[0]["isUpvote"] as Boolean) 1 else -1)
            } else {
                callback(0)
            }
        }
    }

    fun setVote(sightingId: String, isUpvote: Boolean, callback: (String) -> Unit) {
        dbHelper.getDocumentsWhereMultiple(
            "votes", hashMapOf(
                "userId" to auth.currentUser!!.uid,
                "sightingId" to sightingId
            )
        ) { existingVote ->
            if (existingVote.isNotEmpty()) {
                val vote = existingVote[0]
                vote["isUpvote"] = isUpvote
                val voteId = vote["id"] as String
                vote.remove("id")
                dbHelper.updateDocument("votes", voteId, vote) { success ->
                    if (success) {
                        callback(voteId)
                    } else {
                        callback("")
                    }
                }
            } else {
                dbHelper.addDocument(
                    "votes",
                    hashMapOf(
                        "userId" to auth.currentUser!!.uid,
                        "sightingId" to sightingId,
                        "isUpvote" to isUpvote
                    )
                ) { voteId ->
                    callback(voteId)
                }
            }
        }
    }

    fun countVotes(sightingId: String, callback: (Int) -> Unit) {
        dbHelper.getDocumentsWhere("votes", "sightingId", sightingId) { votes ->
            var count = 0

            for (vote in votes) {
                if (vote["isUpvote"] as Boolean) {
                    count++
                } else {
                    count--
                }
            }

            callback(count)
        }
    }

    fun removeVote(sightingId: String, callback: (Boolean) -> Unit) {
        dbHelper.getDocumentsWhereMultiple(
            "votes", hashMapOf(
                "userId" to auth.currentUser!!.uid,
                "sightingId" to sightingId
            )
        ) { votes ->
            if (votes.isEmpty()) {
                callback(false)
            }

            dbHelper.deleteDocument("votes", votes[0]["id"] as String) { success ->
                callback(success)
            }
        }
    }

    fun removeVotesFromSighting(sightingId: String, callback: (Boolean) -> Unit) {
        dbHelper.deleteDocumentsWhere("votes", "sightingId", sightingId) { success ->
            callback(success)
        }
    }
}