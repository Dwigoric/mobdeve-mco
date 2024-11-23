package com.mobdeve.group3.mco.db

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

    fun addUser(userId: String, data: HashMap<String, Any>, callback: (Boolean) -> Unit) {
        dbHelper.addDocument("users", userId, data) { success ->
            callback(success)
        }
    }

    fun getUser(userId: String, callback: (HashMap<String, Any>) -> Unit) {
        dbHelper.getDocument("users", userId) { userData ->
            callback(userData)  // Pass the result to the callback
        }
    }

    fun updateUser(userId: String, data: HashMap<String, Any>, callback: (Boolean) -> Unit) {
        dbHelper.updateDocument("users", userId, data) { success ->
            callback(success)
        }
    }
}