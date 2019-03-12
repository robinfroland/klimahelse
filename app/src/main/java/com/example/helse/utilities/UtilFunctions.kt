package com.example.helse.utilities

@Suppress("UNCHECKED_CAST")
inline fun <reified T : Any> List<*>.itemsAreOfType() =
    if (all { it is T })
        this as ArrayList<T>
    else null
