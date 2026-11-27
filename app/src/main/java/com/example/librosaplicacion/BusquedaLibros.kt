package com.example.librosaplicacion

import android.os.Bundle
import android.util.Log
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.librosaplicacion.api.RetrofitClient
import com.example.librosaplicacion.api.aLibro
import com.example.librosaplicacion.data.AppDatabase
import com.example.librosaplicacion.data.Estado
import com.example.librosaplicacion.data.Libro
import com.example.librosaplicacion.databinding.ActivityBusquedaLibrosBinding
import com.example.librosaplicacion.databinding.DialogQuieroLeerBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.launch

class BusquedaLibros : AppCompatActivity() {

    private lateinit var binding: ActivityBusquedaLibrosBinding
    private lateinit var adaptador: AdaptadorBusqueda
    private val dao by lazy { AppDatabase.obtener(this).libroDao() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityBusquedaLibrosBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ajustarBordes(binding.root)

        adaptador = AdaptadorBusqueda(
            libros = emptyList(),
            alQuerer = { alPulsarQuiero(it) },
            alLeido = { abrirValorar(it) }
        )
        binding.rvResultados.layoutManager = LinearLayoutManager(this)
        binding.rvResultados.adapter = adaptador

        binding.etBuscar.setOnEditorActionListener { _, accion, _ ->
            if (accion == EditorInfo.IME_ACTION_SEARCH) {
                buscar()
                true
            } else false
        }

        // Al cambiar de filtro se repite la búsqueda
        binding.chipGroupFiltros.setOnCheckedStateChangeListener { _, _ -> buscar() }
    }

    private fun buscar() {
        val texto = binding.etBuscar.text.toString().trim()

        // Categoría del chip seleccionado (null = "Todos"); Google Books las clasifica con nombres en inglés
        val categoria = when (binding.chipGroupFiltros.checkedChipId) {
            R.id.chipNovela -> "fiction"
            R.id.chipFantasia -> "fantasy"
            R.id.chipHistoria -> "history"
            else -> null
        }

        // "texto subject:categoria"; si falta una de las dos partes, solo se envía la otra
        val consulta = listOfNotNull(texto.ifEmpty { null }, categoria?.let { "subject:$it" })
            .joinToString(" ")
        if (consulta.isEmpty()) return

        lifecycleScope.launch {
            try {
                val respuesta = RetrofitClient.api.buscar(consulta)
                adaptador.actualizar(respuesta.items.orEmpty().map { it.aLibro() })
            } catch (e: Exception) {
                Log.e("BusquedaLibros", "Error al buscar", e)
                Toast.makeText(this@BusquedaLibros, R.string.error_busqueda, Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Si el libro ya está en la biblioteca avisa; si no, abre el diálogo
    private fun alPulsarQuiero(libro: Libro) {
        lifecycleScope.launch {
            if (dao.obtener(libro.volumeId) != null) {
                Toast.makeText(this@BusquedaLibros, R.string.libro_ya_guardado, Toast.LENGTH_SHORT).show()
            } else {
                mostrarDialogoQuiero(libro)
            }
        }
    }

    // Diálogo con "Recomendado por" e interés; al guardar, inserta el libro en Room
    private fun mostrarDialogoQuiero(libro: Libro) {
        val dialogo = DialogQuieroLeerBinding.inflate(layoutInflater)
        dialogo.tvLibroDialogo.text = libro.titulo

        val ventana = MaterialAlertDialogBuilder(this).setView(dialogo.root).create()

        dialogo.btnCancelarDialogo.setOnClickListener { ventana.dismiss() }
        dialogo.btnGuardarDialogo.setOnClickListener {
            val recomendadoPor = dialogo.etRecomendadoPor.text.toString().trim().ifEmpty { null }
            val interes = dialogo.ratingInteres.rating.toInt()

            lifecycleScope.launch {
                dao.guardar(libro.copy(estado = Estado.QUIERO_LEER, recomendadoPor = recomendadoPor, interes = interes))
                Toast.makeText(this@BusquedaLibros, R.string.libro_guardado, Toast.LENGTH_SHORT).show()
                ventana.dismiss()
            }
        }
        ventana.show()
    }
}