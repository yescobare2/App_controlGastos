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
    @Query("SELECT * FROM gastos ORDER BY id DESC")
    fun obtenerTodosLosGastos(): Flow<List<GastoEntity>>

    // Insertar un nuevo gasto
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarGasto(gasto: GastoEntity)

    // Borrar un gasto
    @Delete
    suspend fun eliminarGasto(gasto: GastoEntity)
}