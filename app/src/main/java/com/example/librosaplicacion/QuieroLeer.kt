package com.example.librosaplicacion

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.librosaplicacion.data.AppDatabase
import com.example.librosaplicacion.data.Estado
import com.example.librosaplicacion.databinding.ActivityQuieroLeerBinding
import kotlinx.coroutines.launch

class QuieroLeer : AppCompatActivity() {

    private lateinit var binding: ActivityQuieroLeerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityQuieroLeerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ajustarBordes(binding.root)
        configurarBarraInferior(binding.barraInferior.bottomNav, R.id.nav_quiero)

        binding.rvQuieroLeer.layoutManager = LinearLayoutManager(this)

        // Room emite la lista cada vez que cambia la tabla; la pantalla se rehace sola
        lifecycleScope.launch {
            AppDatabase.obtener(this@QuieroLeer).libroDao().porEstado(Estado.QUIERO_LEER)
                .collect { libros ->
                    // De mayor a menor interés (sin interés = al final)
                    binding.rvQuieroLeer.adapter =
                        AdaptadorQuiero(libros.sortedByDescending { it.interes ?: 0 })
                }
        }
    }
}