package umg.edu.programacion2.appgastos

class GestorFinanzas {
    // Lista mutable privada que almacena todos los movimientos (tanto ingresos como gastos).
    // Utiliza 'MovimientoBase' gracias a la herencia, lo que permite guardar objetos Ingreso y Gasto juntos.
    private val historial = mutableListOf<MovimientoBase>()

    /**
     * Registra un nuevo ingreso en el historial general.
     */
    fun registrarIngreso(ingreso: Ingreso) {
        historial.add(ingreso)
    }

    /**
     * Registra un nuevo gasto en el historial general.
     */
    fun registrarGasto(gasto: Gasto) {
        historial.add(gasto)
    }

    /**
     * Devuelve el listado completo de movimientos ordenados del más reciente al más antiguo,
     * basándose en la fecha y hora automática que genera el sistema.
     */
    fun obtenerHistorial(): List<MovimientoBase> {
        return historial.sortedByDescending { it.fechaHora }
    }

    /**
     * Calcula de forma automática el balance general de las finanzas:
     * - Recorre cada movimiento en la lista.
     * - Si el movimiento es una instancia de 'Ingreso', suma su monto al balance.
     * - Si el movimiento es una instancia de 'Gasto', resta su monto al balance.
     */
    fun calcularBalance(): Double {
        var balance = 0.0
        for (mov in historial) {
            when (mov) {
                is Ingreso -> balance += mov.monto // Suma automática si es ingreso
                is Gasto -> balance -= mov.monto     // Resta automática si es gasto
            }
        }
        return balance
    }
}