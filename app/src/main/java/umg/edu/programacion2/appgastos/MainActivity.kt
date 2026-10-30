

package umg.edu.programacion2.appgastos

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import umg.edu.programacion2.appgastos.ui.Screen.PantallaLogin
import umg.edu.programacion2.appgastos.ui.Screen.PantallaHome
import umg.edu.programacion2.appgastos.ui.Screen.PantallaFormulario
import umg.edu.programacion2.appgastos.ui.Screen.PantallaHistorialGeneral
import umg.edu.programacion2.appgastos.ui.theme.AppGastosAndroidTheme
import umg.edu.programacion2.appgastos.viewmodel.GastoViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: GastoViewModel by viewModels()
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sessionManager = SessionManager(applicationContext)

        setContent {

            // Preferencia para conservar el modo claro u oscuro.
            val preferencias = remember {
                getSharedPreferences("preferencias_gastos", MODE_PRIVATE)
            }

            var modoOscuro by remember {
                mutableStateOf(
                    preferencias.getBoolean("modo_oscuro", false)
                )
            }

            AppGastosAndroidTheme(darkTheme = modoOscuro) {

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                    /*
                     * Recuperamos el estado de la sesión.
                     * null significa que todavía estamos esperando
                     * la primera respuesta de SessionManager.
                     */
                    val estadoSesion by produceState<Boolean?>(
                        initialValue = null
                    ) {
                        sessionManager.isLoggedIn.collect { valor ->
                            value = valor
                        }
                    }

                    // Permite mostrar la aplicación inmediatamente
                    // después de iniciar sesión correctamente.
                    var loginExitosoLocal by remember {
                        mutableStateOf(false)
                    }

                    when {
                        // Mientras se comprueba la sesión, mostramos carga.
                        estadoSesion == null -> {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator(
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        }

                        // Si hay una sesión activa, entramos a la aplicación.
                        estadoSesion == true || loginExitosoLocal -> {
                            NavegacionGastosSmart(
                                viewModel = viewModel,
                                modoOscuro = modoOscuro,
                                onCambiarTema = { nuevoModo ->

                                    // Actualizamos el tema y guardamos la preferencia.
                                    modoOscuro = nuevoModo

                                    preferencias.edit()
                                        .putBoolean("modo_oscuro", nuevoModo)
                                        .apply()
                                }
                            )
                        }

                        // Si no hay sesión activa, mostramos el login.
                        else -> {
                            PantallaLogin(
                                onLoginSuccess = {

                                    // Guardamos la sesión en segundo plano.
                                    val scope = CoroutineScope(Dispatchers.IO)

                                    scope.launch {
                                        sessionManager.setLoggedIn(true)
                                    }

                                    // Actualizamos la interfaz sin esperar
                                    // a que termine de guardarse la sesión.
                                    loginExitosoLocal = true
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun NavegacionGastosSmart(
    viewModel: GastoViewModel,
    modoOscuro: Boolean,
    onCambiarTema: (Boolean) -> Unit
) {

    // Controla cuál de las pantallas principales se muestra.
    var pantallaActual by remember {
        mutableStateOf("HOME")
    }

    when (pantallaActual) {

        "HOME" -> PantallaHome(
            onNavigateToIngreso = {
                pantallaActual = "INGRESO"
            },
            onNavigateToGasto = {
                pantallaActual = "GASTO"
            },
            onNavigateToHistorial = {
                pantallaActual = "HISTORIAL"
            },
            viewModel = viewModel,
            modoOscuro = modoOscuro,
            onCambiarTema = onCambiarTema
        )

        "INGRESO" -> PantallaFormulario(
            tipoMovimiento = "INGRESO",
            onBack = {
                pantallaActual = "HOME"
            },
            viewModel = viewModel
        )

        "GASTO" -> PantallaFormulario(
            tipoMovimiento = "GASTO",
            onBack = {
                pantallaActual = "HOME"
            },
            viewModel = viewModel
        )

        "HISTORIAL" -> PantallaHistorialGeneral(
            onBack = {
                pantallaActual = "HOME"
            },
            viewModel = viewModel
        )
    }
}

