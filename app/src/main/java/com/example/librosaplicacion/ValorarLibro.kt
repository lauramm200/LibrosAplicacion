package com.example.librosaplicacion

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.librosaplicacion.data.AppDatabase
import com.example.librosaplicacion.data.Estado
import com.example.librosaplicacion.data.Libro
import com.example.librosaplicacion.databinding.ActivityValorarLibroBinding
import kotlinx.coroutines.launch

class ValorarLibro : AppCompatActivity() {

    private lateinit var binding: ActivityValorarLibroBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityValorarLibroBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ajustarBordes(binding.root)

        // Datos del libro recibidos desde Leyendo, Búsqueda o Leídos
        binding.tvTitulo.text = intent.getStringExtra("titulo") ?: ""
        binding.tvAutor.text = intent.getStringExtra("autor") ?: ""

        // El texto del porcentaje sigue al deslizador
        mostrarPorcentaje(binding.sliderRecomendacion.value.toInt())
        binding.sliderRecomendacion.addOnChangeListener { _, valor, _ ->
            mostrarPorcentaje(valor.toInt())
        }

        binding.btnGuardarValoracion.setOnClickListener { guardar() }

        // Si el libro ya estaba valorado (edición), se rellenan los campos con sus datos
        val volumeId = intent.getStringExtra("volumeId")
        if (volumeId != null) {
            lifecycleScope.launch {
                val libro = AppDatabase.obtener(this@ValorarLibro).libroDao().obtener(volumeId)
                val estrellas = libro?.estrellas
                if (libro != null && estrellas != null) {
                    binding.ratingBar.rating = estrellas.toFloat()
                    binding.sliderRecomendacion.value = (libro.porcentaje ?: 50).toFloat()
                    binding.etResena.setText(libro.resena)
                }
            }
        }
    }

    private fun mostrarPorcentaje(valor: Int) {
        binding.tvValorRecomendacion.text = getString(R.string.porcentaje, valor)
    }

    private fun guardar() {
        val estrellas = binding.ratingBar.rating.toInt()
        if (estrellas == 0) {
            Toast.makeText(this, R.string.error_estrellas, Toast.LENGTH_SHORT).show()
            return
        }
        val porcentaje = binding.sliderRecomendacion.value.toInt()
        val resena = binding.etResena.text.toString().trim().ifEmpty { null }
        val volumeId = intent.getStringExtra("volumeId") ?: return

        lifecycleScope.launch {
            val dao = AppDatabase.obtener(this@ValorarLibro).libroDao()

            // Si ya estaba en Room (venía de Leyendo o Leídos) se conservan sus datos; si viene de la búsqueda, se crea
            val base = dao.obtener(volumeId) ?: Libro(
                volumeId = volumeId,
                titulo = intent.getStringExtra("titulo") ?: "",
                autor = intent.getStringExtra("autor") ?: "",
                paginas = intent.getIntExtra("paginas", 0),
                portada = intent.getStringExtra("portada")
            )

            dao.guardar(
                base.copy(
                    estado = Estado.LEIDO,
                    fechaFin = base.fechaFin ?: System.currentTimeMillis(),
                    estrellas = estrellas,
                    porcentaje = porcentaje,
                    resena = resena
                )
            )
            finish()
        }
    }
}