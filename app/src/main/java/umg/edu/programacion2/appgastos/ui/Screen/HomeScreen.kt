package umg.edu.programacion2.appgastos.ui.Screen

import android.R.attr.bottom
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import umg.edu.programacion2.appgastos.ui.theme.*
import umg.edu.programacion2.appgastos.ui.theme.BackgroundGray
import umg.edu.programacion2.appgastos.ui.theme.CardWhite
import umg.edu.programacion2.appgastos.ui.theme.DeepNavy
import umg.edu.programacion2.appgastos.ui.theme.PrimaryEmerald
import umg.edu.programacion2.appgastos.ui.theme.TextMuted
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
    viewModel: GastoViewModel,
    modoOscuro: Boolean,
    onCambiarTema: (Boolean) -> Unit
)

{
    val listaMovimientos by viewModel.listaGastos.collectAsState()

    val totalIngresos = listaMovimientos.filter { it.tipo == "INGRESO" }.sumOf { it.monto }
    val totalGastos = listaMovimientos.filter { it.tipo == "GASTO" }.sumOf { it.monto }
    val balanceFinal = totalIngresos - totalGastos

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "GastoSmart",
                    fontSize = 27.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground,
                    letterSpacing = 0.5.sp
                )

                Text(
                    text = "Control financiero personal",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.65f)
                )
            }

            // Interruptor de modo oscuro
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = if (modoOscuro) "Oscuro" else "Claro",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onBackground
                )

                Switch(
                    checked = modoOscuro,
                    onCheckedChange = { onCambiarTema(it) }
                )
            }
        }


        Spacer(modifier = Modifier.height(32.dp))

        // TARJETA DE BALANCE PRINCIPAL (Estilo Neumorfismo / Minimalista)
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF174A70)),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "BALANCE TOTAL",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White.copy(alpha = 0.8f),
                    letterSpacing = 1.5.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Q %.2f".format(balanceFinal),
                    fontSize = 36.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = if (balanceFinal >= 0) Color.White else AccentRed
                )

                // Pequeña descripción del estado del balance.
                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = if (balanceFinal < 0)
                        "Tus gastos superan a tus ingresos"
                    else
                        "Este es tu balance actual",
                    fontSize = 13.sp,
                    color = Color.White.copy(alpha = 0.85f)
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Tarjetas de ingresos y gastos
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            // Tarjeta de ingresos
            Card(
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.cardElevation(1.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "↑",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = PrimaryEmerald
                        )

                        Spacer(modifier = Modifier.width(6.dp))

                        Text(
                            text = "Ingresos",
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.65f)
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "Q %.2f".format(totalIngresos),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = PrimaryEmerald
                    )
                }
            }

            // Tarjeta de gastos
            Card(
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.cardElevation(1.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "↓",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = AccentRed
                        )
                        Spacer(modifier = Modifier.width(6.dp))

                        Text(
                            text = "Gastos",
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.65f)
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "Q %.2f".format(totalGastos),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = AccentRed
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

            // BOTONES DE ACCIÓN RÁPIDA (Ingreso / Gasto)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Button(
                    onClick = onNavigateToIngreso,
                    modifier = Modifier
                        .weight(1f)
                        .height(54.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryEmerald),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
                ) {
                    Text(
                        text = "+ Ingreso",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

                Button(
                    onClick = onNavigateToGasto,
                    modifier = Modifier
                        .weight(1f)
                        .height(54.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = AccentRed),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
                ) {
                    Text(
                        text = "- Gasto",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // BOTÓN DE HISTORIAL COMPLETO
            OutlinedButton(
                onClick = onNavigateToHistorial,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.onBackground)
            ) {
                Text(
                    text = "Ver historial completo",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }