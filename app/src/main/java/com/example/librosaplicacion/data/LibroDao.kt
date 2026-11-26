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

    @Query("SELECT * FROM libros WHERE estado = :estado ORDER BY interes DESC, fechaInicio DESC, fechaFin DESC")
    fun porEstado(estado: Estado): Flow<List<Libro>>

    @Query("SELECT strftime('%Y-%m', fechaFin / 1000, 'unixepoch') AS mes, SUM(paginas) AS paginas FROM libros WHERE estado = 'LEIDO' GROUP BY mes ORDER BY mes")
    fun paginasPorMes(): Flow<List<PaginasMes>>

    @Query("SELECT * FROM libros WHERE volumeId = :volumeId")
    suspend fun obtener(volumeId: String): Libro?
}