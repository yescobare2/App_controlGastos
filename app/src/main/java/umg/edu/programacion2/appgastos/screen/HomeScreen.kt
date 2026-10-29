package umg.edu.programacion2.appgastos.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
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
 * PantallaHome: Dashboard principal de GastosSmart.
 * Muestra la tarjeta de balance general calculada en tiempo real
 * y los botones circulares para elegir entre registrar un Ingreso o un Gasto.
 */
@Composable
fun PantallaHome(
    onNavigateToIngreso: () -> Unit,
    onNavigateToGasto: () -> Unit,
    onNavigateToHistorial: () -> Unit,
    viewModel: GastoViewModel
) {
    val listaMovimientos by viewModel.listaGastos.collectAsState()

    val totalIngresos = listaMovimientos.filter { it.tipo == "INGRESO" }.sumOf { it.monto }
    val totalGastos = listaMovimientos.filter { it.tipo == "GASTO" }.sumOf { it.monto }
    val balanceFinal = totalIngresos - totalGastos

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "BIENVENIDO A GASTOSMART", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "Sesión activa (Admin)", style = MaterialTheme.typography.bodyMedium)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Card(
                modifier = Modifier
                    .weight(1.5f)
                    .height(110.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(12.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(text = "Balance General", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = "Q$balanceFinal", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                }
            }

            Button(
                onClick = onNavigateToHistorial,
                modifier = Modifier
                    .weight(1f)
                    .height(110.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(text = "VER HISTORIAL", fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Text(text = "¿QUÉ DESEA INGRESAR?", fontWeight = FontWeight.Bold, fontSize = 16.sp)

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = onNavigateToIngreso,
                shape = CircleShape,
                modifier = Modifier.size(110.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32))
            ) {
                Text(text = "INGRESO", color = Color.White, fontWeight = FontWeight.Bold)
            }

            Button(
                onClick = onNavigateToGasto,
                shape = CircleShape,
                modifier = Modifier.size(110.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFC62828))
            ) {
                Text(text = "GASTO", color = Color.White, fontWeight = FontWeight.Bold)
            }
        }
    }
}