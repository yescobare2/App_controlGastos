package umg.edu.programacion2.appgastos.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import umg.edu.programacion2.appgastos.viewmodel.GastoViewModel
import java.text.SimpleDateFormat
import java.util.*

/**
 * PantallaFormulario: Gestiona el registro de ingresos o gastos.
 * Valida montos obligatorios, permite descripciones y ofrece la opción
 * de crear y persistir nuevas categorías personalizadas.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaFormulario(
    tipoMovimiento: String,
    onBack: () -> Unit,
    viewModel: GastoViewModel
) {
    var descripcion by remember { mutableStateOf("") }
    var monto by remember { mutableStateOf("") }
    var mostrarErrorMonto by remember { mutableStateOf(false) }

    // ¡IMPORTANTE! Usar siempre comillas dobles (" ") para listas de Strings
    val baseIngresos = listOf("Salario", "Emprendimiento", "Bonos", "Otro (Crear nueva...)")
    val baseGastos = listOf("Vivienda y Hogar", "Alimentación", "Transporte", "Salud", "Entretenimiento", "Otro (Crear nueva...)")

    val categoriasPersonalizadas by viewModel.obtenerCategoriasPersonalizadas(tipoMovimiento).collectAsState(initial = emptyList())

    val categoriasDisponibles = remember(categoriasPersonalizadas, tipoMovimiento) {
        val base = if (tipoMovimiento == "INGRESO") baseIngresos else baseGastos
        val customNames = categoriasPersonalizadas.map { it.nombre }
        customNames + base
    }

    var categoriaSeleccionada by remember { mutableStateOf("") }
    LaunchedEffect(categoriasDisponibles) {
        if (categoriaSeleccionada.isBlank() && categoriasDisponibles.isNotEmpty()) {
            categoriaSeleccionada = categoriasDisponibles[0]
        }
    }

    var expanded by remember { mutableStateOf(false) }
    var mostrarDialogoNuevaCat by remember { mutableStateOf(false) }
    var nombreNuevaCat by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = if (tipoMovimiento == "INGRESO") "REGISTRAR INGRESO" else "REGISTRAR GASTO",
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp
        )

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = descripcion,
            onValueChange = { descripcion = it },
            label = { Text("DESCRIPCIÓN (Opcional)") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = monto,
            onValueChange = { monto = it },
            label = { Text("MONTO") },
            modifier = Modifier.fillMaxWidth()
        )

        if (mostrarErrorMonto) {
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Ingrese un monto válido mayor a 0", color = MaterialTheme.colorScheme.error, fontSize = 12.sp)
        }

        Spacer(modifier = Modifier.height(12.dp))

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded },
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = categoriaSeleccionada,
                onValueChange = {},
                readOnly = true,
                label = { Text("TIPO / CATEGORÍA") },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                categoriasDisponibles.forEach { cat ->
                    DropdownMenuItem(
                        text = { Text(cat) },
                        onClick = {
                            if (cat.contains("Crear nueva")) {
                                expanded = false
                                mostrarDialogoNuevaCat = true
                            } else {
                                categoriaSeleccionada = cat
                                expanded = false
                            }
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                val montoDouble = monto.toDoubleOrNull()
                if (montoDouble == null || montoDouble <= 0.0) {
                    mostrarErrorMonto = true
                } else {
                    mostrarErrorMonto = false
                    val descripcionFinal = if (descripcion.isBlank()) categoriaSeleccionada else descripcion.trim()
                    val fechaActual = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())

                    viewModel.agregarMovimiento(
                        titulo = descripcionFinal,
                        monto = montoDouble,
                        categoria = categoriaSeleccionada,
                        fecha = fechaActual,
                        tipo = tipoMovimiento
                    )
                    onBack()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("GUARDAR")
        }

        Spacer(modifier = Modifier.height(8.dp))

        TextButton(onClick = onBack) {
            Text("Cancelar / Volver")
        }
    }

    if (mostrarDialogoNuevaCat) {
        AlertDialog(
            onDismissRequest = { mostrarDialogoNuevaCat = false },
            title = { Text("Nueva Categoría Personalizada") },
            text = {
                OutlinedTextField(
                    value = nombreNuevaCat,
                    onValueChange = { nombreNuevaCat = it },
                    label = { Text("Nombre (ej. Cine, Mascotas)") }
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        if (nombreNuevaCat.isNotBlank()) {
                            viewModel.agregarNuevaCategoria(nombreNuevaCat.trim(), tipoMovimiento)
                            categoriaSeleccionada = nombreNuevaCat.trim()
                            nombreNuevaCat = ""
                            mostrarDialogoNuevaCat = false
                        }
                    }
                ) {
                    Text("Guardar Categoría")
                }
            },
            dismissButton = {
                TextButton(onClick = { mostrarDialogoNuevaCat = false }) {
                    Text("Cancelar")
                }
            }
        )
    }
}