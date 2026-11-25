package com.example.librosaplicacion

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.librosaplicacion.data.LibrosXmlParser
import com.example.librosaplicacion.databinding.ActivityLeidosLibrosBinding
import com.example.librosaplicacion.model.Estado

class LeidosLibros : AppCompatActivity() {

    private lateinit var binding: ActivityLeidosLibrosBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLeidosLibrosBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ajustarBordes(binding.root)
        configurarBarraInferior(binding.barraInferior.bottomNav, R.id.nav_leidos)

        // Solo los libros en estado LEIDO
        val leidos = LibrosXmlParser.cargar(this).filter { it.estado == Estado.LEIDO }

        binding.tvContadorLeidos.text = getString(R.string.contador_leidos, leidos.size)
        binding.rvLeidos.adapter = AdaptadorLeidos(leidos)
    }
}