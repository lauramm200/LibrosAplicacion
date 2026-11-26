package com.example.librosaplicacion

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.librosaplicacion.databinding.ItemLibroBusquedaBinding
import com.example.librosaplicacion.data.Libro

// Rellena cada fila de resultados (item_libro_busqueda.xml)
class AdaptadorBusqueda(private var libros: List<Libro>) :
    RecyclerView.Adapter<AdaptadorBusqueda.Holder>() {

    class Holder(val binding: ItemLibroBusquedaBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        Holder(ItemLibroBusquedaBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun getItemCount() = libros.size

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val libro = libros[position]
        holder.binding.tvTitulo.text = libro.titulo
        holder.binding.tvAutor.text = libro.autor
        holder.binding.tvPaginas.text = holder.itemView.context.getString(R.string.paginas_libro, libro.paginas)
    }

    // Cambia la lista mostrada (nuevos resultados de una búsqueda) y refresca la pantalla
    fun actualizar(nuevos: List<Libro>) {
        libros = nuevos
        notifyDataSetChanged()
    }
}