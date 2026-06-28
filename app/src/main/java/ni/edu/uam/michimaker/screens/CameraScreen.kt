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
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.IconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
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

    // Estado para alternar entre cámara trasera y frontal
    var lensFacing by remember { mutableStateOf(CameraSelector.LENS_FACING_BACK) }

    val previewView = remember { PreviewView(context) }

    val imageCapture = remember {
        ImageCapture.Builder()
            .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
            .build()
    }

    // Launcher para permisos
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        viewModel.actualizarPermiso(granted)
    }

    // Verificar permisos iniciales
    LaunchedEffect(Unit) {
        viewModel.actualizarPermiso(PermissionManager.hasCameraPermission(context))
    }

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                viewModel.actualizarPermiso(PermissionManager.hasCameraPermission(context))
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    // 🔥 SOLUCIÓN: Manejar el ciclo de vida y el switch mediante LaunchedEffect reactivo a lensFacing
    LaunchedEffect(lensFacing, cameraState.hasPermission) {
        if (!cameraState.hasPermission) return@LaunchedEffect

        val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()

            val preview = Preview.Builder()
                .build()
                .also {
                    it.surfaceProvider = previewView.surfaceProvider
                }

            val cameraSelector = CameraSelector.Builder()
                .requireLensFacing(lensFacing)
                .build()

            try {
                cameraProvider.unbindAll() // Desvincula la cámara activa anterior
                cameraProvider.bindToLifecycle(
                    lifecycleOwner,
                    cameraSelector,
                    preview,
                    imageCapture
                )
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }, ContextCompat.getMainExecutor(context))
    }

    if (!cameraState.hasPermission) {
        PermissionRequestScreen(
            onRequest = { permissionLauncher.launch(Manifest.permission.CAMERA) }
        )
        return
    }

    Box(modifier = Modifier.fillMaxSize()) {

        // AndroidView simplificada (El LaunchedEffect se encarga de las actualizaciones)
        AndroidView(
            factory = { previewView },
            modifier = Modifier.fillMaxSize()
        )

        // Botón para cambiar de cámara (Esquina superior derecha)
        IconButton(
            onClick = {
                lensFacing = if (lensFacing == CameraSelector.LENS_FACING_BACK) {
                    CameraSelector.LENS_FACING_FRONT
                } else {
                    CameraSelector.LENS_FACING_BACK
                }
            },
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(24.dp)
                .size(48.dp),
            colors = androidx.compose.material3.IconButtonDefaults.iconButtonColors(
                containerColor = Color.Black.copy(alpha = 0.5f),
                contentColor = Color.White
            )
        ) {
            Icon(
                imageVector = Icons.Default.Refresh,
                contentDescription = "Girar Cámara",
                modifier = Modifier.size(28.dp)
            )
        }

        // Botón de captura (Abajo)
        Box(
            modifier = Modifier.fillMaxSize().padding(bottom = 24.dp),
            contentAlignment = Alignment.BottomCenter
        ) {
            Button(
                onClick = {
                    viewModel.iniciarCaptura()
                    val file = FileUtils.crearArchivoImagen(context)
                    val outputOptions = ImageCapture.OutputFileOptions.Builder(file).build()

                    imageCapture.takePicture(
                        outputOptions,
                        ContextCompat.getMainExecutor(context),
                        object : ImageCapture.OnImageSavedCallback {
                            override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                                val imagePath = file.absolutePath
                                viewModel.capturaExitosa(imagePath)
                                navController.navigate("filter/${android.net.Uri.encode(imagePath)}")
                            }

                            override fun onError(exception: ImageCaptureException) {
                                viewModel.capturaFallida(exception.message ?: "Error al capturar imagen")
                            }
                        }
                    )
                }
            ) {
                Text("Capturar")
            }
        }
    }
}