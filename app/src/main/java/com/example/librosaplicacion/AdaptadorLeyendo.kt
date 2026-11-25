package com.example.librosaplicacion

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.librosaplicacion.databinding.ItemLibroLeyendoBinding
import com.example.librosaplicacion.model.Libro

// Rellena cada fila de "Otros en curso" (item_libro_leyendo.xml)
class AdaptadorLeyendo(
    private val libros: List<Libro>,
    private val alTerminar: (Libro) -> Unit
) : RecyclerView.Adapter<AdaptadorLeyendo.Holder>() {

    class Holder(val binding: ItemLibroLeyendoBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        Holder(ItemLibroLeyendoBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun getItemCount() = libros.size

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val libro = libros[position]
        holder.binding.tvTitulo.text = libro.titulo
        holder.binding.tvInicio.text = holder.itemView.context
            .getString(R.string.inicio_lectura, formatearFecha(libro.fechaInicio))
        holder.binding.btnTerminar.setOnClickListener { alTerminar(libro) }
    }
}