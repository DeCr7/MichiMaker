package ni.edu.uam.michimaker.ia

import android.content.Context
import com.google.mlkit.vision.common.InputImage
import kotlinx.coroutines.suspendCancellableCoroutine
import ni.edu.uam.michimaker.R
import ni.edu.uam.michimaker.storage.ImageStorageManager
import ni.edu.uam.michimaker.utils.BitmapUtils
import ni.edu.uam.michimaker.utils.FilterCatalog
import ni.edu.uam.michimaker.utils.ImageUtils
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class CatFilterManager(

    private val context: Context,

    private val detector: FaceDetectorManager,

    private val overlayManager: OverlayManager,

    private val storageManager: ImageStorageManager
) : FilterProcessor {

    override fun aplicarFiltro(
        rutaImagen: String,
        filtro: String
    ): String {

        throw UnsupportedOperationException(
            "Usar aplicarFiltroSuspend()"
        )
    }

    suspend fun aplicarFiltroSuspend(
        rutaImagen: String,
        filtro: String
    ): String {

        val bitmapOriginal =
            ImageUtils.cargarBitmap(
                rutaImagen
            )

        val inputImage =
            InputImage.fromFilePath(
                context,
                android.net.Uri.fromFile(
                    java.io.File(rutaImagen)
                )
            )

        val faces =
            detectarRostrosSuspend(
                inputImage
            )

        if (faces.isEmpty()) {

            throw Exception(
                "No se detectó ningún rostro"
            )
        }

        val rostro =
            faces.first()

        val filtroSeleccionado =
            FilterCatalog.filtros.first {
                it.nombre.equals(
                    filtro,
                    ignoreCase = true
                )
            }

        val orejasBitmap =
            BitmapUtils.drawableToBitmap(
                context,
                filtroSeleccionado.earsRes
            )

        val hocicoBitmap =
            BitmapUtils.drawableToBitmap(
                context,
                filtroSeleccionado.noseRes
            )

        var resultado =
            bitmapOriginal

        val cara =
            rostro.boundingBox

        /*
         * OREJAS
         */
        resultado =
            overlayManager.aplicarOverlay(
                imagenBase = resultado,
                overlay = orejasBitmap,
                x = cara.left.toFloat(),
                y = cara.top.toFloat() -
                        (cara.height() * 0.50f),
                ancho = cara.width().toFloat(),
                alto = cara.height() * 0.50f
            )

        /*
         * HOCICO
         */
        resultado =
            overlayManager.aplicarOverlay(
                imagenBase = resultado,
                overlay = hocicoBitmap,
                x = cara.centerX() -
                        (cara.width() * 0.20f),
                y = cara.centerY().toFloat(),
                ancho = cara.width() * 0.40f,
                alto = cara.height() * 0.25f
            )

        val rutaDestino =
            storageManager
                .crearRutaTransformada()

        return ImageUtils.guardarBitmap(
            resultado,
            rutaDestino
        )
    }

    private suspend fun detectarRostrosSuspend(
        image: InputImage
    ) =
        suspendCancellableCoroutine {

            detector.detectarRostros(

                image = image,

                onSuccess = { faces ->

                    it.resume(faces)
                },

                onError = { error ->

                    it.resumeWithException(
                        error
                    )
                }
            )
        }
}