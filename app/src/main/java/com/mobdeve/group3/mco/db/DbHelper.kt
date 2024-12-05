package com.mobdeve.group3.mco.db

import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.firestore

class DbHelper {
    private val db = Firebase.firestore
    private val TAG = "DbHelper"

    companion object {
        private var instance: DbHelper? = null

        fun getInstance(): DbHelper {
            if (instance == null) {
                instance = DbHelper()
            }
            return instance as DbHelper
        }
    }

    private constructor()

    fun constructData(vararg data: Pair<String, Any>): HashMap<String, Any> {
        val map = HashMap<String, Any>()
        for (pair in data) {
            map[pair.first] = pair.second
        }
        return map
    }

    fun addDocument(
        collection: String,
        documentId: String,
        data: HashMap<String, Any?>,
        callback: (Boolean) -> Unit
    ) {
        val document = db.collection(collection).document(documentId)
        document.set(data)
            .addOnSuccessListener {
                Log.d(TAG, "DocumentSnapshot added with ID: ${document.id}")
                callback(true)
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding document", e)
                callback(false)
            }
    }

    fun addDocument(
        collection: String,
        data: HashMap<String, Any?>,
        callback: (String) -> Unit
    ) {
        val document = db.collection(collection).document()
        document.set(data)
            .addOnSuccessListener {
                Log.d(TAG, "DocumentSnapshot added with ID: ${document.id}")
                callback(document.id)
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding document", e)
                callback("")
            }
    }

    fun updateDocument(
        collection: String,
        documentId: String,
        data: HashMap<String, Any?>,
        callback: (Boolean) -> Unit
    ) {
        db.collection(collection)
            .document(documentId)
            .update(data)
            .addOnSuccessListener {
                Log.d(TAG, "DocumentSnapshot successfully updated!")
                callback(true)
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error updating document", e)
                callback(false)
            }
    }

    fun deleteDocument(collection: String, documentId: String, callback: (Boolean) -> Unit) {
        db.collection(collection)
            .document(documentId)
            .delete()
            .addOnSuccessListener {
                Log.d(TAG, "DocumentSnapshot successfully deleted!")
                callback(true)
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error deleting document", e)
                callback(false)
            }
    }

    fun getDocument(
        collection: String,
        documentId: String,
        callback: (HashMap<String, Any?>) -> Unit
    ) {
        val document = HashMap<String, Any?>()
        db.collection(collection)
            .document(documentId)
            .get()
            .addOnSuccessListener { result ->
                val data = result.data
                data?.set("id", result.id)
                document.putAll(data as HashMap<String, Any?>)
                callback(document)  // Return the document through the callback
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting document.", exception)
                callback(document)  // Return an empty document on failure
            }
    }


    fun getDocuments(collection: String, callback: (ArrayList<HashMap<String, Any?>>) -> Unit) {
        val documents = ArrayList<HashMap<String, Any?>>()
        db.collection(collection)
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val data = document.data as HashMap<String, Any?>
                    data["id"] = document.id
                    documents.add(data)
                }
                callback(documents)  // Return the documents through the callback
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents.", exception)
                callback(documents)  // Empty documents on failure
            }
    }

    fun getDocumentsWhere(
        collection: String,
        field: String,
        value: Any?,
        callback: (ArrayList<HashMap<String, Any?>>) -> Unit
    ) {
        val documents = ArrayList<HashMap<String, Any?>>()
        db.collection(collection)
            .whereEqualTo(field, value)
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val data = document.data as HashMap<String, Any?>
                    data["id"] = document.id
                    documents.add(data)
                }
                callback(documents)  // Return the documents through the callback
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents.", exception)
                callback(documents)  // Empty documents on failure
            }
    }

    fun getDocumentsWhereMultiple(
        collection: String,
        fields: HashMap<String, Any?>,
        callback: (ArrayList<HashMap<String, Any?>>) -> Unit
    ) {
        val documents = ArrayList<HashMap<String, Any?>>()
        var query = db.collection(collection)
        for (field in fields) {
            query = query.whereEqualTo(field.key, field.value) as CollectionReference
        }

        query.get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val data = document.data as HashMap<String, Any?>
                    data["id"] = document.id
                    documents.add(data)
                }
                callback(documents)  // Return the documents through the callback
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents.", exception)
                callback(documents)  // Empty documents on failure
            }
    }

    fun getDocumentReference(collection: String, documentId: String): DocumentReference? {
        return if (documentId.isNotEmpty()) {
            db.collection(collection).document(documentId)
        } else {
            Log.e(TAG, "getDocumentReference called with an empty documentId")
            null
        }
    }

}