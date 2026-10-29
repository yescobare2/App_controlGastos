package umg.edu.programacion2.appgastos.viewmodel
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import umg.edu.programacion2.appgastos.data.AppDatabase
import umg.edu.programacion2.appgastos.data.CategoriaEntity
import umg.edu.programacion2.appgastos.data.GastoEntity

class GastoViewModel(application: Application) : AndroidViewModel(application) {

    // Instancia del DAO a través de la Base de Datos
    private val dao = AppDatabase.getDatabase(application).gastoDao()

    // Lista de gastos expuesta como StateFlow para que la interfaz se entere en tiempo real
    val listaGastos: StateFlow<List<GastoEntity>> = dao.obtenerTodosLosMovimientos()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // Función para agregar un nuevo gasto
    fun agregarMovimiento(titulo: String, monto: Double, categoria: String, fecha: String, tipo: String) {
        viewModelScope.launch {
            val nuevoMovimiento = GastoEntity(
                titulo = titulo,
                monto = monto,
                categoria = categoria,
                fecha = fecha,
                tipo = tipo
            )
            dao.insertarMovimiento(nuevoMovimiento)
        }
    }

    // Función para eliminar un gasto
    fun eliminarMovimiento(movimiento: GastoEntity) {
        viewModelScope.launch {
            dao.eliminarMovimiento(movimiento)
        }
    }

    fun obtenerCategoriasPersonalizadas(tipo: String): Flow<List<CategoriaEntity>> {
        return dao.obtenerCategoriasPorTipo(tipo)
    }

    fun agregarNuevaCategoria(nombre: String, tipo: String) {
        viewModelScope.launch {
            dao.insertarCategoria(CategoriaEntity(nombre = nombre, tipo = tipo))
        }
    }
}