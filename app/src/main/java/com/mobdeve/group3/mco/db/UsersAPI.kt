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

    fun addUser(userId: String, data: HashMap<String, Any>): Boolean {
        return dbHelper.addDocument("users", userId, data)
    }

    fun getUser(userId: String): HashMap<String, Any> {
        return dbHelper.getDocument("users", userId)
    }

    fun updateUser(userId: String, data: HashMap<String, Any>): Boolean {
        return dbHelper.updateDocument("users", userId, data)
    }
}