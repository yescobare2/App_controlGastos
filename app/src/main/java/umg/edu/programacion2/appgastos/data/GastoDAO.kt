package umg.edu.programacion2.appgastos.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface GastoDao {

    // Obtener todos los gastos en tiempo real usando Flow
    @Query("SELECT * FROM movimientos ORDER BY id DESC")
    fun obtenerTodosLosMovimientos(): Flow<List<GastoEntity>>

    // Insertar un nuevo gasto
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarMovimiento(movimiento: GastoEntity) : Long

    // Borrar un gasto
    @Delete
    suspend fun eliminarMovimiento(moviento: GastoEntity) : Int

    // Métodos para categorías personalizadas
    @Query("SELECT * FROM categorias WHERE tipo = :tipo ORDER BY id DESC")
    fun obtenerCategoriasPorTipo(tipo: String): Flow<List<CategoriaEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarCategoria(categoria: CategoriaEntity): Long
}