package com.example.librosaplicacion

import android.os.Bundle
import android.view.inputmethod.EditorInfo
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.librosaplicacion.data.LibrosXmlParser
import com.example.librosaplicacion.databinding.ActivityBusquedaLibrosBinding

class BusquedaLibros : AppCompatActivity() {

    private lateinit var binding: ActivityBusquedaLibrosBinding
    private lateinit var adaptador: AdaptadorBusqueda

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityBusquedaLibrosBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ajustarBordes(binding.root)

        // Lista inicial: todos los libros del XML
        adaptador = AdaptadorBusqueda(LibrosXmlParser.cargar(this))
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

    // Filtra los libros cuyo título o autor contienen el texto escrito
    private fun buscar() {
        val texto = binding.etBuscar.text.toString().trim()
        val resultados = LibrosXmlParser.cargar(this).filter {
            it.titulo.contains(texto, ignoreCase = true) || it.autor.contains(texto, ignoreCase = true)
        }
        adaptador.actualizar(resultados)
    }
}