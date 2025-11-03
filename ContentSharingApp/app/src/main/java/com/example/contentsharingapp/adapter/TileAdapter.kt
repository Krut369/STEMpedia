package com.example.contentsharingapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.contentsharingapp.R
import com.example.contentsharingapp.model.TileItem

class TileAdapter(
    private val tiles: List<TileItem>,
    private val onTileClick: (TileItem) -> Unit
) : RecyclerView.Adapter<TileAdapter.TileViewHolder>() {

    inner class TileViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageView: ImageView = itemView.findViewById(R.id.tileImageView)
        private val titleTextView: TextView = itemView.findViewById(R.id.tileTitleTextView)

        fun bind(tile: TileItem) {
            titleTextView.text = tile.title

            if (!tile.imageUrl.isNullOrBlank()) {
                Glide.with(itemView.context)
                    .load(tile.imageUrl)
                    .centerCrop()
                    .placeholder(R.drawable.ic_launcher_background)
                    .error(R.drawable.ic_launcher_background)
                    .into(imageView)
            } else {
                imageView.setImageResource(R.drawable.ic_launcher_background)
            }

            itemView.setOnClickListener {
                onTileClick(tile)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TileViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_tile, parent, false)
        return TileViewHolder(view)
    }

    override fun onBindViewHolder(holder: TileViewHolder, position: Int) {
        holder.bind(tiles[position])
    }

    override fun getItemCount(): Int = tiles.size
}
