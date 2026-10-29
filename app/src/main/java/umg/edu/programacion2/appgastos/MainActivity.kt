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
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.input.PasswordVisualTransformation
import kotlinx.coroutines.launch
class MainActivity : ComponentActivity() {

    private val viewModel: GastoViewModel by viewModels()
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sessionManager = SessionManager(applicationContext)

        setContent {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background
            ) {
                val isLoggedIn by sessionManager.isLoggedIn.collectAsState(initial = false)
                var loginExitosoLocal by remember { mutableStateOf(false) }

                val sesionActiva = isLoggedIn || loginExitosoLocal

                if (!sesionActiva) {
                    PantallaLogin(
                        onLoginSuccess = {
                            // Guardamos en DataStore y actualizamos estado local
                            val scope =
                                kotlinx.coroutines.CoroutineScope(kotlinx.coroutines.Dispatchers.IO)
                            scope.launch {
                                sessionManager.setLoggedIn(true)
                            }
                            loginExitosoLocal = true
                        }
                    )
                } else {

                    PantallaPrincipal(viewModel = viewModel)
                }
            }
        }
    }

    // --- PANTALLA DE INICIO DE SESIÓN (Única vez) ---
    @Composable
    fun PantallaLogin(onLoginSuccess: () -> Unit) {
        var usuario by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }
        var errorMensaje by remember { mutableStateOf(false) }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Bienvenido a Finanzas UMG",
                style = MaterialTheme.typography.headlineMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Inicia sesión por única vez", style = MaterialTheme.typography.bodyMedium)

            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(
                value = usuario,
                onValueChange = { usuario = it },
                label = { Text("Usuario (ej. admin)") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Contraseña (ej. 1234)") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )

            if (errorMensaje) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Usuario o contraseña incorrectos",
                    color = MaterialTheme.colorScheme.error
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = {
                    // Credenciales sencillas de ejemplo (puedes cambiarlas a tu gusto)
                    if (usuario == "admin" && password == "1234") {
                        onLoginSuccess()
                    } else {
                        errorMensaje = true
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Entrar")
            }
        }
    }

// --- PANTALLA PRINCIPAL DE FINANZAS ---

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun PantallaPrincipal(viewModel: GastoViewModel) {
        val listaMovimientos by viewModel.listaGastos.collectAsState()

        var titulo by remember { mutableStateOf("") }
        var monto by remember { mutableStateOf("") }
        var tipoSeleccionado by remember { mutableStateOf("GASTO") }

        val categoriasGastos = listOf(
            "Vivienda y Hogar", "Alimentación", "Transporte",
            "Salud y cuidado personal", "Entretenimiento", "Finanzas", "Otros"
        )
        val categoriasIngresos = listOf("Salario", "Emprendimiento", "Bonos", "Otros")

        val categoriasDisponibles =
            if (tipoSeleccionado == "GASTO") categoriasGastos else categoriasIngresos
        var categoriaSeleccionada by remember { mutableStateOf(categoriasDisponibles[0]) }

        LaunchedEffect(tipoSeleccionado) {
            categoriaSeleccionada =
                if (tipoSeleccionado == "GASTO") categoriasGastos[0] else categoriasIngresos[0]
        }

        var expanded by remember { mutableStateOf(false) }

        // Cálculo básico del balance
        val totalIngresos = listaMovimientos.filter { it.tipo == "INGRESO" }.sumOf { it.monto }
        val totalGastos = listaMovimientos.filter { it.tipo == "GASTO" }.sumOf { it.monto }
        val balanceFinal = totalIngresos - totalGastos

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Título simple
            Text(text = "Control Financiero", style = MaterialTheme.typography.headlineSmall)
            Spacer(modifier = Modifier.height(8.dp))

            // Balance simple en texto
            Text(text = "Ingresos: Q$totalIngresos")
            Text(text = "Gastos: Q$totalGastos")
            Text(
                text = "Balance Total: Q$balanceFinal",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Selector básico de tipo (Gasto / Ingreso)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(onClick = { tipoSeleccionado = "GASTO" }) {
                    Text("Gasto")
                }
                Button(onClick = { tipoSeleccionado = "INGRESO" }) {
                    Text("Ingreso")
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Tipo actual: $tipoSeleccionado",
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(12.dp))

            // Campos de texto simples
            OutlinedTextField(
                value = titulo,
                onValueChange = { titulo = it },
                label = { Text("Descripción") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = monto,
                onValueChange = { monto = it },
                label = { Text("Monto") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))

            // Menú desplegable simple de categorías
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded },
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = categoriaSeleccionada,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Categoría") },
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

            // Estado para controlar mensajes de alerta o validación
            var mostrarErrorMonto by remember { mutableStateOf(false) }
            // Botón de guardar simple
            Button(
                onClick = {
            // Validamos que el monto no esté vacío y sea un número válido
            val montoDouble = monto.toDoubleOrNull()

            if (montoDouble == null || montoDouble <= 0.0) {
                // Si el monto no es válido, mostramos alerta
                mostrarErrorMonto = true
            } else {
                // Si el monto es válido, procedemos a guardar
                mostrarErrorMonto = false

                // Si la descripción está vacía, usamos la categoría por defecto
                val tituloFinal = if (titulo.isBlank()) categoriaSeleccionada else titulo.trim()
                val fechaActual = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())

                viewModel.agregarMovimiento(
                    titulo = tituloFinal,
                    monto = montoDouble,
                    categoria = categoriaSeleccionada,
                    fecha = fechaActual,
                    tipo = tipoSeleccionado
                )

                // Limpiamos los campos después de guardar con éxito
                titulo = ""
                monto = ""
            }
        },
        modifier = Modifier.fillMaxWidth()
        ) {
            Text("Guardar")
        }

// Mensaje de error visual si el monto es incorrecto
        if (mostrarErrorMonto) {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Por favor, ingresa un monto válido mayor a 0",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }

            Spacer(modifier = Modifier.height(16.dp))
            Divider()
            Spacer(modifier = Modifier.height(8.dp))

            // Lista simple de historial
            Text(text = "Historial:", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                items(listaMovimientos) { movimiento ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(text = "${movimiento.titulo} (${movimiento.tipo})")
                            Text(
                                text = "${movimiento.categoria} - ${movimiento.fecha}",
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text(text = "Q${movimiento.monto}")
                            TextButton(onClick = { viewModel.eliminarMovimiento(movimiento) }) {
                                Text("Borrar")
                            }
                        }
                    }
                }
            }
        }
    }
}