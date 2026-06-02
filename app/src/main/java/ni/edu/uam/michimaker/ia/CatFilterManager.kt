package ni.edu.uam.michimaker.ia

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.Face
import kotlinx.coroutines.suspendCancellableCoroutine
import ni.edu.uam.michimaker.storage.ImageStorageManager
import ni.edu.uam.michimaker.utils.BitmapUtils
import ni.edu.uam.michimaker.utils.FilterCatalog
import ni.edu.uam.michimaker.utils.ImageUtils
import java.io.File
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class CatFilterManager(

    private val context: Context,
    private val detector: FaceDetectorManager,
    private val overlayManager: OverlayManager,
    private val storageManager: ImageStorageManager
) : FilterProcessor {

    // Cache para evitar recrear bitmaps
    private val bitmapCache = mutableMapOf<Int, Bitmap>()

    override fun aplicarFiltro(
        rutaImagen: String,
        filtro: String
    ): String {
        throw UnsupportedOperationException("Usar aplicarFiltroSuspend()")
    }

    suspend fun aplicarFiltroSuspend(
        rutaImagen: String,
        filtro: String
    ): String {

        val bitmapOriginal = ImageUtils.cargarBitmap(rutaImagen)

        val inputImage = InputImage.fromFilePath(
            context,
            Uri.fromFile(File(rutaImagen))
        )

        val faces = detectarRostrosSuspend(inputImage)

        if (faces.isEmpty()) {
            throw Exception("No se detectó ningún rostro")
        }

        // ✔ rostro más grande (más estable)
        val rostro = faces.maxByOrNull {
            it.boundingBox.width() * it.boundingBox.height()
        }!!

        val filtroSeleccionado = FilterCatalog.filtros.firstOrNull {
            it.nombre.equals(filtro, ignoreCase = true)
        } ?: throw Exception("Filtro no encontrado: $filtro")

        // ✔ cache de bitmaps
        val orejasBitmap = bitmapCache.getOrPut(filtroSeleccionado.earsRes) {
            BitmapUtils.drawableToBitmap(context, filtroSeleccionado.earsRes)
        }

        val hocicoBitmap = bitmapCache.getOrPut(filtroSeleccionado.noseRes) {
            BitmapUtils.drawableToBitmap(context, filtroSeleccionado.noseRes)
        }

        var resultado = bitmapOriginal

        val cara = rostro.boundingBox

        // =========================
        // ESCALAS DINÁMICAS
        // =========================

        val earsScale = filtroSeleccionado.earsScale
        val noseScale = filtroSeleccionado.noseScale

        // =========================
        // OREJAS (mejor centrado)
        // =========================

        val earsWidth = cara.width().toFloat() * earsScale
        val earsHeight = cara.height().toFloat() * 0.6f

        resultado = overlayManager.aplicarOverlay(
            imagenBase = resultado,
            overlay = orejasBitmap,
            x = cara.centerX() - (earsWidth / 2f),
            y = cara.top.toFloat() - (earsHeight * 0.8f),
            ancho = earsWidth,
            alto = earsHeight
        )

        // =========================
        // HOCICO (mejor centrado en nariz)
        // =========================

        val noseWidth = cara.width().toFloat() * noseScale
        val noseHeight = cara.height().toFloat() * 0.35f

        resultado = overlayManager.aplicarOverlay(
            imagenBase = resultado,
            overlay = hocicoBitmap,
            x = cara.centerX() - (noseWidth / 2f),
            y = cara.centerY().toFloat() - (noseHeight * 0.2f),
            ancho = noseWidth,
            alto = noseHeight
        )

        // =========================
        // GUARDADO FINAL
        // =========================

        val rutaDestino = storageManager.crearRutaTransformada()

        return ImageUtils.guardarBitmap(resultado, rutaDestino)
    }

    // =========================
    // FACE DETECTION SUSPEND
    // =========================

    private suspend fun detectarRostrosSuspend(
        image: InputImage
    ): List<Face> =
        suspendCancellableCoroutine { cont ->

            detector.detectarRostros(
                image = image,
                onSuccess = { faces ->
                    cont.resume(faces)
                },
                onError = { error ->
                    cont.resumeWithException(error)
                }
            )
        }
}