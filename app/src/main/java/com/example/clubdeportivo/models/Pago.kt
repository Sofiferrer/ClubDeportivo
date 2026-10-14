package com.example.clubdeportivo.models

data class Pago(
    val id: Int = 0,
    val alumnoId: Int,
    val monto: Double,
    val metodoPago: String,
    val cuotas: Int = 1,
    val fechaPago: String = "",
    val periodoDesde: String = "",
    val periodoHasta: String = ""
)
