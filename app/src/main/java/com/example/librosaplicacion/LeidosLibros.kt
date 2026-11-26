package com.example.librosaplicacion

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.librosaplicacion.data.AppDatabase
import com.example.librosaplicacion.data.Estado
import com.example.librosaplicacion.databinding.ActivityLeidosLibrosBinding
import kotlinx.coroutines.launch

class LeidosLibros : AppCompatActivity() {

    private lateinit var binding: ActivityLeidosLibrosBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLeidosLibrosBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ajustarBordes(binding.root)
        configurarBarraInferior(binding.barraInferior.bottomNav, R.id.nav_leidos)

        lifecycleScope.launch {
            AppDatabase.obtener(this@LeidosLibros).libroDao().porEstado(Estado.LEIDO)
                .collect { libros ->
                    // Los terminados más recientemente, primero
                    val leidos = libros.sortedByDescending { it.fechaFin ?: 0L }
                    binding.tvContadorLeidos.text = getString(R.string.contador_leidos, leidos.size)
                    binding.rvLeidos.adapter = AdaptadorLeidos(leidos) { abrirValorar(it) }
                }
        }
    }
}