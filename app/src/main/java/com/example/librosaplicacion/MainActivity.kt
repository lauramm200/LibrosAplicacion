package com.example.librosaplicacion

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.librosaplicacion.databinding.ActivityBienvenidaBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBienvenidaBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityBienvenidaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Margen para que el contenido no quede bajo la barra de estado ni la de navegación
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val barras = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(barras.left, barras.top, barras.right, barras.bottom)
            insets
        }

        // Nombre recibido desde Login
        val usuario = intent.getStringExtra("usuario") ?: ""
        binding.tvSaludo.text = getString(R.string.saludo, usuario)

        // Cada vista pulsable abre su pantalla
        binding.cardSeguirLeyendo.setOnClickListener { abrir(EstoyLeyendo::class.java) }
        binding.btnBuscar.setOnClickListener { abrir(BusquedaLibros::class.java) }
        binding.btnLeidos.setOnClickListener { abrir(LeidosLibros::class.java) }
        binding.btnLeyendo.setOnClickListener { abrir(EstoyLeyendo::class.java) }
        binding.btnQuiero.setOnClickListener { abrir(QuieroLeer::class.java) }
        binding.btnPerfil.setOnClickListener {
            startActivity(Intent(this, PerfilUser::class.java).putExtra("usuario", usuario))
        }    }

    // Función única para abrir cualquier pantalla (evita repetir el Intent 6 veces)
    private fun abrir(destino: Class<*>) {
        startActivity(Intent(this, destino))
    }
}