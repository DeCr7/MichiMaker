package ni.edu.uam.michimaker.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import ni.edu.uam.michimaker.R
import ni.edu.uam.michimaker.navigation.Routes
import ni.edu.uam.michimaker.viewmodel.UserViewModel

@Composable
fun LoginScreen(
    navController: NavController,
    viewModel: UserViewModel
) {

    var username by remember {
        mutableStateOf("")
    }

    var password by remember {
        mutableStateOf("")
    }

    val loginExitoso by
    viewModel.loginExitoso.collectAsStateWithLifecycle()

    val loading by
    viewModel.loading.collectAsStateWithLifecycle()

    val mensaje by
    viewModel.mensaje.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.limpiarEstados()
    }

    LaunchedEffect(loginExitoso) {

        if (loginExitoso) {

            navController.navigate(
                Routes.HOME
            ) {
                popUpTo(Routes.LOGIN) {
                    inclusive = true
                }
            }
        }
    }

    val gradient =
        Brush.verticalGradient(
            colors = listOf(
                Color(0xFFFFD1DC),
                Color(0xFFFFE0B2),
                Color(0xFFE1BEE7)
            )
        )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(gradient)
            .padding(24.dp),

        horizontalAlignment =
            Alignment.CenterHorizontally,

        verticalArrangement =
            Arrangement.Center
    ) {

        Image(
            painter = painterResource(
                R.drawable.michimaker_logo
            ),
            contentDescription = null,
            modifier = Modifier.size(180.dp)
        )

        Spacer(
            modifier = Modifier.height(24.dp)
        )

        Card(
            modifier = Modifier.fillMaxWidth(),

            colors = CardDefaults.cardColors(
                containerColor =
                    Color.White.copy(alpha = 0.85f)
            )
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {

                Text(
                    text = "Iniciar Sesión",
                    style =
                        MaterialTheme.typography.headlineSmall
                )

                Spacer(
                    modifier = Modifier.height(20.dp)
                )

                OutlinedTextField(
                    value = username,

                    onValueChange = {
                        username = it
                    },

                    label = {
                        Text("Usuario")
                    },

                    modifier =
                        Modifier.fillMaxWidth(),

                    colors =
                        OutlinedTextFieldDefaults.colors(
                            focusedTextColor =
                                Color.Black,

                            unfocusedTextColor =
                                Color.Black,

                            focusedContainerColor =
                                Color.White,

                            unfocusedContainerColor =
                                Color.White,

                            focusedBorderColor =
                                Color(0xFFBA68C8),

                            unfocusedBorderColor =
                                Color.Gray,

                            focusedLabelColor =
                                Color(0xFFBA68C8),

                            unfocusedLabelColor =
                                Color.DarkGray,

                            cursorColor =
                                Color(0xFFBA68C8)
                        )
                )

                Spacer(
                    modifier = Modifier.height(12.dp)
                )

                OutlinedTextField(
                    value = password,

                    onValueChange = {
                        password = it
                    },

                    label = {
                        Text("Contraseña")
                    },

                    visualTransformation =
                        PasswordVisualTransformation(),

                    keyboardOptions =
                        KeyboardOptions.Default,

                    modifier =
                        Modifier.fillMaxWidth(),

                    colors =
                        OutlinedTextFieldDefaults.colors(
                            focusedTextColor =
                                Color.Black,

                            unfocusedTextColor =
                                Color.Black,

                            focusedContainerColor =
                                Color.White,

                            unfocusedContainerColor =
                                Color.White,

                            focusedBorderColor =
                                Color(0xFFBA68C8),

                            unfocusedBorderColor =
                                Color.Gray,

                            focusedLabelColor =
                                Color(0xFFBA68C8),

                            unfocusedLabelColor =
                                Color.DarkGray,

                            cursorColor =
                                Color(0xFFBA68C8)
                        )
                )

                Spacer(
                    modifier = Modifier.height(24.dp)
                )

                if (mensaje.isNotBlank()) {

                    Card(
                        modifier =
                            Modifier.fillMaxWidth(),

                        colors =
                            CardDefaults.cardColors(

                                containerColor =
                                    if (loginExitoso)
                                        Color(0xFFC8E6C9)
                                    else
                                        Color(0xFFFFCDD2)
                            )
                    ) {

                        Text(
                            text = mensaje,

                            modifier =
                                Modifier.padding(12.dp),

                            color = Color.Black
                        )
                    }

                    Spacer(
                        modifier = Modifier.height(12.dp)
                    )
                }

                Button(
                    onClick = {

                        viewModel.login(
                            username,
                            password
                        )
                    },

                    enabled = !loading,

                    modifier =
                        Modifier.fillMaxWidth()
                ) {

                    if (loading) {

                        CircularProgressIndicator(
                            modifier =
                                Modifier.size(20.dp),

                            strokeWidth = 2.dp
                        )

                        Spacer(
                            modifier =
                                Modifier.width(8.dp)
                        )

                        Text(
                            "Verificando..."
                        )

                    } else {

                        Text(
                            "Iniciar Sesión"
                        )
                    }
                }

                Spacer(
                    modifier = Modifier.height(12.dp)
                )

                TextButton(
                    modifier =
                        Modifier.align(
                            Alignment.CenterHorizontally
                        ),

                    onClick = {

                        navController.navigate(
                            Routes.REGISTER
                        )
                    }
                ) {

                    Text(
                        "Crear cuenta"
                    )
                }
            }
        }
    }
}