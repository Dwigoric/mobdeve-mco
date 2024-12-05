package com.mobdeve.group3.mco.storage

import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.storage
import java.util.UUID

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

    fun generateObjectRef(path: Array<String>, name: String): StorageReference {
        return storageRef.child(path.joinToString("/") + "/" + name)
    }

    fun generateNewObjectRef(path: Array<String>): StorageReference {
        return generateObjectRef(path, generateId())
    }

    fun getObject(objectRef: StorageReference, callback: (ByteArray) -> Unit) {
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

    fun getObject(path: Array<String>, name: String, callback: (ByteArray) -> Unit) {
        return getObject(generateObjectRef(path, name), callback)
    }

    fun putObject(objectRef: StorageReference, data: ByteArray, callback: (String) -> Unit) {
        objectRef.putBytes(data)
            .addOnSuccessListener {
                Log.d(TAG, "Successfully uploaded object")
                callback(objectRef.name)
            }
            .addOnFailureListener {
                Log.e(TAG, "Error uploading object", it)
                callback("")
            }
    }

    fun putObject(path: Array<String>, name: String, data: ByteArray, callback: (String) -> Unit) {
        return putObject(generateObjectRef(path, name), data, callback)
    }

    fun deleteObject(objectRef: StorageReference, callback: (Boolean) -> Unit) {
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

    fun deleteObject(path: Array<String>, name: String, callback: (Boolean) -> Unit) {
        return deleteObject(generateObjectRef(path, name), callback)
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

    private fun generateId(): String {
        return UUID.randomUUID().toString()
    }
}