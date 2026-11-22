package com.example.librosaplicacion.model // Paquete donde se ubica el modelo de datos

// Enum que define los estados en los que puede estar un libro (lectura actual, futura o pasada)
enum class Estado { 
    QUIERO_LEER, // Estado para libros guardados para leer más adelante
    LEYENDO,     // Estado para libros en proceso de lectura actual
    LEIDO        // Estado para libros que ya han sido terminados
}

// Data class inmutable que representa la entidad de un Libro en la aplicación
data class Libro(
    val volumeId: String,                           // Identificador único del libro (ej: "ejemplo1" o id de Google Books)
    val titulo: String,                             // Título del libro
    val autor: String,                              // Nombre del autor o autora del libro
    val paginas: Int,                               // Número total de páginas que tiene el libro
    val estado: Estado = Estado.QUIERO_LEER,       // Estado de lectura del libro (por defecto: QUIERO_LEER)

    // CAMPOS OPCIONALES PARA LIBROS EN ESTADO "QUIERO LEER":
    val recomendadoPor: String? = null,             // Persona o fuente que recomendó el libro (opcional)
    val interes: Int? = null,                       // Nivel de interés o prioridad de lectura (del 1 al 5) (opcional)

    // CAMPOS OPCIONALES PARA LIBROS EN ESTADO "LEYENDO":
    val fechaInicio: Long? = null,                  // Timestamp en milisegundos de la fecha en que se empezó a leer (opcional)

    // CAMPOS OPCIONALES PARA LIBROS EN ESTADO "LEÍDO":
    val fechaFin: Long? = null,                     // Timestamp en milisegundos de la fecha en que se terminó de leer (opcional)
    val valoracion: Int? = null,                    // Calificación asignada al libro, por ejemplo de 1 a 5 estrellas (opcional)
    val porcentajeRecomendacion: Int? = null,       // Porcentaje de recomendación del libro (ej: 80%, 100%) (opcional)
    val resena: String? = null                      // Opinión o reseña escrita sobre el libro (opcional)
)