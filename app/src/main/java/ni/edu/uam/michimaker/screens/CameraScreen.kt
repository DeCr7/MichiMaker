package ni.edu.uam.michimaker.screens

import android.Manifest
import android.app.Activity
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import ni.edu.uam.michimaker.navigation.Routes
import ni.edu.uam.michimaker.utils.FileUtils
import ni.edu.uam.michimaker.utils.PermissionManager
import ni.edu.uam.michimaker.viewmodel.CameraViewModel
import androidx.compose.material3.*
import ni.edu.uam.michimaker.utils.PermissionRequestScreen

@Composable
fun CameraScreen(
    navController: NavController,
    viewModel: CameraViewModel = viewModel()
) {

    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val cameraState by viewModel.cameraState.collectAsState()

    val previewView = remember { PreviewView(context) }

    val imageCapture = remember {
        ImageCapture.Builder()
            .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
            .build()
    }

    // --- PERMISO ---
    LaunchedEffect(Unit) {
        val granted = PermissionManager.hasCameraPermission(context)
        viewModel.actualizarPermiso(granted)
    }

    if (!cameraState.hasPermission) {
        PermissionRequestScreen(
            onRequest = {
                ActivityCompat.requestPermissions(
                    context as Activity,
                    arrayOf(Manifest.permission.CAMERA),
                    100
                )
            }
        )
        return
    }

    // --- CAMERA PREVIEW ---
    AndroidView(
        factory = {
            val cameraProviderFuture = ProcessCameraProvider.getInstance(context)

            cameraProviderFuture.addListener({
                val cameraProvider = cameraProviderFuture.get()

                val preview = Preview.Builder().build().also {
                    it.surfaceProvider = previewView.surfaceProvider
                }

                val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

                cameraProvider.unbindAll()

                cameraProvider.bindToLifecycle(
                    lifecycleOwner,
                    cameraSelector,
                    preview,
                    imageCapture
                )

            }, ContextCompat.getMainExecutor(context))

            previewView
        },
        modifier = Modifier.fillMaxSize()
    )

    // --- BOTÓN CAPTURA ---
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomCenter
    ) {

        Button(
            modifier = Modifier
                .padding(24.dp),
            onClick = {

                viewModel.iniciarCaptura()

                val file = FileUtils.crearArchivoImagen(context)

                val outputOptions = ImageCapture.OutputFileOptions.Builder(file).build()

                imageCapture.takePicture(
                    outputOptions,
                    ContextCompat.getMainExecutor(context),
                    object : ImageCapture.OnImageSavedCallback {

                        override fun onImageSaved(output: ImageCapture.OutputFileResults) {

                            val path = file.absolutePath

                            viewModel.capturaExitosa(path)

                            navController.navigate(Routes.FILTER)
                        }

                        override fun onError(exception: ImageCaptureException) {

                            viewModel.capturaFallida(exception.message ?: "Error cámara")
                        }
                    }
                )
            }
        ) {
            Text("Capturar")
        }
    }
}