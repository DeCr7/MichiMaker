package ni.edu.uam.michimaker

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import ni.edu.uam.michimaker.navigation.AppNavigation
import ni.edu.uam.michimaker.repository.UsuarioRepository
import ni.edu.uam.michimaker.ui.theme.MichiMakerTheme
import ni.edu.uam.michimaker.utils.SessionManager

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {

            val usuarios =
                UsuarioRepository()
                    .obtenerUsuarios()

            Log.d(
                "USUARIOS_TEST",
                usuarios.toString()
            )
        }

        enableEdgeToEdge()

        SessionManager.init(this)
        setContent {
            MichiMakerTheme {
                AppNavigation()
            }
        }
    }
}