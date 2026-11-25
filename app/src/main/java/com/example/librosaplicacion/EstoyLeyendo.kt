package com.example.librosaplicacion

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.librosaplicacion.data.LibrosXmlParser
import com.example.librosaplicacion.databinding.ActivityEstoyLeyendoBinding
import com.example.librosaplicacion.model.Estado

class EstoyLeyendo : AppCompatActivity() {

    private lateinit var binding: ActivityEstoyLeyendoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityEstoyLeyendoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ajustarBordes(binding.root)
        configurarBarraInferior(binding.barraInferior.bottomNav, R.id.nav_leyendo)

        // Solo los libros en estado LEYENDO
        val leyendo = LibrosXmlParser.cargar(this).filter { it.estado == Estado.LEYENDO }
        binding.tvContadorLeyendo.text = getString(R.string.contador_leyendo, leyendo.size)

        // El primero va en la tarjeta destacada; el resto, en la lista
        val actual = leyendo.firstOrNull()
        if (actual != null) {
            binding.tvTituloActual.text = actual.titulo
            binding.tvAutorActual.text = actual.autor
            binding.tvInicioActual.text = getString(R.string.inicio_lectura, formatearFecha(actual.fechaInicio))
        } else {
            binding.cardAhoraMismo.visibility = View.GONE
            binding.tvOtros.visibility = View.GONE
        }
        binding.rvOtrosLeyendo.adapter = AdaptadorLeyendo(leyendo.drop(1))
    }
}