package com.example.librosaplicacion

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.librosaplicacion.data.LibrosXmlParser
import com.example.librosaplicacion.databinding.ActivityPerfilUserBinding
import com.example.librosaplicacion.databinding.ItemBarraMesBinding
import com.example.librosaplicacion.data.Estado
import com.example.librosaplicacion.data.Libro
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class PerfilUser : AppCompatActivity() {

    private lateinit var binding: ActivityPerfilUserBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityPerfilUserBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ajustarBordes(binding.root)

        // Cabecera
        val usuario = intent.getStringExtra("usuario") ?: ""
        binding.tvSaludo.text = getString(R.string.saludo, usuario)
        binding.tvLectorDesde.text = getString(R.string.lector_desde, Calendar.getInstance().get(Calendar.YEAR).toString())

        // Contadores por estado
        val libros = LibrosXmlParser.cargar(this)
        val leidos = libros.filter { it.estado == Estado.LEIDO }
        binding.tvNumLeidos.text = leidos.size.toString()
        binding.tvNumLeyendo.text = libros.count { it.estado == Estado.LEYENDO }.toString()
        binding.tvNumPorLeer.text = libros.count { it.estado == Estado.QUIERO_LEER }.toString()

        // Gráfica: los 6 últimos meses, el actual en la última barra
        val barras = listOf(binding.barra1, binding.barra2, binding.barra3,
            binding.barra4, binding.barra5, binding.barra6)
        val meses = List(6) { i -> Calendar.getInstance().apply { add(Calendar.MONTH, i - 5) } }
        val paginas = meses.map { paginasDelMes(leidos, it) }
        val maximo = maxOf(paginas.max(), 1)   // mínimo 1 para no dividir entre 0

        for (i in 0 until 6) mostrarBarra(barras[i], meses[i], paginas[i], maximo)
        binding.tvMedia.text = getString(R.string.media_mensual, paginas.sum() / 6)
    }

    // Suma las páginas de los libros terminados en el mes indicado
    private fun paginasDelMes(libros: List<Libro>, mes: Calendar): Int =
        libros.filter { libro ->
            val fin = libro.fechaFin ?: return@filter false
            val c = Calendar.getInstance().apply { timeInMillis = fin }
            c.get(Calendar.YEAR) == mes.get(Calendar.YEAR) &&
                    c.get(Calendar.MONTH) == mes.get(Calendar.MONTH)
        }.sumOf { it.paginas }

    // Rellena una columna: valor, mes y altura proporcional al máximo
    private fun mostrarBarra(barra: ItemBarraMesBinding, mes: Calendar, paginas: Int, maximo: Int) {
        barra.tvValor.text = paginas.toString()
        barra.tvMes.text = SimpleDateFormat("MMM", Locale("es", "ES")).format(mes.time)
            .trimEnd('.').replaceFirstChar { it.uppercase() }

        val alturaDp = maxOf(4, paginas * 120 / maximo)       // entre 4dp y 120dp
        val params = barra.vBarra.layoutParams
        params.height = (alturaDp * resources.displayMetrics.density).toInt()   // dp -> píxeles
        barra.vBarra.layoutParams = params
    }
}