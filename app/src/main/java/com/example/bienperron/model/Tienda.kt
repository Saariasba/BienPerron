package com.example.bienperron.model

data class Tienda(
    val id: String,
    val nombre: String? = "",
    val image: String? = "",
    val latitud: Double? = 0.0,
    val longitud: Double? = 0.0,
    val telefono: String? = ""
)
