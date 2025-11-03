package com.example.mvvmauth.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mvvmauth.data.model.Item
import com.example.mvvmauth.databinding.ItemRowBinding

class ItemAdapter : RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {

    private val items = mutableListOf<Item>()

    class ItemViewHolder(val binding: ItemRowBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        // Inflate layout using ViewBinding
        val binding = ItemRowBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ItemViewHolder(binding)
    }


    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        // Get the item at this position
        val item = items[position]
        
        // Bind data to views using ViewBinding
        holder.binding.apply {
            titleTextView.text = item.title
            descriptionTextView.text = item.description
        }
    }


    override fun getItemCount(): Int = items.size


    fun updateItems(newItems: List<Item>) {
        items.clear()
        items.addAll(newItems)
        // Notify adapter that the entire data set has changed
        notifyDataSetChanged()
    }
}

