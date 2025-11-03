package com.example.mvvmauth.data.model

data class Item(
    val id: String = "",
    val title: String = "",
    val description: String = ""
) {
    constructor() : this("", "", "")
}

