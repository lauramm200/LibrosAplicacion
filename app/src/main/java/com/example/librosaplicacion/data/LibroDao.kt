package com.example.librosaplicacion.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

data class PaginasMes(val mes: String, val paginas: Int)

@Dao
interface LibroDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun guardar(libro: Libro)

    @Query("SELECT * FROM libros WHERE usuario = :usuario AND estado = :estado ORDER BY interes DESC, fechaInicio DESC, fechaFin DESC")
    fun porEstado(usuario: String, estado: Estado): Flow<List<Libro>>

    @Query("SELECT strftime('%Y-%m', fechaFin / 1000, 'unixepoch') AS mes, SUM(paginas) AS paginas FROM libros WHERE usuario = :usuario AND estado = 'LEIDO' GROUP BY mes ORDER BY mes")
    fun paginasPorMes(usuario: String): Flow<List<PaginasMes>>

    @Query("SELECT * FROM libros WHERE volumeId = :volumeId AND usuario = :usuario")
    suspend fun obtener(volumeId: String, usuario: String): Libro?

    @Query("SELECT * FROM libros WHERE usuario = :usuario")
    fun todos(usuario: String): Flow<List<Libro>>

}