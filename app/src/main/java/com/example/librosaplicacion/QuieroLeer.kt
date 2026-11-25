package com.example.librosaplicacion

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.librosaplicacion.data.LibrosXmlParser
import com.example.librosaplicacion.databinding.ActivityQuieroLeerBinding
import com.example.librosaplicacion.model.Estado

class QuieroLeer : AppCompatActivity() {

    private lateinit var binding: ActivityQuieroLeerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityQuieroLeerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ajustarBordes(binding.root)
        configurarBarraInferior(binding.barraInferior.bottomNav, R.id.nav_quiero)

        // Solo QUIERO_LEER, de mayor a menor interés (sin interés = al final)
        val quiero = LibrosXmlParser.cargar(this)
            .filter { it.estado == Estado.QUIERO_LEER }
            .sortedByDescending { it.interes ?: 0 }

        binding.rvQuieroLeer.layoutManager = LinearLayoutManager(this)
        binding.rvQuieroLeer.adapter = AdaptadorQuiero(quiero)
    }
}