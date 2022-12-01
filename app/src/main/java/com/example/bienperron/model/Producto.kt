package com.example.bienperron.model

data class Producto(
    val id: String,
    val image: String? = "",
    val descripcion: String? = "",
    val nombre: String? = "",
    val precio: Long = 0
)
