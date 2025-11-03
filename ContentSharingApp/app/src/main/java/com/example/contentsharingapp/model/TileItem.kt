package com.example.contentsharingapp.model

data class TileItem(
    val id: String = "",
    val title: String = "",
    val type: String = "content", 
    val imagePath: String? = null,
    val targetUrl: String? = null,
    val youtubeId: String? = null,
    val order: Long = 0,
    val visible: Boolean = true,
    var imageUrl: String? = null 
)
