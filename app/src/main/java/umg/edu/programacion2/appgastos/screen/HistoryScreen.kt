package umg.edu.programacion2.appgastos.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import umg.edu.programacion2.appgastos.viewmodel.GastoViewModel

/**
 * PantallaHistorialGeneral: Despliega la lista completa de transacciones financieras.
 * Permite visualizar el detalle, categoría, fecha, monto con color distintivo
 * y la opción de eliminar registros de la base de datos.
 */
@Composable
fun PantallaHistorialGeneral(
    onBack: () -> Unit,
    viewModel: GastoViewModel
) {
    val listaMovimientos by viewModel.listaGastos.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Historial General", fontWeight = FontWeight.Bold, fontSize = 20.sp)
            TextButton(onClick = onBack) {
                Text("Volver")
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(listaMovimientos) { movimiento ->
                val esIngreso = movimiento.tipo == "INGRESO"
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = movimiento.titulo, fontWeight = FontWeight.Bold)
                            Text(text = "${movimiento.categoria} • ${movimiento.fecha}", style = MaterialTheme.typography.bodySmall)
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = "${if (esIngreso) "+" else "-"}Q${movimiento.monto}",
                                fontWeight = FontWeight.Bold,
                                color = if (esIngreso) Color(0xFF2E7D32) else Color(0xFFC62828)
                            )
                            TextButton(onClick = { viewModel.eliminarMovimiento(movimiento) }) {
                                Text("Borrar", color = MaterialTheme.colorScheme.error, fontSize = 12.sp)
                            }
                        }
                    }
                }
            }
        }
    }
}