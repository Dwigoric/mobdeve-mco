package com.mobdeve.group3.mco.db

import android.util.Log

class SightingsAPI {
    private val dbHelper = DbHelper.getInstance()

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

    fun addSighting(data: HashMap<String, Any>, callback: (String) -> Unit) {
        dbHelper.addDocument("sightings", data) { sightingId ->
            callback(sightingId)
        }
    }

    fun getSighting(sightingId: String, callback: (HashMap<String, Any>) -> Unit) {
        dbHelper.getDocument("sightings", sightingId) { sightingData ->
            callback(sightingData)
        }
    }

    fun getSightings(callback: (ArrayList<HashMap<String, Any>>) -> Unit) {
        val documents = ArrayList<HashMap<String, Any>>()
        dbHelper.getDocuments("sightings") { fetchedDocs ->
            Log.d("getSightings", "Fetched ${fetchedDocs.size} documents from Firestore") // Add this
            documents.addAll(fetchedDocs)
            callback(documents)
        }
    }

    fun updateSighting(
        sightingId: String,
        data: HashMap<String, Any>,
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
}