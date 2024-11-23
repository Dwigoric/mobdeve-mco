package com.mobdeve.group3.mco.db

import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.firebase.Firebase
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
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

    fun addDocument(collection: String, documentId: String, data: HashMap<String, Any>): Boolean {
        var success = true

        val document = db.collection(collection).document(documentId)
        document.set(data)
            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot added with ID: ${document.id}") }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding document", e)
                success = false
            }

        return success
    }

    fun addDocument(collection: String, data: HashMap<String, Any>): String {
        val document = db.collection(collection).document()
        document.set(data)
            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot added with ID: ${document.id}") }
            .addOnFailureListener { e -> Log.w(TAG, "Error adding document", e) }

        return document.id
    }

    fun updateDocument(
        collection: String,
        documentId: String,
        data: HashMap<String, Any>
    ): Boolean {
        var success = true

        db.collection(collection)
            .document(documentId)
            .update(data)
            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully updated!") }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error updating document", e)
                success = false
            }

        return success
    }

    fun deleteDocument(collection: String, documentId: String): Boolean {
        var success = true

        db.collection(collection)
            .document(documentId)
            .delete()
            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully deleted!") }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error deleting document", e)
                success = false
            }

        return success
    }

    fun getDocument(
        collection: String,
        documentId: String
    ): HashMap<String, Any> {
        val document = HashMap<String, Any>()
        db.collection(collection)
            .document(documentId)
            .get()
            .addOnSuccessListener { result ->
                document.putAll(result.data as HashMap<String, Any>)
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting document.", exception)
            }

        return document
    }

    fun getDocumentA(collection: String, documentId: String, callback: (HashMap<String, Any>) -> Unit) {
        val document = HashMap<String, Any>()
        db.collection(collection)
            .document(documentId)
            .get()
            .addOnSuccessListener { result ->
                result.data?.let {
                    document.putAll(it)
                }
                callback(document)  // Return the document through the callback
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting document.", exception)
                callback(document)  // Return an empty document on failure
            }
    }


    fun getDocuments(collection: String): ArrayList<HashMap<String, Any>> {
        val documents = ArrayList<HashMap<String, Any>>()
        db.collection(collection)
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    documents.add(document.data as HashMap<String, Any>)
                }
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents.", exception)
            }

        return documents
    }

    fun getDocumentsWhere(
        collection: String,
        field: String,
        value: Any
    ): ArrayList<HashMap<String, Any>> {
        val documents = ArrayList<HashMap<String, Any>>()
        db.collection(collection)
            .whereEqualTo(field, value)
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    documents.add(document.data as HashMap<String, Any>)
                }
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents.", exception)
            }

        return documents
    }

    fun getDocumentsWhereMultiple(
        collection: String,
        fields: HashMap<String, Any>
    ): ArrayList<HashMap<String, Any>> {
        val documents = ArrayList<HashMap<String, Any>>()
        var query = db.collection(collection)
        for (field in fields) {
            query = query.whereEqualTo(field.key, field.value) as CollectionReference
        }
        query.get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    documents.add(document.data as HashMap<String, Any>)
                }
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents.", exception)
            }

        return documents
    }
}