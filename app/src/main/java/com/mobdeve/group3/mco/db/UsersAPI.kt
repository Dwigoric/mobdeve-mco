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

    // TODO: Register/Login using Firebase Auth

    fun getUser(userId: String): HashMap<String, Any> {
        return dbHelper.getDocument("users", userId)
    }

    fun updateUser(userId: String, data: HashMap<String, Any>): Boolean {
        return dbHelper.updateDocument("users", userId, data)
    }
}