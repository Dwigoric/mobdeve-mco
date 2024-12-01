package com.mobdeve.group3.mco.storage

import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.storage.storage

class StorageHelper {
    private val storageRef = Firebase.storage.reference

    companion object {
        private var instance: StorageHelper? = null

        fun getInstance(): StorageHelper {
            if (instance == null) {
                instance = StorageHelper()
            }
            return instance as StorageHelper
        }
    }

    private constructor()

    fun getObject(path: Array<String>, name: String, callback: (ByteArray) -> Unit) {
        val objectRef = storageRef.child(path.joinToString("/") + "/" + name)
        val oneMegabyte: Long = 1024 * 1024
        objectRef.getBytes(oneMegabyte)
            .addOnSuccessListener {
                Log.d("StorageHelper", "Successfully downloaded object")
                callback(it)
            }
            .addOnFailureListener {
                Log.e("StorageHelper", "Error downloading object", it)
            }
    }

    fun putObject(path: Array<String>, name: String, data: ByteArray, callback: (Boolean) -> Unit) {
        val objectRef = storageRef.child(path.joinToString("/") + "/" + name)
        objectRef.putBytes(data)
            .addOnSuccessListener {
                Log.d("StorageHelper", "Successfully uploaded object")
                callback(true)
            }
            .addOnFailureListener {
                Log.e("StorageHelper", "Error uploading object", it)
                callback(false)
            }
    }

    fun deleteObject(path: Array<String>, name: String, callback: (Boolean) -> Unit) {
        val objectRef = storageRef.child(path.joinToString("/") + "/" + name)
        objectRef.delete()
            .addOnSuccessListener {
                Log.d("StorageHelper", "Successfully deleted object")
                callback(true)
            }
            .addOnFailureListener {
                Log.e("StorageHelper", "Error deleting object", it)
                callback(false)
            }
    }

    fun getDownloadUrl(path: Array<String>, name: String, callback: (String) -> Unit) {
        val objectRef = storageRef.child(path.joinToString("/") + "/" + name)
        objectRef.downloadUrl
            .addOnSuccessListener {
                Log.d("StorageHelper", "Successfully retrieved download URL")
                callback(it.toString())
            }
            .addOnFailureListener {
                Log.e("StorageHelper", "Error retrieving download URL", it)
            }
    }

    fun getMetadata(path: Array<String>, name: String, callback: (String) -> Unit) {
        val objectRef = storageRef.child(path.joinToString("/") + "/" + name)
        objectRef.metadata
            .addOnSuccessListener {
                Log.d("StorageHelper", "Successfully retrieved metadata")
                callback(it.contentType.toString())
            }
            .addOnFailureListener {
                Log.e("StorageHelper", "Error retrieving metadata", it)
            }
    }
}