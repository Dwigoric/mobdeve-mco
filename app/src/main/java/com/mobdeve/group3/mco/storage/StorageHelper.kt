package com.mobdeve.group3.mco.storage

import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.storage.storage

class StorageHelper {
    private val storageRef = Firebase.storage.reference
    private val TAG = "StorageHelper"

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
                Log.d(TAG, "Successfully downloaded object")
                callback(it)
            }
            .addOnFailureListener {
                Log.e(TAG, "Error downloading object", it)
                callback(byteArrayOf())
            }
    }

    fun putObject(path: Array<String>, name: String, data: ByteArray, callback: (String) -> Unit) {
        val objectRef = storageRef.child(path.joinToString("/") + "/" + name)
        objectRef.putBytes(data)
            .addOnSuccessListener {
                Log.d(TAG, "Successfully uploaded object")
                objectRef.downloadUrl
                    .addOnSuccessListener { callback(it.toString()) }
                    .addOnFailureListener {
                        Log.e(TAG, "Error retrieving download URL", it)
                        callback("")
                    }
            }
            .addOnFailureListener {
                Log.e(TAG, "Error uploading object", it)
                callback("")
            }
    }

    fun deleteObject(path: Array<String>, name: String, callback: (Boolean) -> Unit) {
        val objectRef = storageRef.child(path.joinToString("/") + "/" + name)
        objectRef.delete()
            .addOnSuccessListener {
                Log.d(TAG, "Successfully deleted object")
                callback(true)
            }
            .addOnFailureListener {
                Log.e(TAG, "Error deleting object", it)
                callback(false)
            }
    }

    fun getDownloadUrl(path: Array<String>, name: String, callback: (String) -> Unit) {
        val objectRef = storageRef.child(path.joinToString("/") + "/" + name)
        objectRef.downloadUrl
            .addOnSuccessListener {
                Log.d(TAG, "Successfully retrieved download URL")
                callback(it.toString())
            }
            .addOnFailureListener {
                Log.e(TAG, "Error retrieving download URL", it)
                callback("")
            }
    }

    fun getMetadata(path: Array<String>, name: String, callback: (String) -> Unit) {
        val objectRef = storageRef.child(path.joinToString("/") + "/" + name)
        objectRef.metadata
            .addOnSuccessListener {
                Log.d(TAG, "Successfully retrieved metadata")
                callback(it.contentType.toString())
            }
            .addOnFailureListener {
                Log.e(TAG, "Error retrieving metadata", it)
                callback("")
            }
    }
}