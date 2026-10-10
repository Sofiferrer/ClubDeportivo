package com.example.clubdeportivo.models

data class Usuario(
    val id: Int = 0,
    val username: String,
    val password: String,
    val nombre: String,
    val apellido: String
) {
    val nombreCompleto: String
        get() = "$nombre $apellido".trim()
}
