
package umg.edu.programacion2.appgastos.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

// Paleta para el modo oscuro
private val DarkColorScheme = darkColorScheme(
    primary = PrimaryEmerald,
    secondary = DeepNavy,
    tertiary = AccentRed,

    background = DarkBackground,
    surface = DarkCard,

    onPrimary = CardWhite,
    onSecondary = CardWhite,
    onTertiary = CardWhite,
    onBackground = DarkText,
    onSurface = DarkText
)

// Paleta para el modo claro
private val LightColorScheme = lightColorScheme(
    primary = PrimaryEmerald,
    secondary = DeepNavy,
    tertiary = AccentRed,

    background = BackgroundGray,
    surface = CardWhite,

    onPrimary = CardWhite,
    onSecondary = CardWhite,
    onTertiary = CardWhite,
    onBackground = TextDark,
    onSurface = TextDark
)

@Composable
fun AppGastosAndroidTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) {
        DarkColorScheme
    } else {
        LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
