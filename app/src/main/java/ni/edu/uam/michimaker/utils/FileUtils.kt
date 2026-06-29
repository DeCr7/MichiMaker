package ni.edu.uam.michimaker.utils

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.provider.MediaStore
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

object FileUtils {

    fun crearArchivoImagen(context: Context): File {
        val timestamp = System.currentTimeMillis()
        return File(
            context.filesDir,
            "michi_$timestamp.jpg"
        )
    }

    /**
     * Guarda un archivo de imagen interno en la galería pública del dispositivo (Carpeta Pictures/MichiMaker)
     */
    fun guardarEnGaleriaPublica(context: Context, rutaArchivoInterno: String): Boolean {
        return try {
            val bitmap = BitmapFactory.decodeFile(rutaArchivoInterno) ?: return false
            val timestamp = System.currentTimeMillis()
            val nombreArchivo = "michi_transformado_$timestamp.jpg"

            val contentValues = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, nombreArchivo)
                put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    put(MediaStore.MediaColumns.RELATIVE_PATH, "Pictures/MichiMaker")
                    put(MediaStore.MediaColumns.IS_PENDING, 1)
                }
            }

            val resolver = context.contentResolver
            val uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

            uri?.let { imageUri ->
                val outputStream: OutputStream? = resolver.openOutputStream(imageUri)
                outputStream?.use {
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)
                }

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    contentValues.clear()
                    contentValues.put(MediaStore.MediaColumns.IS_PENDING, 0)
                    resolver.update(imageUri, contentValues, null, null)
                }
                true
            } ?: false
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}