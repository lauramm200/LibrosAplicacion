package com.example.librosaplicacion.data // Paquete donde se ubican las clases de acceso a datos / parsers

import android.content.Context                        // Permite acceder a recursos globales del sistema Android
import android.util.Xml                              // Proporciona utilidades para el procesamiento de XML en Android
import com.example.librosaplicacion.R                 // Acceso a los recursos de la aplicación (R.raw.libros)
import org.xmlpull.v1.XmlPullParser                  // Clase encargada de parsear elementos XML paso a paso

// Objeto singleton responsable de leer y parsear el archivo XML de libros
object LibrosXmlParser {

    // Función que lee el recurso R.raw.libros y devuelve una lista de objetos Libro
    fun cargar(context: Context): List<Libro> {
        val libros = mutableListOf<Libro>() // Lista acumuladora donde guardaremos los libros extraídos

        // Abre el archivo XML ubicado en res/raw/libros.xml como un InputStream y garantiza su cierre al finalizar (.use)
        context.resources.openRawResource(R.raw.libros).use { input ->
            val parser = Xml.newPullParser()          // Crea un nuevo parser de tipo XmlPullParser
            parser.setInput(input, "UTF-8")          // Configura la fuente de entrada y la codificación de caracteres (UTF-8)

            var etiqueta = ""                         // Variable temporal para recordar la etiqueta XML activa
            var volumeId = ""                         // Variable para almacenar el ID del libro actual
            var titulo = ""                           // Variable para almacenar el título del libro actual
            var autor = ""                            // Variable para almacenar el autor del libro actual
            var paginas = 0                           // Variable para almacenar el número de páginas del libro actual
            var evento = parser.eventType            // Obtiene el evento inicial del parser (normalmente START_DOCUMENT)

            // Recorre el documento XML hasta llegar a la etiqueta final de cierre de documento (END_DOCUMENT)
            while (evento != XmlPullParser.END_DOCUMENT) {
                when (evento) {
                    // Al encontrar la apertura de una etiqueta (<libro>, <titulo>, <autor>, etc.)
                    XmlPullParser.START_TAG -> {
                        etiqueta = parser.name        // Guarda el nombre de la etiqueta abierta actual
                        // Al iniciar un nuevo nodo de libro (<libro>), reinicia los valores temporales
                        if (etiqueta == "libro") {
                            volumeId = ""             // Reinicia el volumeId para el nuevo libro
                            titulo = ""               // Reinicia el título para el nuevo libro
                            autor = ""                // Reinicia el autor para el nuevo libro
                            paginas = 0               // Reinicia el número de páginas para el nuevo libro
                        }
                    }

                    // Al leer el contenido de texto que hay dentro de una etiqueta
                    XmlPullParser.TEXT -> {
                        val texto = parser.text.trim() // Obtiene el texto eliminando espacios y saltos de línea innecesarios
                        if (texto.isNotEmpty()) {     // Verifica que el texto leído no esté vacío
                            when (etiqueta) {
                                "volumeId" -> volumeId = texto                  // Asigna el texto al ID si estamos dentro de <volumeId>
                                "titulo" -> titulo = texto                      // Asigna el texto al título si estamos dentro de <titulo>
                                "autor" -> autor = texto                        // Asigna el texto al autor si estamos dentro de <autor>
                                "paginas" -> paginas = texto.toIntOrNull() ?: 0 // Convierte el texto a entero para las páginas
                            }
                        }
                    }

                    // Al encontrar el cierre de una etiqueta (</libro>, </titulo>, etc.)
                    XmlPullParser.END_TAG -> {
                        // Si se cierra el nodo completo </libro>, crea y añade el objeto Libro a la lista
                        if (parser.name == "libro") {
                            libros.add(
                                Libro(
                                    volumeId = volumeId,
                                    titulo = titulo,
                                    autor = autor,
                                    paginas = paginas,
                                    estado = Estado.QUIERO_LEER
                                )
                            )
                        }
                        etiqueta = ""                 // Resetea la etiqueta activa al cerrar cualquier nodo
                    }
                }
                evento = parser.next()                // Avanza al siguiente evento del documento XML (siguiente nodo)
            }
        }
        return libros                                 // Devuelve la lista completa de libros parseados desde el XML
    }
}
