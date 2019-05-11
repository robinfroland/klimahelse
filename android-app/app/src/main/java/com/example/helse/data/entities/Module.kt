package com.example.helse.data.entities

import com.example.helse.R

data class Module(
    val moduleKey: String,
    val iconResourceId: Int = R.drawable.ic_launcher_foreground,
    var category: String = "Module",
    val dangerIndicator: String = "Dangerindicator",
    var pushEnabled: Boolean = true,
    var enabledOnDashboard: Boolean = true
)