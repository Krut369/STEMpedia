package com.example.contentsharingapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.example.contentsharingapp.adapter.TileAdapter
import com.example.contentsharingapp.databinding.ActivityMainBinding
import com.example.contentsharingapp.model.TileItem
import com.example.contentsharingapp.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()
    private lateinit var adapter: TileAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        observeViewModel()
    }

    private fun setupRecyclerView() {
        adapter = TileAdapter(emptyList()) { tile ->
            onTileClicked(tile)
        }

        binding.recyclerViewTiles.layoutManager = GridLayoutManager(this, 3)
        binding.recyclerViewTiles.adapter = adapter
    }

    private fun observeViewModel() {
        viewModel.tiles.observe(this, Observer { tiles ->
            Log.d("MainActivity", "Tiles updated: ${tiles.size} tiles")
            val newAdapter = TileAdapter(tiles) { tile ->
                onTileClicked(tile)
            }
            binding.recyclerViewTiles.adapter = newAdapter
            adapter = newAdapter
            
            if (tiles.isEmpty()) {
                Log.w("MainActivity", "No tiles loaded - Firebase collection may be empty")
                binding.recyclerViewTiles.postDelayed({
                    Toast.makeText(
                        this,
                        "No tiles found. Please add tiles to Firebase collection 'home_tiles'",
                        Toast.LENGTH_LONG
                    ).show()
                }, 2000)
            } else {
                Log.d("MainActivity", "Successfully displaying ${tiles.size} tiles")
            }
        })

        viewModel.isLoading.observe(this, Observer { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        })

        viewModel.error.observe(this, Observer { error ->
            if (error != null) {
                Log.e("MainActivity", "Firebase error: $error")
                if (viewModel.tiles.value?.isEmpty() == true) {
                    Toast.makeText(
                        this,
                        "Unable to load tiles. Check Firebase connection.",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        })
    }

    private fun onTileClicked(tile: TileItem) {
        val intent = Intent(this, WebViewActivity::class.java).apply {
            putExtra("type", tile.type)
            putExtra("targetUrl", tile.targetUrl)
            putExtra("youtubeId", tile.youtubeId)
        }
        startActivity(intent)
    }
}