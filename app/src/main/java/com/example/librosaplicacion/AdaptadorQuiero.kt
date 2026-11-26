package com.example.librosaplicacion

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.librosaplicacion.databinding.ItemLibroQuieroBinding
import com.example.librosaplicacion.data.Libro

// Rellena cada fila de la lista "Quiero leer" (item_libro_quiero.xml)
class AdaptadorQuiero(private val libros: List<Libro>) :
    RecyclerView.Adapter<AdaptadorQuiero.Holder>() {

    class Holder(val binding: ItemLibroQuieroBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        Holder(ItemLibroQuieroBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun getItemCount() = libros.size

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val libro = libros[position]
        holder.binding.tvPuesto.text = (position + 1).toString()
        holder.binding.tvTitulo.text = libro.titulo
        holder.binding.tvAutor.text = libro.autor

        // Si nadie lo recomendó, se oculta la línea en vez de dejarla vacía
        val recomendado = libro.recomendadoPor
        holder.binding.tvRecomendadoPor.visibility = if (recomendado == null) View.GONE else View.VISIBLE
        if (recomendado != null) {
            holder.binding.tvRecomendadoPor.text =
                holder.itemView.context.getString(R.string.recomendado_por, recomendado)
        }
    }
}