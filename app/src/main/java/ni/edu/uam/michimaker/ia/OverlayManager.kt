package ni.edu.uam.michimaker.ia

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint

class OverlayManager {

    fun aplicarOverlay(
        imagenBase: Bitmap,
        overlay: Bitmap,
        x: Float,
        y: Float,
        ancho: Float,
        alto: Float,
        rotacion: Float = 0f
    ): Bitmap {

        val resultado = imagenBase.copy(
            Bitmap.Config.ARGB_8888,
            true
        )

        val canvas = Canvas(resultado)

        val paint = Paint(
            Paint.ANTI_ALIAS_FLAG
        )

        val overlayEscalado =
            Bitmap.createScaledBitmap(
                overlay,
                ancho.toInt().coerceAtLeast(1),
                alto.toInt().coerceAtLeast(1),
                true
            )

        // Guardar estado actual del canvas
        canvas.save()

        // Rotar alrededor del centro del overlay
        canvas.rotate(
            rotacion,
            x + (ancho / 2f),
            y + (alto / 2f)
        )

        canvas.drawBitmap(
            overlayEscalado,
            x,
            y,
            paint
        )

        // Restaurar estado original
        canvas.restore()

        return resultado
    }
}