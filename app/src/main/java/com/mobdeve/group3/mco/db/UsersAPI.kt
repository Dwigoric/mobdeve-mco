package com.mobdeve.group3.mco.db

import android.util.Log
import com.google.firebase.firestore.DocumentReference

class UsersAPI {
    private val dbHelper = DbHelper.getInstance()

    companion object {
        private var instance: UsersAPI? = null

        fun getInstance(): UsersAPI {
            if (instance == null) {
                instance = UsersAPI()
            }
            return instance as UsersAPI
        }
    }

    private constructor()

    fun addUser(userId: String, data: HashMap<String, Any?>, callback: (Boolean) -> Unit) {
        dbHelper.addDocument("users", userId, data) { success ->
            callback(success)
        }
    }

    fun getUser(userId: String, callback: (HashMap<String, Any?>) -> Unit) {
        if (userId.isEmpty()) {
            Log.e("UsersAPI", "getUser called with empty userId")
            callback(HashMap()) // Return an empty map
            return
        }
        dbHelper.getDocument("users", userId) { userData ->
            callback(userData)
        }
    }

    fun getUserWithUsername(username: String, callback: (HashMap<String, Any?>) -> Unit) {
        if (username.isEmpty()) {
            Log.e("UsersAPI", "getUserWithUsername called with empty username")
            callback(HashMap()) // Return an empty map
            return
        }

        dbHelper.getDocumentsWhere("users", "username", username) { userData ->
            if (userData.isNotEmpty()) {
                callback(userData[0])
            } else {
                callback(HashMap())
            }
        }
    }

    fun getUserReference(userId: String): DocumentReference? {
        return dbHelper.getDocumentReference("users", userId)
    }

    fun updateUser(userId: String, data: HashMap<String, Any?>, callback: (Boolean) -> Unit) {
        dbHelper.updateDocument("users", userId, data) { success ->
            callback(success)
        }
    }
}