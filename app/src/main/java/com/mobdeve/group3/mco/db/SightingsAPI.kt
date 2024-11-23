package com.mobdeve.group3.mco.db

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
        dbHelper.getDocuments("sightings") { sightings ->
            callback(sightings)
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