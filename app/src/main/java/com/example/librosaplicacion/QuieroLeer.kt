package com.example.librosaplicacion

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.librosaplicacion.data.AppDatabase
import com.example.librosaplicacion.data.Estado
import com.example.librosaplicacion.data.Libro
import com.example.librosaplicacion.databinding.ActivityQuieroLeerBinding
import kotlinx.coroutines.launch

class QuieroLeer : AppCompatActivity() {

    private lateinit var binding: ActivityQuieroLeerBinding
    private val dao by lazy { AppDatabase.obtener(this).libroDao() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityQuieroLeerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ajustarBordes(binding.root)
        configurarBarraInferior(binding.barraInferior.bottomNav, R.id.nav_quiero)

        binding.btnPerfilHeader.setOnClickListener { abrirPerfil() }

        binding.rvQuieroLeer.layoutManager = LinearLayoutManager(this)

        lifecycleScope.launch {
            dao.porEstado(Estado.QUIERO_LEER).collect { libros ->
                binding.rvQuieroLeer.adapter = AdaptadorQuiero(
                    libros.sortedByDescending { it.interes ?: 0 }
                ) { empezar(it) }
            }
        }
    }

    // Pasa el libro a "Leyendo" guardando la fecha de inicio (ahora)
    private fun empezar(libro: Libro) {
        lifecycleScope.launch {
            dao.guardar(libro.copy(estado = Estado.LEYENDO, fechaInicio = System.currentTimeMillis()))
        }
    }
}