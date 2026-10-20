package umg.edu.programacion2.appgastos.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "gastos")
data class GastoEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val titulo: String,
    val monto: Double,
    val categoria: String,
    val fecha: String
)