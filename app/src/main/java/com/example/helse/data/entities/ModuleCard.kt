package com.example.helse.data.entities

import com.example.helse.R

data class ModuleCard(
    val iconResourceId: Int = R.drawable.ic_launcher_foreground,
    val category: String = "Module",
    val dangerIndicator: String = "Dangerindicator",
    val pushEnabled: Boolean = true
)