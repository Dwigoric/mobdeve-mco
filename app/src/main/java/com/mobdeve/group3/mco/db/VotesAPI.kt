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

    fun setVote(sightingId: String, isUpvote: Boolean, callback: (String) -> Unit) {
        val existingVote = dbHelper.getDocumentsWhereMultiple(
            "votes", hashMapOf(
                "userId" to auth.currentUser!!.uid,
                "sightingId" to sightingId
            )
        ) { existingVote ->
            if (existingVote.isNotEmpty()) {
                val vote = existingVote[0]
                vote["isUpvote"] = isUpvote
                dbHelper.updateDocument("votes", vote["id"] as String, vote) { success ->
                    if (success) {
                        callback(vote["id"] as String)
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
            ), { votes ->
                if (votes.isEmpty()) {
                    callback(false)
                }

                dbHelper.deleteDocument("votes", votes[0]["id"] as String) { success ->
                    callback(success)
                }
            }
        )
    }
}