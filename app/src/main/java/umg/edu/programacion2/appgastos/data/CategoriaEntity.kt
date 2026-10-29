package umg.edu.programacion2.appgastos.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "categorias_tabla")
data class CategoriaEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nombre: String,
    val tipo: String // "INGRESO" o "GASTO"
)