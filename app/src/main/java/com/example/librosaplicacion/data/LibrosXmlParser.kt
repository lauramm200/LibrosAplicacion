package com.example.librosaplicacion.data

import com.example.librosaplicacion.R
import android.content.Context
import android.util.Xml
import com.example.librosaplicacion.model.Libro
import org.xmlpull.v1.XmlPullParser

object LibrosXmlParser {

    fun cargar(context: Context): List<Libro> {
        val libros = mutableListOf<Libro>()
        context.resources.openRawResource(R.raw.libros).use { input ->            val parser = Xml.newPullParser()
            parser.setInput(input, "UTF-8")

            var etiqueta = ""
            var volumeId = ""
            var titulo = ""
            var autor = ""
            var paginas = 0

            var evento = parser.eventType
            while (evento != XmlPullParser.END_DOCUMENT) {
                when (evento) {
                    XmlPullParser.START_TAG -> etiqueta = parser.name
                    XmlPullParser.TEXT -> when (etiqueta) {
                        "volumeId" -> volumeId = parser.text
                        "titulo" -> titulo = parser.text
                        "autor" -> autor = parser.text
                        "paginas" -> paginas = parser.text.toIntOrNull() ?: 0
                    }
                    XmlPullParser.END_TAG -> {
                        if (parser.name == "libro") {
                            libros.add(Libro(volumeId, titulo, autor, paginas))
                        }
                        etiqueta = ""
                    }
                }
                evento = parser.next()
            }
        }
        return libros
    }
}
