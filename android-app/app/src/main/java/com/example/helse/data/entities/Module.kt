package com.example.helse.data.entities

import com.example.helse.R

data class Module(
    val moduleKey: String,
    val iconResourceId: Int = R.drawable.ic_launcher_foreground,
    val category: String = "Module",
    var dangerIndicator: String = "Dangerindicator"
)