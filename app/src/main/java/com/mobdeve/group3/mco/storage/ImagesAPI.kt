package com.mobdeve.group3.mco.storage

class ImagesAPI {
    private val storageHelper = StorageHelper.getInstance()

    companion object {
        private var instance: ImagesAPI? = null

        fun getInstance(): ImagesAPI {
            if (instance == null) {
                instance = ImagesAPI()
            }
            return instance as ImagesAPI
        }
    }

    private constructor()

    private fun getImage(path: Array<String>, name: String, callback: (ByteArray) -> Unit) {
        val path = arrayOf("images") + path
        storageHelper.getObject(path, name) { data ->
            callback(data)
        }
    }

    private fun putImage(
        path: Array<String>,
        name: String,
        data: ByteArray,
        callback: (String) -> Unit
    ) {
        val path = arrayOf("images") + path
        storageHelper.putObject(path, name, data) { url ->
            callback(url)
        }
    }

    private fun deleteImage(path: Array<String>, name: String, callback: (Boolean) -> Unit) {
        val path = arrayOf("images") + path
        storageHelper.deleteObject(path, name) { success ->
            callback(success)
        }
    }

    private fun getDownloadUrl(path: Array<String>, name: String, callback: (String) -> Unit) {
        val path = arrayOf("images") + path
        storageHelper.getDownloadUrl(path, name) { url ->
            callback(url)
        }
    }

    fun getProfileImage(userId: String, callback: (ByteArray) -> Unit) {
        getImage(arrayOf("profile"), userId, callback)
    }

    fun getProfileImageUrl(userId: String, callback: (String) -> Unit) {
        getDownloadUrl(arrayOf("profile"), userId, callback)
    }

    fun putProfileImage(userId: String, data: ByteArray, callback: (String) -> Unit) {
        putImage(arrayOf("profile"), userId, data, callback)
    }

    fun deleteProfileImage(userId: String, callback: (Boolean) -> Unit) {
        deleteImage(arrayOf("profile"), userId, callback)
    }

    fun getSightingImage(sightingId: String, callback: (ByteArray) -> Unit) {
        getImage(arrayOf("sightings"), sightingId, callback)
    }

    fun getSightingImageUrl(sightingId: String, callback: (String) -> Unit) {
        getDownloadUrl(arrayOf("sightings"), sightingId, callback)
    }

    fun putSightingImage(sightingId: String, data: ByteArray, callback: (String) -> Unit) {
        putImage(arrayOf("sightings"), sightingId, data, callback)
    }

    fun deleteSightingImage(sightingId: String, callback: (Boolean) -> Unit) {
        deleteImage(arrayOf("sightings"), sightingId, callback)
    }

    fun getSpeciesImage(scientificName: String, callback: (ByteArray) -> Unit) {
        getImage(arrayOf("species"), scientificName, callback)
    }

    fun getSpeciesImageUrl(scientificName: String, callback: (String) -> Unit) {
        getDownloadUrl(arrayOf("species"), scientificName, callback)
    }

    fun putSpeciesImage(scientificName: String, data: ByteArray, callback: (String) -> Unit) {
        putImage(arrayOf("species"), scientificName, data, callback)
    }

    fun deleteSpeciesImage(scientificName: String, callback: (Boolean) -> Unit) {
        deleteImage(arrayOf("species"), scientificName, callback)
    }
}