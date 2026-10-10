package com.example.clubdeportivo.models

data class Alumno(
    val id: Int = 0,
    val nombre: String,
    val apellido: String,
    val dni: String,
    val esSocio: Boolean,
    val aptoFisico: Boolean,
    val fechaAlta: String = "",
    val fechaVencimiento: String? = null,
    val habilitado: Boolean = true
) {
    val nombreCompleto: String
        get() = "$nombre $apellido".trim()
}
