package com.example.librosaplicacion

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.librosaplicacion.data.AppDatabase
import com.example.librosaplicacion.data.Estado
import com.example.librosaplicacion.databinding.ActivityEstoyLeyendoBinding
import kotlinx.coroutines.launch

class EstoyLeyendo : AppCompatActivity() {

    private lateinit var binding: ActivityEstoyLeyendoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityEstoyLeyendoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ajustarBordes(binding.root)
        configurarBarraInferior(binding.barraInferior.bottomNav, R.id.nav_leyendo)

        binding.btnPerfilHeader.setOnClickListener { abrirPerfil() }

        binding.rvOtrosLeyendo.layoutManager = LinearLayoutManager(this)

        lifecycleScope.launch {
            AppDatabase.obtener(this@EstoyLeyendo).libroDao().porEstado(Estado.LEYENDO)
                .collect { libros ->
                    // El último libro que empezó a leerse (fechaInicio más reciente) va primero
                    val leyendo = libros.sortedByDescending { it.fechaInicio ?: 0L }
                    binding.tvContadorLeyendo.text = getString(R.string.contador_leyendo, leyendo.size)

                    // El último libro seleccionado va en la tarjeta principal ("AHORA MISMO")
                    val actual = leyendo.firstOrNull()
                    val hayLibros = actual != null
                    binding.cardAhoraMismo.visibility = if (hayLibros) View.VISIBLE else View.GONE

                    if (actual != null) {
                        binding.ivPortadaActual.cargarPortada(actual.portada)
                        binding.tvTituloActual.text = actual.titulo
                        binding.tvAutorActual.text = actual.autor
                        binding.tvInicioActual.text =
                            getString(R.string.inicio_lectura, formatearFecha(actual.fechaInicio))
                        binding.btnMarcarTerminado.setOnClickListener { abrirValorar(actual) }
                    }

                    // Los demás libros van a la lista "Otros en curso"
                    val otros = leyendo.drop(1)
                    binding.tvOtros.visibility = if (otros.isNotEmpty()) View.VISIBLE else View.GONE
                    binding.rvOtrosLeyendo.adapter = AdaptadorLeyendo(otros) { abrirValorar(it) }
                }
        }
    }
}