package com.example.librosaplicacion.api

import com.example.librosaplicacion.data.Libro

data class RespuestaVolumenes(val items: List<Volumen>?)
data class Volumen(val id: String, val volumeInfo: InfoVolumen)
data class InfoVolumen(
    val title: String?,
    val authors: List<String>?,
    val pageCount: Int?,
    val imageLinks: Imagenes?
)
data class Imagenes(val thumbnail: String?)

fun Volumen.aLibro() = Libro(
    volumeId = id,
    titulo = volumeInfo.title ?: "Sin título",
    autor = volumeInfo.authors?.joinToString() ?: "Autor desconocido",
    paginas = volumeInfo.pageCount ?: 0,
    portada = volumeInfo.imageLinks?.thumbnail?.replace("http://", "https://")
)