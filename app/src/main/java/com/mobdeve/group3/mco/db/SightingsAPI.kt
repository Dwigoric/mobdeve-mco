package com.mobdeve.group3.mco.db

import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

class SightingsAPI {
    private val dbHelper = DbHelper.getInstance()
    private val auth = Firebase.auth

    companion object {
        private var instance: SightingsAPI? = null

        fun getInstance(): SightingsAPI {
            if (instance == null) {
                instance = SightingsAPI()
            }
            return instance as SightingsAPI
        }
    }

    private constructor()

    fun addSighting(data: HashMap<String, Any?>, callback: (String) -> Unit) {
        dbHelper.addDocument("sightings", data) { sightingId ->
            callback(sightingId)
        }
    }

    fun getSighting(sightingId: String, callback: (HashMap<String, Any?>) -> Unit) {
        dbHelper.getDocument("sightings", sightingId) { sightingData ->
            callback(sightingData)
        }
    }

    fun getSightings(callback: (ArrayList<HashMap<String, Any?>>) -> Unit) {
        val documents = ArrayList<HashMap<String, Any?>>()
        dbHelper.getDocuments("sightings") { fetchedDocs ->
            Log.d(
                "getSightings",
                "Fetched ${fetchedDocs.size} documents from Firestore"
            ) // Add this
            documents.addAll(fetchedDocs)
            callback(documents)
        }
    }

    fun getUserSightings(callback: (ArrayList<HashMap<String, Any?>>) -> Unit) {
        dbHelper.getDocumentsWhere(
            "sightings",
            "author",
            UsersAPI.getInstance().getUserReference(auth.currentUser!!.uid) as Any
        ) { sightings ->
            callback(sightings)
        }
    }

    fun updateSighting(
        sightingId: String,
        data: HashMap<String, Any?>,
        callback: (Boolean) -> Unit
    ) {
        dbHelper.updateDocument("sightings", sightingId, data) { success ->
            callback(success)
        }
    }

    fun deleteSighting(sightingId: String, callback: (Boolean) -> Unit) {
        dbHelper.deleteDocument("sightings", sightingId) { success ->
            callback(success)
        }
    }

    fun increaseScore(sightingId: String, amount: Int = 1, callback: (Boolean) -> Unit) {
        dbHelper.getDocument("sightings", sightingId) { sightingData ->
            val score = sightingData["score"] as Long
            dbHelper.updateDocument(
                "sightings",
                sightingId,
                hashMapOf("score" to score + amount)
            ) { success ->
                callback(success)
            }
        }
    }

    fun decreaseScore(sightingId: String, amount: Int = 1, callback: (Boolean) -> Unit) {
        dbHelper.getDocument("sightings", sightingId) { sightingData ->
            val score = sightingData["score"] as Long
            dbHelper.updateDocument(
                "sightings",
                sightingId,
                hashMapOf("score" to score - amount)
            ) { success ->
                callback(success)
            }
        }
    }

    fun getImageUriForSighting(sightingId: String, callback: (String?) -> Unit) {
        dbHelper.getDocument("sightings", sightingId) { sightingData ->
            if (sightingData.isNotEmpty()) {
                val imageUri = sightingData["imageId"] as? String
                callback(imageUri)
            } else {
                callback(null)
            }
        }
    }

}