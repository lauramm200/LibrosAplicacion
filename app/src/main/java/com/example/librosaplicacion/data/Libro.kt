package com.example.librosaplicacion.data

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class Estado { QUIERO_LEER, LEYENDO, LEIDO }

@Entity(tableName = "libros")
data class Libro(
    @PrimaryKey val volumeId: String,
    val titulo: String,
    val autor: String,
    val paginas: Int,
    val portada: String? = null,
    val estado: Estado = Estado.QUIERO_LEER,
    // Quiero leer
    val recomendadoPor: String? = null,
    val interes: Int? = null,
    // Leyendo
    val fechaInicio: Long? = null,
    // Leídos
    val fechaFin: Long? = null,
    val estrellas: Int? = null,
    val porcentaje: Int? = null,
    val resena: String? = null
)