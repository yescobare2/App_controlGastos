package umg.edu.programacion2.appgastos.data

import androidx.room.Entity
import androidx.room.PrimaryKey

// CLASE BASE (Principio de Herencia y abstracción para evitar código repetitivo)
abstract class TransaccionBase {
    abstract val id: Long
    abstract val titulo: String
    abstract val monto: Double
    abstract val categoria: String
    abstract val fecha: String
    abstract val tipo: String
}

// ENTIDAD ROOM QUE HEREDA DE LA CLASE BASE
@Entity(tableName = "movimientos")
data class GastoEntity(
    @PrimaryKey(autoGenerate = true)
    override val id: Long = 0L,
    override val titulo: String,
    override val monto: Double,
    override val categoria: String,
    override val fecha: String,
    override val tipo: String
) : TransaccionBase()