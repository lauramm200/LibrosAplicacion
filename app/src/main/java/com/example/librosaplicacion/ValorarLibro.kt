package com.example.librosaplicacion

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.librosaplicacion.databinding.ActivityValorarLibroBinding

class ValorarLibro : AppCompatActivity() {

    private lateinit var binding: ActivityValorarLibroBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityValorarLibroBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ajustarBordes(binding.root)

        // Datos del libro recibidos desde Leyendo
        binding.tvTitulo.text = intent.getStringExtra("titulo") ?: ""
        binding.tvAutor.text = intent.getStringExtra("autor") ?: ""

        // El texto del porcentaje sigue al deslizador
        mostrarPorcentaje(binding.sliderRecomendacion.value.toInt())
        binding.sliderRecomendacion.addOnChangeListener { _, valor, _ ->
            mostrarPorcentaje(valor.toInt())
        }

        binding.btnGuardarValoracion.setOnClickListener { guardar() }
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

        // Aquí se guardarán estrellas, porcentaje y resena en Room (siguiente fase)
        finish()
    }
}