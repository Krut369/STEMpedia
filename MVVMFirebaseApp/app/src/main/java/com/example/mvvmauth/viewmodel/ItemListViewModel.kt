package com.example.mvvmauth.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mvvmauth.data.model.Item
import com.example.mvvmauth.data.repository.ItemRepository


class ItemListViewModel : ViewModel() {

    private val repository = ItemRepository()

    private val _items = MutableLiveData<List<Item>>()
    val items: LiveData<List<Item>> = _items

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage


    fun loadItems() {
        _isLoading.value = true
        
        _errorMessage.value = null

        repository.getItems().observeForever { result ->
            _isLoading.value = false

            result.fold(
                onSuccess = { itemsList ->
                    _items.value = itemsList
                    _errorMessage.value = null
                },
                onFailure = { exception ->
                    _items.value = emptyList()
                    _errorMessage.value = exception.message ?: "Failed to load items"
                }
            )
        }
    }

    fun clearError() {
        _errorMessage.value = null
    }

    fun addItem(title: String, description: String, onComplete: (Boolean, String?) -> Unit) {
        if (title.isBlank()) {
            onComplete(false, "Title cannot be empty")
            return
        }
        
        if (description.isBlank()) {
            onComplete(false, "Description cannot be empty")
            return
        }

        val newItem = Item(
            id = "",
            title = title.trim(),
            description = description.trim()
        )

        repository.addItem(newItem) { success, error ->
            if (success) {
                loadItems()
                onComplete(true, null)
            } else {
                onComplete(false, error ?: "Failed to add item")
            }
        }
    }
}

