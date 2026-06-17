package ni.edu.uam.michimaker.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.navigation.NavController
import ni.edu.uam.michimaker.R
import ni.edu.uam.michimaker.dto.UsuarioDto
import ni.edu.uam.michimaker.viewmodel.UserViewModel

@Composable
fun RegisterScreen(
    navController: NavController,
    viewModel: UserViewModel
) {

    var nombre by remember {
        mutableStateOf("")
    }

    var username by remember {
        mutableStateOf("")
    }

    var correo by remember {
        mutableStateOf("")
    }

    var password by remember {
        mutableStateOf("")
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

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(
                containerColor =
                    Color.White.copy(alpha = 0.85f)
            )
        ) {

            Column(
                modifier = Modifier
                    .padding(24.dp),
                horizontalAlignment =
                    Alignment.CenterHorizontally
            ) {

                Image(
                    painter = painterResource(
                        R.drawable.michimaker_logo
                    ),
                    contentDescription = null,
                    modifier = Modifier.size(240.dp)
                )

                Spacer(
                    modifier = Modifier.height(16.dp)
                )

                Text(
                    text = "Crear Cuenta",
                    style = MaterialTheme
                        .typography
                        .headlineSmall,
                    color = Color.Black
                )

                Spacer(
                    modifier = Modifier.height(24.dp)
                )

                OutlinedTextField(
                    value = nombre,
                    onValueChange = {
                        nombre = it
                    },
                    label = {
                        Text("Nombre")
                    },
                    modifier =
                        Modifier.fillMaxWidth(),
                    colors =
                        OutlinedTextFieldDefaults.colors(
                            focusedContainerColor =
                                Color.White,
                            unfocusedContainerColor =
                                Color.White,
                            focusedTextColor =
                                Color.Black,
                            unfocusedTextColor =
                                Color.Black,
                            focusedLabelColor =
                                Color(0xFF7E57C2),
                            unfocusedLabelColor =
                                Color.DarkGray,
                            focusedBorderColor =
                                Color(0xFF7E57C2),
                            unfocusedBorderColor =
                                Color.Gray
                        )
                )

                Spacer(
                    modifier = Modifier.height(12.dp)
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
                            focusedContainerColor =
                                Color.White,
                            unfocusedContainerColor =
                                Color.White,
                            focusedTextColor =
                                Color.Black,
                            unfocusedTextColor =
                                Color.Black,
                            focusedLabelColor =
                                Color(0xFF7E57C2),
                            unfocusedLabelColor =
                                Color.DarkGray,
                            focusedBorderColor =
                                Color(0xFF7E57C2),
                            unfocusedBorderColor =
                                Color.Gray
                        )
                )

                Spacer(
                    modifier = Modifier.height(12.dp)
                )

                OutlinedTextField(
                    value = correo,
                    onValueChange = {
                        correo = it
                    },
                    label = {
                        Text("Correo")
                    },
                    modifier =
                        Modifier.fillMaxWidth(),
                    colors =
                        OutlinedTextFieldDefaults.colors(
                            focusedContainerColor =
                                Color.White,
                            unfocusedContainerColor =
                                Color.White,
                            focusedTextColor =
                                Color.Black,
                            unfocusedTextColor =
                                Color.Black,
                            focusedLabelColor =
                                Color(0xFF7E57C2),
                            unfocusedLabelColor =
                                Color.DarkGray,
                            focusedBorderColor =
                                Color(0xFF7E57C2),
                            unfocusedBorderColor =
                                Color.Gray
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
                            focusedContainerColor =
                                Color.White,
                            unfocusedContainerColor =
                                Color.White,
                            focusedTextColor =
                                Color.Black,
                            unfocusedTextColor =
                                Color.Black,
                            focusedLabelColor =
                                Color(0xFF7E57C2),
                            unfocusedLabelColor =
                                Color.DarkGray,
                            focusedBorderColor =
                                Color(0xFF7E57C2),
                            unfocusedBorderColor =
                                Color.Gray
                        )
                )

                Spacer(
                    modifier = Modifier.height(24.dp)
                )

                Button(
                    onClick = {

                        viewModel.registrar(
                            UsuarioDto(
                                username = username,
                                nombre = nombre,
                                correo = correo,
                                password = password,
                                fotoPerfil = null
                            )
                        )

                        navController.popBackStack()
                    },
                    modifier =
                        Modifier.fillMaxWidth()
                ) {

                    Text("Registrarse")
                }
            }
        }
    }
}