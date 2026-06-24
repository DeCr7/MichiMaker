package ni.edu.uam.michimaker.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import ni.edu.uam.michimaker.navigation.Routes
import ni.edu.uam.michimaker.repository.TransformacionRepository
import ni.edu.uam.michimaker.utils.SessionManager
import ni.edu.uam.michimaker.viewmodel.TransformacionViewModel
import ni.edu.uam.michimaker.viewmodel.TransformacionViewModelFactory

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64


private fun bitmapDesdeBase64(
    base64: String?
): Bitmap? {

    return try {

        if(base64.isNullOrBlank()) {
            return null
        }


        val bytes =
            Base64.decode(
                base64,
                Base64.DEFAULT
            )


        BitmapFactory.decodeByteArray(
            bytes,
            0,
            bytes.size
        )

    } catch(e: Exception) {

        null
    }
}



@Composable
fun HistoryScreen(
    navController: NavController
) {


    val gradient =
        Brush.verticalGradient(
            colors = listOf(
                Color(0xFFF8BBD0), // rosa viejo
                Color(0xFFD1C4E9), // lavanda
                Color(0xFFFFECB3)  // crema
            )
        )


    val usuario =
        SessionManager.obtenerUsuario()


    if(usuario == null) {

        LaunchedEffect(Unit) {

            navController.navigate(
                Routes.LOGIN
            ) {
                popUpTo(0)
            }
        }

        return
    }


    val repository =
        remember {
            TransformacionRepository()
        }


    val viewModel: TransformacionViewModel =
        viewModel(
            factory =
                TransformacionViewModelFactory(
                    repository = repository,
                    usuarioId = usuario.id ?: 0
                )
        )


    val state by viewModel.uiState.collectAsState()



    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .background(gradient)
                .padding(16.dp)
    ) {


        Text(
            text = "Historial de ${usuario.username}",
            style =
                MaterialTheme.typography.titleLarge
        )


        Spacer(
            Modifier.height(12.dp)
        )


        Text(
            text =
                "Transformaciones guardadas en tu cuenta"
        )


        Spacer(
            Modifier.height(16.dp)
        )


        Button(
            onClick = {

                navController.navigate(
                    Routes.HOME
                ) {
                    launchSingleTop = true
                }
            }
        ) {

            Text("Volver al inicio")
        }



        Spacer(
            Modifier.height(16.dp)
        )


        Text(
            text =
                "Total de transformaciones: ${state.total}"
        )



        Spacer(
            Modifier.height(16.dp)
        )



        when {


            state.cargando -> {

                CircularProgressIndicator()
            }



            state.error != null -> {

                Text(
                    text =
                        "Error: ${state.error}",
                    color =
                        MaterialTheme.colorScheme.error
                )
            }



            state.transformaciones.isEmpty() -> {


                Text(
                    "No hay transformaciones registradas."
                )


                Spacer(
                    Modifier.height(12.dp)
                )


                Button(
                    onClick = {

                        navController.navigate(
                            Routes.CAMERA
                        )
                    }
                ) {

                    Text("Crear una Transformación")
                }
            }



            else -> {


                LazyColumn(
                    verticalArrangement =
                        Arrangement.spacedBy(12.dp)
                ) {


                    items(
                        items =
                            state.transformaciones,
                        key =
                            { it.id }
                    ) { item ->



                        Card(
                            modifier =
                                Modifier.fillMaxWidth()
                        ) {


                            Column(
                                Modifier.padding(12.dp)
                            ) {



                                Text(
                                    text =
                                        item.nombreFiltro,

                                    style =
                                        MaterialTheme
                                            .typography
                                            .titleMedium
                                )



                                Spacer(
                                    Modifier.height(6.dp)
                                )



                                Text(
                                    text =
                                        "Fecha: ${item.fecha}"
                                )



                                Spacer(
                                    Modifier.height(8.dp)
                                )



                                val bitmap =
                                    remember(
                                        item.id,
                                        item.imagenBase64
                                    ) {


                                        bitmapDesdeBase64(
                                            item.imagenBase64
                                        )
                                        // Compatibilidad con registros viejos
                                            ?: null
                                    }



                                if(bitmap != null) {


                                    Image(
                                        bitmap =
                                            bitmap.asImageBitmap(),

                                        contentDescription =
                                            "Transformación",

                                        modifier =
                                            Modifier
                                                .fillMaxWidth()
                                                .height(220.dp)
                                    )

                                } else {


                                    Text(
                                        text =
                                            "No se pudo cargar la imagen",

                                        color =
                                            MaterialTheme
                                                .colorScheme
                                                .error
                                    )
                                }




                                if(
                                    !item.leyenda.isNullOrBlank()
                                ) {


                                    Spacer(
                                        Modifier.height(8.dp)
                                    )


                                    HorizontalDivider()


                                    Spacer(
                                        Modifier.height(8.dp)
                                    )


                                    Text(
                                        text =
                                            "Leyenda",

                                        style =
                                            MaterialTheme
                                                .typography
                                                .labelLarge
                                    )


                                    Text(
                                        text =
                                            item.leyenda ?: ""
                                    )

                                    Spacer(
                                        modifier = Modifier.height(12.dp)
                                    )

                                    var editando by remember {
                                        mutableStateOf(false)
                                    }

                                    var nuevaLeyenda by remember {
                                        mutableStateOf(
                                            item.leyenda ?: ""
                                        )
                                    }

                                    if (editando) {

                                        OutlinedTextField(
                                            value = nuevaLeyenda,

                                            onValueChange = {
                                                nuevaLeyenda = it
                                            },

                                            modifier = Modifier.fillMaxWidth(),

                                            label = {
                                                Text("Editar leyenda")
                                            }
                                        )

                                        Spacer(
                                            modifier = Modifier.height(8.dp)
                                        )

                                        Row(
                                            horizontalArrangement =
                                                Arrangement.spacedBy(8.dp)
                                        ) {

                                            Button(
                                                onClick = {

                                                    viewModel.actualizarLeyenda(
                                                        item.id,
                                                        nuevaLeyenda
                                                    )

                                                    editando = false
                                                }
                                            ) {

                                                Text("Guardar")
                                            }

                                            OutlinedButton(
                                                onClick = {

                                                    editando = false

                                                    nuevaLeyenda =
                                                        item.leyenda ?: ""
                                                }
                                            ) {

                                                Text("Cancelar")
                                            }
                                        }

                                    } else {

                                        Row(
                                            horizontalArrangement =
                                                Arrangement.spacedBy(8.dp)
                                        ) {

                                            Button(
                                                onClick = {

                                                    editando = true
                                                }
                                            ) {

                                                Text("Editar")
                                            }

                                            Button(
                                                onClick = {

                                                    viewModel.eliminarTransformacion(
                                                        item.id
                                                    )
                                                }
                                            ) {

                                                Text("Eliminar")
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}