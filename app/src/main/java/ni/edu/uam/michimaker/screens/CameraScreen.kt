package ni.edu.uam.michimaker.screens

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import ni.edu.uam.michimaker.navigation.Routes
import ni.edu.uam.michimaker.utils.FileUtils
import ni.edu.uam.michimaker.utils.PermissionManager
import ni.edu.uam.michimaker.utils.PermissionRequestScreen
import ni.edu.uam.michimaker.viewmodel.CameraViewModel

@Composable
fun CameraScreen(
    navController: NavController,
    viewModel: CameraViewModel = viewModel()
) {

    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    val cameraState by viewModel.cameraState.collectAsState()

    val previewView = remember {
        PreviewView(context)
    }

    val imageCapture = remember {
        ImageCapture.Builder()
            .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
            .build()
    }

    // Launcher moderno para permisos
    val permissionLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission()
        ) { granted ->

            viewModel.actualizarPermiso(granted)
        }

    // Verificar permiso al abrir pantalla
    LaunchedEffect(Unit) {
        viewModel.actualizarPermiso(
            PermissionManager.hasCameraPermission(context)
        )
    }

    // Verificar nuevamente al regresar a la pantalla
    DisposableEffect(lifecycleOwner) {

        val observer = LifecycleEventObserver { _, event ->

            if (event == Lifecycle.Event.ON_RESUME) {

                viewModel.actualizarPermiso(
                    PermissionManager.hasCameraPermission(context)
                )
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    // Pantalla de permiso
    if (!cameraState.hasPermission) {

        PermissionRequestScreen(
            onRequest = {
                permissionLauncher.launch(
                    Manifest.permission.CAMERA
                )
            }
        )

        return
    }

    // Vista previa de cámara
    AndroidView(
        factory = {

            val cameraProviderFuture =
                ProcessCameraProvider.getInstance(context)

            cameraProviderFuture.addListener({

                val cameraProvider =
                    cameraProviderFuture.get()

                val preview =
                    Preview.Builder()
                        .build()
                        .also {
                            it.surfaceProvider =
                                previewView.surfaceProvider
                        }

                cameraProvider.unbindAll()

                cameraProvider.bindToLifecycle(
                    lifecycleOwner,
                    CameraSelector.DEFAULT_BACK_CAMERA,
                    preview,
                    imageCapture
                )

            }, ContextCompat.getMainExecutor(context))

            previewView
        },
        modifier = Modifier.fillMaxSize()
    )

    // Botón de captura
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomCenter
    ) {

        Button(
            modifier = Modifier.padding(24.dp),
            onClick = {

                viewModel.iniciarCaptura()

                val file =
                    FileUtils.crearArchivoImagen(context)

                val outputOptions =
                    ImageCapture.OutputFileOptions.Builder(file)
                        .build()

                imageCapture.takePicture(
                    outputOptions,
                    ContextCompat.getMainExecutor(context),
                    object : ImageCapture.OnImageSavedCallback {

                        override fun onImageSaved(
                            output: ImageCapture.OutputFileResults
                        ) {

                            val imagePath = file.absolutePath

                            viewModel.capturaExitosa(imagePath)

                            navController.navigate(
                                "filter/${android.net.Uri.encode(imagePath)}"
                            )
                        }

                        override fun onError(
                            exception: ImageCaptureException
                        ) {

                            viewModel.capturaFallida(
                                exception.message
                                    ?: "Error al capturar imagen"
                            )
                        }
                    }
                )
            }
        ) {

            Text("Capturar")
        }
    }
}