package com.example.mvvmauth.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.mvvmauth.data.model.Item
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ItemRepository {

    private val firestore: FirebaseFirestore = Firebase.firestore
    
    private val itemsCollection = "items"
    fun getItems(): LiveData<Result<List<Item>>> {
        val itemsLiveData = MutableLiveData<Result<List<Item>>>()

        firestore.collection(itemsCollection)
            .get()
            .addOnSuccessListener { querySnapshot ->
                val items = querySnapshot.documents.mapNotNull { document ->
                    document.toObject(Item::class.java)?.copy(id = document.id)
                }
                itemsLiveData.postValue(Result.success(items))
            }
            .addOnFailureListener { exception ->
                itemsLiveData.postValue(Result.failure(exception))
            }

        return itemsLiveData
    }


    fun addItem(item: Item, onComplete: (Boolean, String?) -> Unit) {
        firestore.collection(itemsCollection)
            .add(item)
            .addOnSuccessListener {
                onComplete(true, null)
            }
            .addOnFailureListener { exception ->
                onComplete(false, exception.message)
            }
    }
}

