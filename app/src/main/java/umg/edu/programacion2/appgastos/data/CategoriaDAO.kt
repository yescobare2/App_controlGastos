package umg.edu.programacion2.appgastos.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoriaDao {
    @Query("SELECT * FROM categorias WHERE tipo = :tipo")
    fun obtenerCategoriasPorTipo(tipo: String): Flow<List<CategoriaEntity>>

    @Insert
    suspend fun insertarCategoria(categoria: CategoriaEntity)
}