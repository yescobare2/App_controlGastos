package umg.edu.programacion2.appgastos

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import umg.edu.programacion2.appgastos.viewmodel.GastoViewModel
import umg.edu.programacion2.appgastos.data.GastoEntity
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : ComponentActivity() {

    private val gastoViewModel: GastoViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background
            ) {
                PantallaPrincipal(viewModel = gastoViewModel)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaPrincipal(viewModel: GastoViewModel) {
    val listaGastos by viewModel.listaGastos.collectAsState()

    // Estados para los campos
    var titulo by remember { mutableStateOf("") }
    var monto by remember { mutableStateOf("") }

    // Lista de categorías predeterminadas solicitadas
    val categoriasDisponibles = listOf(
        "Vivienda y Hogar",
        "Alimentación",
        "Transporte",
        "Salud y cuidado personal",
        "Entretenimiento",
        "Finanzas",
        "Otros"
    )

    // Estado para controlar la categoría seleccionada y si el menú está abierto
    var categoriaSeleccionada by remember { mutableStateOf(categoriasDisponibles[0]) }
    var expanded by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Control de Gastos UMG") }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            // --- FORMULARIO PARA AGREGAR GASTO ---
            OutlinedTextField(
                value = titulo,
                onValueChange = { titulo = it },
                label = { Text("Descripción del gasto (ej. Almuerzo)") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = monto,
                onValueChange = { monto = it },
                label = { Text("Monto (ej. 45.00)") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))

            // --- MENÚ DESPLEGABLE (EXPOSED DROPDOWN) PARA CATEGORÍAS ---
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded },
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = categoriaSeleccionada,
                    onValueChange = {},
                    readOnly = true, // Evita que el usuario escriba libremente
                    label = { Text("Categoría") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                )

                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    categoriasDisponibles.forEach { categoriaItem ->
                        DropdownMenuItem(
                            text = { Text(categoriaItem) },
                            onClick = {
                                categoriaSeleccionada = categoriaItem
                                expanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = {
                    if (titulo.isNotBlank() && monto.isNotBlank()) {
                        val montoDouble = monto.toDoubleOrNull() ?: 0.0
                        val fechaActual = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())

                        // Guardamos con la categoría seleccionada del menú
                        viewModel.agregarGasto(titulo, montoDouble, categoriaSeleccionada, fechaActual)

                        // Limpiamos campos principales
                        titulo = ""
                        monto = ""
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Guardar Gasto")
            }

            Spacer(modifier = Modifier.height(24.dp))
            Divider()
            Spacer(modifier = Modifier.height(16.dp))

            // --- LISTA DE GASTOS REGISTRADOS ---
            Text(
                text = "Gastos Recientes",
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(listaGastos) { gasto ->
                    GastoItem(gasto = gasto, onDelete = {
                        viewModel.eliminarGasto(gasto)
                    })
                }
            }
        }
    }
}

@Composable
fun GastoItem(gasto: GastoEntity, onDelete: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = gasto.titulo, style = MaterialTheme.typography.bodyLarge)
                Text(text = "Categoría: ${gasto.categoria} • ${gasto.fecha}", style = MaterialTheme.typography.bodySmall)
            }
            Column(horizontalAlignment = androidx.compose.ui.Alignment.End) {
                Text(
                    text = "Q${gasto.monto}",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                TextButton(onClick = onDelete) {
                    Text("Eliminar", color = MaterialTheme.colorScheme.error)
                }
            }
        }
    }
}