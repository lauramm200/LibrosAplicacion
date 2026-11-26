package com.example.librosaplicacion

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
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

        lifecycleScope.launch {
            AppDatabase.obtener(this@EstoyLeyendo).libroDao().porEstado(Estado.LEYENDO)
                .collect { libros ->
                    // El que empezó más recientemente va primero
                    val leyendo = libros.sortedByDescending { it.fechaInicio ?: 0L }
                    binding.tvContadorLeyendo.text = getString(R.string.contador_leyendo, leyendo.size)

                    // El primero va en la tarjeta destacada; el resto, en la lista
                    val actual = leyendo.firstOrNull()
                    val hayLibros = actual != null
                    binding.cardAhoraMismo.visibility = if (hayLibros) View.VISIBLE else View.GONE
                    binding.tvOtros.visibility = if (hayLibros) View.VISIBLE else View.GONE

                    if (actual != null) {
                        binding.tvTituloActual.text = actual.titulo
                        binding.tvAutorActual.text = actual.autor
                        binding.tvInicioActual.text =
                            getString(R.string.inicio_lectura, formatearFecha(actual.fechaInicio))
                        binding.btnMarcarTerminado.setOnClickListener { abrirValorar(actual) }
                    }
                    binding.rvOtrosLeyendo.adapter = AdaptadorLeyendo(leyendo.drop(1)) { abrirValorar(it) }
                }
        }
    }
}