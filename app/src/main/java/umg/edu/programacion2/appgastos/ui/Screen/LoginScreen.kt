
package umg.edu.programacion2.appgastos.ui.Screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/*
 * PantallaLogin:
 * Gestiona la interfaz de inicio de sesión de la aplicación.
 * Permite ingresar usuario y contraseña con validación de credenciales.
 */

@Composable
fun PantallaLogin(onLoginSuccess: () -> Unit) {

    // Variables que almacenan los datos ingresados
    var usuario by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMensaje by remember { mutableStateOf(false) }

    // Colores de la aplicación
    val azulPrincipal = Color(0xFF173B55)
    val grisFondo = Color(0xFFF4F6F8)
    val grisTexto = Color(0xFF78838C)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(grisFondo)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        // Nombre de la aplicación
        Text(
            text = "GastoSmart",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            color = azulPrincipal
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Control de tus finanzas personales",
            fontSize = 14.sp,
            color = grisTexto
        )

        Spacer(modifier = Modifier.height(30.dp))

        // Tarjeta principal del inicio de sesión
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(22.dp),
            colors = CardDefaults.cardColors(
                containerColor = azulPrincipal
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 3.dp
            )
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.Start
            ) {

                // Encabezado de la tarjeta
                Text(
                    text = "¡Bienvenido!",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Inicia sesión para continuar",
                    fontSize = 14.sp,
                    color = Color(0xFFD5E2EB)
                )

                Spacer(modifier = Modifier.height(25.dp))

                // Campo de usuario
                Text(
                    text = "Usuario",
                    color = Color.White,
                    fontWeight = FontWeight.Medium,
                    fontSize = 14.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = usuario,
                    onValueChange = {
                        usuario = it
                        errorMensaje = false
                    },
                    placeholder = {
                        Text("Ingresa tu usuario")
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        focusedBorderColor = Color.White,
                        unfocusedBorderColor = Color.White,
                        focusedTextColor = azulPrincipal,
                        unfocusedTextColor = azulPrincipal,
                        focusedPlaceholderColor = grisTexto,
                        unfocusedPlaceholderColor = grisTexto,
                        cursorColor = azulPrincipal
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(18.dp))

                // Campo de contraseña
                Text(
                    text = "Contraseña",
                    color = Color.White,
                    fontWeight = FontWeight.Medium,
                    fontSize = 14.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = password,
                    onValueChange = {
                        password = it
                        errorMensaje = false
                    },
                    placeholder = {
                        Text("Ingresa tu contraseña")
                    },
                    visualTransformation = PasswordVisualTransformation(),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        focusedBorderColor = Color.White,
                        unfocusedBorderColor = Color.White,
                        focusedTextColor = azulPrincipal,
                        unfocusedTextColor = azulPrincipal,
                        focusedPlaceholderColor = grisTexto,
                        unfocusedPlaceholderColor = grisTexto,
                        cursorColor = azulPrincipal
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                // Mensaje cuando las credenciales son incorrectas
                if (errorMensaje) {
                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Usuario o contraseña incorrectos",
                        color = Color(0xFFFFB4AB),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                Spacer(modifier = Modifier.height(25.dp))

                // Botón para iniciar sesión
                Button(
                    onClick = {
                        if (usuario == "yesmy" && password == "7578") {
                            errorMensaje = false
                            onLoginSuccess()
                        } else {
                            errorMensaje = true
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White,
                        contentColor = azulPrincipal
                    )
                ) {
                    Text(
                        text = "ENTRAR",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(15.dp))

                Text(
                    text = "Accede a tu cuenta de GastoSmart",
                    fontSize = 12.sp,
                    color = Color(0xFFD5E2EB),
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Organiza. Controla. Ahorra.",
            fontSize = 12.sp,
            color = grisTexto
        )
    }
}
