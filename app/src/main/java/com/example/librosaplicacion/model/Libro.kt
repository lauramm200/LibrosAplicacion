package com.example.librosaplicacion.model

//Define los únicos tres valores posibles de la categoría.
// Evita errores de escritura que tendrías con un String (“leido”, “Leído”...)
// y permite filtrar después con facilidad (estado == Estado.LEIDO).
enum class Estado { QUIERO_LEER, LEYENDO, LEIDO }


//Una data class genera automáticamente equals, hashCode, toString y copy().
// El copy() será clave: para pasar un libro de “Leyendo” a “Leído” harás libro.copy(estado = Estado.LEIDO, fechaFin = ..., valoracion = ...)
// sin modificar el objeto original (todo es val).
data class Libro(
    val volumeId: String,
    val titulo: String,
    val autor: String,
    val paginas: Int,
    val estado: Estado = Estado.QUIERO_LEER,

    // Quiero leer
    val recomendadoPor: String? = null,
    val interes: Int? = null,

    // Leyendo
    val fechaInicio: Long? = null,

    // Leídos
    val fechaFin: Long? = null,
    val valoracion: Int? = null,
    val porcentajeRecomendacion: Int? = null,
    val resena: String? = null
)