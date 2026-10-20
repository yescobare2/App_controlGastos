package umg.edu.programacion2.appgastos

import java.time.LocalDateTime
import java.util.UUID

/**
 * Clase base que agrupa los atributos comunes (id, categoría, descripción, monto y fecha)
 * para evitar duplicación de código mediante herencia.
 */
open class MovimientoBase(
    val id: String = UUID.randomUUID().toString(),
    open val categoria: String,
    open val descripcion: String,
    open val monto: Double,
    val fechaHora: LocalDateTime = LocalDateTime.now()
)