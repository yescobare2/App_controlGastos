package umg.edu.programacion2.appgastos

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import androidx.compose.runtime.collectAsState
import kotlinx.coroutines.launch
import umg.edu.programacion2.appgastos.screen.PantallaLogin
import umg.edu.programacion2.appgastos.screen.PantallaHome
import umg.edu.programacion2.appgastos.screen.PantallaFormulario
import umg.edu.programacion2.appgastos.screen.PantallaHistorialGeneral
import umg.edu.programacion2.appgastos.viewmodel.GastoViewModel

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
                            val scope = CoroutineScope(Dispatchers.IO)
                            scope.launch {
                                sessionManager.setLoggedIn(true)
                            }
                            loginExitosoLocal = true
                        }
                    )
                } else {
                    NavegacionGastosSmart(viewModel = viewModel)
                }
            }
        }
    }
}

@Composable
fun NavegacionGastosSmart(viewModel: GastoViewModel) {
    var pantallaActual by remember { mutableStateOf("HOME") }

    when (pantallaActual) {
        "HOME" -> PantallaHome(
            onNavigateToIngreso = { pantallaActual = "INGRESO" },
            onNavigateToGasto = { pantallaActual = "GASTO" },
            onNavigateToHistorial = { pantallaActual = "HISTORIAL" },
            viewModel = viewModel
        )
        "INGRESO" -> PantallaFormulario(
            tipoMovimiento = "INGRESO",
            onBack = { pantallaActual = "HOME" },
            viewModel = viewModel
        )
        "GASTO" -> PantallaFormulario(
            tipoMovimiento = "GASTO",
            onBack = { pantallaActual = "HOME" },
            viewModel = viewModel
        )
        "HISTORIAL" -> PantallaHistorialGeneral(
            onBack = { pantallaActual = "HOME" },
            viewModel = viewModel
        )
    }
}
