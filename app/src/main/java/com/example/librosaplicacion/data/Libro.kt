package com.example.librosaplicacion.data

import androidx.room.Entity

enum class Estado { QUIERO_LEER, LEYENDO, LEIDO }

// Clave compuesta: el mismo libro puede estar guardado por varios usuarios
@Entity(tableName = "libros", primaryKeys = ["volumeId", "usuario"])
data class Libro(
    val volumeId: String,
    val usuario: String = "",   // Dueño del registro (nombre con el que inició sesión)
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