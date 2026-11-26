package com.example.librosaplicacion

import android.os.Bundle
import android.util.Log
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.librosaplicacion.api.RetrofitClient
import com.example.librosaplicacion.api.aLibro
import com.example.librosaplicacion.databinding.ActivityBusquedaLibrosBinding
import kotlinx.coroutines.launch

class BusquedaLibros : AppCompatActivity() {

    private lateinit var binding: ActivityBusquedaLibrosBinding
    private lateinit var adaptador: AdaptadorBusqueda

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityBusquedaLibrosBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ajustarBordes(binding.root)

        // La lista empieza vacía: se llena al buscar
        adaptador = AdaptadorBusqueda(emptyList())
        binding.rvResultados.layoutManager = LinearLayoutManager(this)
        binding.rvResultados.adapter = adaptador

        // Al pulsar la lupa del teclado se lanza la búsqueda
        binding.etBuscar.setOnEditorActionListener { _, accion, _ ->
            if (accion == EditorInfo.IME_ACTION_SEARCH) {
                buscar()
                true
            } else false
        }
    }

    // Pide a Google Books los libros que coinciden con el texto y los muestra
    private fun buscar() {
        val texto = binding.etBuscar.text.toString().trim()
        if (texto.isEmpty()) return

        lifecycleScope.launch {
            try {
                val respuesta = RetrofitClient.api.buscar(texto)
                adaptador.actualizar(respuesta.items.orEmpty().map { it.aLibro() })
            } catch (e: Exception) {
                Log.e("BusquedaLibros", "Error al buscar", e)
                Toast.makeText(this@BusquedaLibros, R.string.error_busqueda, Toast.LENGTH_SHORT).show()
            }
        }
    }
}