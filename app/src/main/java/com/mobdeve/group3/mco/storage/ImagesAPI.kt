package com.mobdeve.group3.mco.storage

import java.io.ByteArrayOutputStream
import java.io.InputStream

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

        fun getByteArrayFromInputStream(inputStream: InputStream?): ByteArray {
            val stream = ByteArrayOutputStream()
            val buffer = ByteArray(1024)
            var len: Int
            while (inputStream?.read(buffer).also {
                    len = if (it != null) it else -1
                } != -1) {
                stream.write(buffer, 0, len)
            }
            return stream.toByteArray()
        }
    }

    private constructor()

    private fun getImage(path: String, name: String, callback: (ByteArray) -> Unit) {
        storageHelper.getObject(arrayOf("images", path), name) { data ->
            callback(data)
        }
    }

    private fun putImage(
        path: String,
        name: String,
        data: ByteArray,
        callback: (String) -> Unit
    ) {
        storageHelper.putObject(arrayOf("images", path), name, data) { url ->
            callback(url)
        }
    }

    private fun putNewImage(path: String, data: ByteArray, callback: (String) -> Unit) {
        val objectRef = storageHelper.generateNewObjectRef(arrayOf("images", path))
        storageHelper.putObject(objectRef, data) { url ->
            callback(url)
        }
    }

    private fun deleteImage(path: String, name: String, callback: (Boolean) -> Unit) {
        storageHelper.deleteObject(arrayOf("images", path), name) { success ->
            callback(success)
        }
    }

    private fun getDownloadUrl(path: String, name: String, callback: (String) -> Unit) {
        storageHelper.getDownloadUrl(arrayOf("images", path), name) { url ->
            callback(url)
        }
    }

    fun getProfileImage(userId: String, callback: (ByteArray) -> Unit) {
        getImage("profile", userId, callback)
    }

    fun getProfileImageUrl(userId: String, callback: (String) -> Unit) {
        getDownloadUrl("profile", userId, callback)
    }

    fun putProfileImage(userId: String, data: ByteArray, callback: (String) -> Unit) {
        putImage("profile", userId, data, callback)
    }

    fun deleteProfileImage(userId: String, callback: (Boolean) -> Unit) {
        deleteImage("profile", userId, callback)
    }

    fun getSightingImage(sightingId: String, callback: (ByteArray) -> Unit) {
        getImage("sightings", sightingId, callback)
    }

    fun getSightingImageUrl(sightingId: String, callback: (String) -> Unit) {
        getDownloadUrl("sightings", sightingId, callback)
    }

    fun putSightingImage(data: ByteArray, callback: (String) -> Unit) {
        putNewImage("sightings", data, callback)
    }

    fun deleteSightingImage(sightingId: String, callback: (Boolean) -> Unit) {
        deleteImage("sightings", sightingId, callback)
    }

    fun getSpeciesImage(scientificName: String, callback: (ByteArray) -> Unit) {
        getImage("species", scientificName, callback)
    }

    fun getSpeciesImageUrl(scientificName: String, callback: (String) -> Unit) {
        getDownloadUrl("species", scientificName, callback)
    }

    fun putSpeciesImage(scientificName: String, data: ByteArray, callback: (String) -> Unit) {
        putImage("species", scientificName, data, callback)
    }

    fun deleteSpeciesImage(scientificName: String, callback: (Boolean) -> Unit) {
        deleteImage("species", scientificName, callback)
    }
}