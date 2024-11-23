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

    fun addSighting(data: HashMap<String, Any>): String {
        return dbHelper.addDocument("sightings", data)
    }

    fun getSighting(sightingId: String): HashMap<String, Any> {
        return dbHelper.getDocument("sightings", sightingId)
    }

    fun updateSighting(sightingId: String, data: HashMap<String, Any>): Boolean {
        return dbHelper.updateDocument("sightings", sightingId, data)
    }

    fun deleteSighting(sightingId: String): Boolean {
        return dbHelper.deleteDocument("sightings", sightingId)
    }
}