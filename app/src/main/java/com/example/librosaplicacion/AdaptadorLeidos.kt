package com.example.librosaplicacion

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.librosaplicacion.databinding.ItemLibroLeidoBinding
import com.example.librosaplicacion.data.Libro

// Rellena cada celda de la cuadrícula (item_libro_leido.xml) con los datos de un libro
class AdaptadorLeidos(
    private val libros: List<Libro>,
    private val alEditar: (Libro) -> Unit
) : RecyclerView.Adapter<AdaptadorLeidos.Holder>() {

    // Guarda las vistas de una celda para no buscarlas cada vez que se reutiliza
    class Holder(val binding: ItemLibroLeidoBinding) : RecyclerView.ViewHolder(binding.root)

    // Crea una celda vacía a partir del XML del item
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        Holder(ItemLibroLeidoBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    // Cuántas celdas hay en total
    override fun getItemCount() = libros.size

    // Rellena la celda de la posición indicada con su libro
    override fun onBindViewHolder(holder: Holder, position: Int) {
        val libro = libros[position]
        holder.binding.ivPortada.cargarPortada(libro.portada)
        holder.binding.tvTitulo.text = libro.titulo
        holder.binding.tvPorcentaje.text = holder.itemView.context
            .getString(R.string.porcentaje, libro.porcentaje ?: 0)
        holder.binding.btnEditar.setOnClickListener { alEditar(libro) }
    }
}

