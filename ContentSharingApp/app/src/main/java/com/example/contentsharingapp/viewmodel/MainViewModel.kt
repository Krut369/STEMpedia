package com.example.contentsharingapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.contentsharingapp.model.TileItem
import com.example.contentsharingapp.repository.TileRepository
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    private val repository = TileRepository()

    private val _tiles = MutableLiveData<List<TileItem>>()
    val tiles: LiveData<List<TileItem>> = _tiles

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    init {
        loadTiles()
    }

    fun loadTiles() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            try {
                val tilesList = repository.getTiles()
                _tiles.value = tilesList
                
                if (tilesList.isEmpty()) {
                    kotlinx.coroutines.delay(1000)
                    val reloadedTiles = repository.getTiles()
                    _tiles.value = reloadedTiles
                }
            } catch (e: Exception) {
                _error.value = e.message
                _tiles.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }
}
