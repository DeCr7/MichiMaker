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
        alto: Float
    ): Bitmap {

        val resultado =
            imagenBase.copy(
                Bitmap.Config.ARGB_8888,
                true
            )

        val canvas =
            Canvas(resultado)

        val paint =
            Paint()

        val overlayEscalado =
            Bitmap.createScaledBitmap(
                overlay,
                ancho.toInt(),
                alto.toInt(),
                true
            )

        canvas.drawBitmap(
            overlayEscalado,
            x,
            y,
            paint
        )

        return resultado
    }
}