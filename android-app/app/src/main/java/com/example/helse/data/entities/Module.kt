package com.example.helse.data.entities

data class Module(
    val moduleKey: String,
    val iconResourceId: Int,
    val category: String = "Kategori",
    var dangerIndicator: String = "",
    var isLoading: Boolean = true
)