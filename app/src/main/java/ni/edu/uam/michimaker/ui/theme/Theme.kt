package ni.edu.uam.michimaker.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView

// 1. Definición para inyectar gradientes personalizados en el tema
@Immutable
data class MichiGradientColors(
    val top: Color = Color.Unspecified,
    val middle: Color = Color.Unspecified,
    val bottom: Color = Color.Unspecified
)

val LocalMichiGradients = staticCompositionLocalOf { MichiGradientColors() }

// Extension para usar en las Screens de forma directa como: MaterialTheme.michiGradients
val MaterialTheme.michiGradients: MichiGradientColors
    @Composable
    get() = LocalMichiGradients.current

// 2. Esquemas de color estándar de Material 3
private val DarkColorScheme = darkColorScheme(
    primary = PrimaryDark,
    background = BackgroundDark,
    surface = SurfaceDark,
    onPrimary = Color.Black,
    onBackground = TextPrimaryDark,
    onSurface = TextPrimaryDark
)

private val LightColorScheme = lightColorScheme(
    primary = PrimaryLight,
    background = BackgroundLight,
    surface = SurfaceLight,
    onPrimary = Color.White,
    onBackground = TextPrimaryLight,
    onSurface = TextPrimaryLight
)

@Composable
fun MichiMakerTheme(
    darkTheme: Boolean = isSystemInDarkTheme(), // Cambia automáticamente según el sistema
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    // Selección del gradiente correspondiente al tema actual
    val gradients = if (darkTheme) {
        MichiGradientColors(
            top = DeepPurpleDark,
            middle = NeonVioletDark,
            bottom = MidnightBlueDark
        )
    } else {
        MichiGradientColors(
            top = PinkPastel,
            middle = PeachPastel,
            bottom = LavenderPastel
        )
    }

    // Ajustar la barra de notificaciones del teléfono al color del tema
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.surface.toArgb()
        }
    }

    // Proveemos tanto el esquema de Material 3 como nuestro gradiente personalizado
    CompositionLocalProvider(LocalMichiGradients provides gradients) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography, // Asegúrate de tener tu Type.kt original aquí
            content = content
        )
    }
}