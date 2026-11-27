package com.example.librosaplicacion

import android.app.Activity
import android.content.Intent
import com.google.android.material.bottomnavigation.BottomNavigationView
import android.view.View
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.librosaplicacion.data.Libro
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import android.widget.ImageView
import com.bumptech.glide.Glide

// Configura la barra inferior de Leídos, Leyendo y Quiero leer.
// "actual" = id de la opción que corresponde a la pantalla donde se llama (0 si es neutra, ej. inicio o búsqueda).
fun Activity.configurarBarraInferior(barra: BottomNavigationView, actual: Int) {
    // Relaciona cada opción del menú con su pantalla
    val destinos = mapOf(
        R.id.nav_buscar to BusquedaLibros::class.java,
        R.id.nav_leidos to LeidosLibros::class.java,
        R.id.nav_leyendo to EstoyLeyendo::class.java,
        R.id.nav_quiero to QuieroLeer::class.java
    )

    // Resalta la opción de esta pantalla si corresponde (0 = ninguna seleccionada de forma activa)
    if (actual != 0) {
        barra.selectedItemId = actual
    } else {
        barra.menu.setGroupCheckable(0, true, false)
        for (i in 0 until barra.menu.size()) {
            barra.menu.getItem(i).isChecked = false
        }
        barra.menu.setGroupCheckable(0, true, true)
    }

    barra.setOnItemSelectedListener { item ->
        if (item.itemId != actual) {
            val intentNavegacion = Intent(this, destinos.getValue(item.itemId))
            intentNavegacion.putExtra("usuario", obtenerUsuarioActual())
            startActivity(intentNavegacion)
        }
        true
    }
}

// Obtiene el nombre de usuario activo desde el Intent o desde la sesión guardada
fun Activity.obtenerUsuarioActual(): String {
    val deIntent = intent.getStringExtra("usuario")
    if (!deIntent.isNullOrEmpty()) return deIntent
    return getSharedPreferences("sesion", Activity.MODE_PRIVATE).getString("usuario", "") ?: ""
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

// Abre la pantalla de valorar enviándole los datos del libro terminado
fun Activity.abrirValorar(libro: Libro) {
    startActivity(
        Intent(this, ValorarLibro::class.java)
            .putExtra("volumeId", libro.volumeId)
            .putExtra("titulo", libro.titulo)
            .putExtra("autor", libro.autor)
            .putExtra("paginas", libro.paginas)
            .putExtra("portada", libro.portada)
    )
}

// Abre la pantalla de mi perfil pasando el nombre de usuario activo
fun Activity.abrirPerfil() {
    val usuario = obtenerUsuarioActual()
    startActivity(Intent(this, PerfilUser::class.java).putExtra("usuario", usuario))
}

// Carga la portada desde su URL; si no hay URL, se queda el fondo amarillo del marcador
fun ImageView.cargarPortada(url: String?) {
    Glide.with(this).load(url).into(this)
}