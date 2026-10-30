
package umg.edu.programacion2.appgastos.ui.Screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
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
import java.text.SimpleDateFormat
import java.util.*

/*
 * PantallaFormulario:
 * Permite registrar ingresos y gastos.
 * Incluye validación del monto, selección de categorías
 * y creación de categorías personalizadas.
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaFormulario(
    tipoMovimiento: String,
    onBack: () -> Unit,
    viewModel: GastoViewModel
) {

    // Variables que almacenan los datos ingresados por el usuario.
    var descripcion by remember { mutableStateOf("") }
    var monto by remember { mutableStateOf("") }
    var mostrarErrorMonto by remember { mutableStateOf(false) }

    // Categorías predeterminadas para los ingresos.
    val baseIngresos = listOf(
        "Salario", "Emprendimiento", "Bonos", "Otro (Crear nueva...)"
    )

    // Categorías predeterminadas para los gastos.
    val baseGastos = listOf(
        "Vivienda y Hogar",
        "Alimentación",
        "Transporte",
        "Salud",
        "Entretenimiento",
        "Otro (Crear nueva...)"
    )

    // Obtiene las categorías personalizadas almacenadas en la base de datos.
    val categoriasPersonalizadas by viewModel
        .obtenerCategoriasPersonalizadas(tipoMovimiento)
        .collectAsState(initial = emptyList())

    // Combina las categorías predeterminadas con las creadas por el usuario.
    val categoriasDisponibles = remember(categoriasPersonalizadas, tipoMovimiento) {
        val base = if (tipoMovimiento == "INGRESO") baseIngresos else baseGastos
        val customNames = categoriasPersonalizadas.map { it.nombre }
        customNames + base
    }

    // Almacena la categoría que el usuario ha seleccionado.
    var categoriaSeleccionada by remember { mutableStateOf("") }

    // Selecciona automáticamente la primera categoría disponible.
    LaunchedEffect(categoriasDisponibles) {
        if (categoriaSeleccionada.isBlank() && categoriasDisponibles.isNotEmpty()) {
            categoriaSeleccionada = categoriasDisponibles[0]
        }
    }

    // Controla la apertura del menú desplegable y del diálogo de nuevas categorías.
    var expanded by remember { mutableStateOf(false) }
    var mostrarDialogoNuevaCat by remember { mutableStateOf(false) }
    var nombreNuevaCat by remember { mutableStateOf("") }

    // Asigna un color diferente dependiendo del tipo de movimiento.
    val colorMovimiento = if (tipoMovimiento == "INGRESO") {
        Color(0xFF15966B) // Verde para ingresos.
    } else {
        Color(0xFFD65D5D) // Rojo para gastos.
    }

    // Obtiene los colores del tema actual para adaptar la pantalla al modo claro u oscuro.
    val colorFondo = MaterialTheme.colorScheme.background
    val colorTarjeta = MaterialTheme.colorScheme.surface
    val colorTexto = MaterialTheme.colorScheme.onSurface
    val colorTextoSecundario = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.65f)

    // Contenedor principal de la pantalla.
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorFondo)
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {

        // Título principal según el tipo de movimiento.
        Text(
            text = if (tipoMovimiento == "INGRESO")
                "Registrar ingreso"
            else
                "Registrar gasto",
            fontWeight = FontWeight.Bold,
            fontSize = 25.sp,
            color = colorMovimiento
        )

        Spacer(modifier = Modifier.height(6.dp))

        // Descripción breve de la operación que se realizará.
        Text(
            text = if (tipoMovimiento == "INGRESO")
                "Agrega el dinero que recibiste"
            else
                "Registra el dinero que has gastado",
            fontSize = 14.sp,
            color = colorTextoSecundario
        )

        Spacer(modifier = Modifier.height(28.dp))

        // Tarjeta que contiene los campos del formulario.
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(
                containerColor = colorTarjeta
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 1.dp
            )
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp)
            ) {

                Text(
                    text = "Información del movimiento",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = colorTexto
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Campo para ingresar una descripción opcional.
                OutlinedTextField(
                    value = descripcion,
                    onValueChange = { descripcion = it },
                    label = { Text("DESCRIPCIÓN (Opcional)") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = colorTexto,
                        unfocusedTextColor = colorTexto,
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = colorTextoSecundario,
                        focusedLabelColor = MaterialTheme.colorScheme.primary,
                        unfocusedLabelColor = colorTextoSecundario,
                        cursorColor = MaterialTheme.colorScheme.primary
                    )
                )

                Spacer(modifier = Modifier.height(15.dp))

                // Campo para ingresar el monto del movimiento.
                OutlinedTextField(
                    value = monto,
                    onValueChange = { monto = it },
                    label = { Text("MONTO") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = colorTexto,
                        unfocusedTextColor = colorTexto,
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = colorTextoSecundario,
                        focusedLabelColor = MaterialTheme.colorScheme.primary,
                        unfocusedLabelColor = colorTextoSecundario,
                        cursorColor = MaterialTheme.colorScheme.primary
                    )
                )

                // Muestra un mensaje cuando el monto ingresado no es válido.
                if (mostrarErrorMonto) {
                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "Ingrese un monto válido mayor a 0",
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 12.sp
                    )
                }

                Spacer(modifier = Modifier.height(15.dp))

                // Menú desplegable para seleccionar una categoría.
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
                            .fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = colorTexto,
                            unfocusedTextColor = colorTexto,
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = colorTextoSecundario,
                            focusedLabelColor = MaterialTheme.colorScheme.primary,
                            unfocusedLabelColor = colorTextoSecundario
                        )
                    )

                    // Presenta todas las categorías disponibles.
                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {

                        categoriasDisponibles.forEach { cat ->

                            DropdownMenuItem(
                                text = { Text(cat) },
                                onClick = {
                                    // Si selecciona crear una categoría, abre el diálogo.
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
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Botón para validar y guardar el movimiento.
        Button(
            onClick = {

                // Convierte el monto de texto a número decimal.
                val montoDouble = monto.toDoubleOrNull()

                // Comprueba que el monto sea válido y mayor que cero.
                if (montoDouble == null || montoDouble <= 0.0) {
                    mostrarErrorMonto = true
                } else {
                    mostrarErrorMonto = false

                    // Si no se escribió una descripción, utiliza la categoría como título.
                    val descripcionFinal =
                        if (descripcion.isBlank()) categoriaSeleccionada
                        else descripcion.trim()

                    // Obtiene la fecha actual para guardar el movimiento.
                    val fechaActual =
                        SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())

                    // Envía los datos al ViewModel para registrar el movimiento.
                    viewModel.agregarMovimiento(
                        titulo = descripcionFinal,
                        monto = montoDouble,
                        categoria = categoriaSeleccionada,
                        fecha = fechaActual,
                        tipo = tipoMovimiento
                    )

                    // Regresa a la pantalla principal después de guardar.
                    onBack()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = colorMovimiento
            )
        ) {

            Text(
                text = "GUARDAR",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Botón para cancelar el registro y regresar al inicio.
        TextButton(
            onClick = onBack,
            modifier = Modifier.fillMaxWidth()
        ) {

            Text(
                text = "Cancelar y volver",
                color = colorTextoSecundario,
                fontSize = 14.sp
            )
        }

        // Diálogo para crear y guardar una categoría personalizada.
        if (mostrarDialogoNuevaCat) {

            AlertDialog(
                onDismissRequest = {
                    mostrarDialogoNuevaCat = false
                },
                title = {
                    Text(
                        "Nueva Categoría Personalizada",
                        color = colorTexto
                    )
                },
                text = {

                    // Campo donde se escribe el nombre de la nueva categoría.
                    OutlinedTextField(
                        value = nombreNuevaCat,
                        onValueChange = { nombreNuevaCat = it },
                        label = { Text("Nombre (ej. Cine, Mascotas)") },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = colorTexto,
                            unfocusedTextColor = colorTexto,
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = colorTextoSecundario
                        )
                    )
                },
                confirmButton = {

                    TextButton(
                        onClick = {

                            // Guarda la categoría si el nombre no está vacío.
                            if (nombreNuevaCat.isNotBlank()) {

                                viewModel.agregarNuevaCategoria(
                                    nombreNuevaCat.trim(),
                                    tipoMovimiento
                                )

                                // Selecciona la nueva categoría y limpia el campo.
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

                    TextButton(
                        onClick = {
                            // Cierra el diálogo y limpia el campo.
                            mostrarDialogoNuevaCat = false
                            nombreNuevaCat = ""
                        }
                    ) {
                        Text("Cancelar")
                    }
                }
            )
        }
    }
}
