package ni.edu.uam.michimaker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import ni.edu.uam.michimaker.navigation.AppNavigation
import ni.edu.uam.michimaker.ui.theme.MichiMakerTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            MichiMakerTheme {
                AppNavigation()
            }
        }
    }
}