package com.example.librosaplicacion

import android.app.Activity
import android.content.Intent
import com.google.android.material.bottomnavigation.BottomNavigationView
import android.view.View
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// Configura la barra inferior de Leídos, Leyendo y Quiero leer.
// "actual" = id de la opción que corresponde a la pantalla donde se llama.
fun Activity.configurarBarraInferior(barra: BottomNavigationView, actual: Int) {
    // Relaciona cada opción del menú con su pantalla
    val destinos = mapOf(
        R.id.nav_leidos to LeidosLibros::class.java,
        R.id.nav_leyendo to EstoyLeyendo::class.java,
        R.id.nav_quiero to QuieroLeer::class.java
    )

    // Resalta la opción de esta pantalla (antes del listener, para no disparar un clic)
    barra.selectedItemId = actual

    barra.setOnItemSelectedListener { item ->
        if (item.itemId != actual) {
            startActivity(Intent(this, destinos.getValue(item.itemId)))
            finish()
        }
        true
    }
}

// Añade padding igual al tamaño de las barras del sistema para que no tapen el contenido
fun ajustarBordes(vista: View) {
    // Padding original del XML, guardado antes de que el listener lo modifique
    val izq = vista.paddingLeft
    val arr = vista.paddingTop
    val der = vista.paddingRight
    val aba = vista.paddingBottom

    ViewCompat.setOnApplyWindowInsetsListener(vista) { v, insets ->
        val barras = insets.getInsets(WindowInsetsCompat.Type.systemBars())
        v.setPadding(izq + barras.left, arr + barras.top, der + barras.right, aba + barras.bottom)
        insets
    }
}

// Convierte milisegundos a texto tipo "2 nov 2026"
fun formatearFecha(milisegundos: Long?): String {
    if (milisegundos == null) return ""
    return SimpleDateFormat("d MMM yyyy", Locale("es", "ES")).format(Date(milisegundos))
}