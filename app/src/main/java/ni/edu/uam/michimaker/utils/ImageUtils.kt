package ni.edu.uam.michimaker.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.File
import java.io.FileOutputStream

object ImageUtils {

    fun cargarBitmap(
        ruta: String
    ): Bitmap? {

        return BitmapFactory.decodeFile(ruta)
    }

    fun guardarBitmap(
        bitmap: Bitmap,
        rutaDestino: String
    ): String {

        val archivo =
            File(rutaDestino)

        FileOutputStream(archivo).use {

            bitmap.compress(
                Bitmap.CompressFormat.JPEG,
                100,
                it
            )
        }

        return archivo.absolutePath
    }
}