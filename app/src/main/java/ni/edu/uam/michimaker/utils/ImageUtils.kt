package ni.edu.uam.michimaker.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

object ImageUtils {

    fun cargarBitmap(ruta: String): Bitmap? {
        return try {
            BitmapFactory.decodeFile(ruta)
        } catch (e: Exception) {
            null
        }
    }

    fun guardarBitmap(bitmap: Bitmap, rutaDestino: String): String {
        val archivo = File(rutaDestino)
        FileOutputStream(archivo).use {
            bitmap.compress(
                Bitmap.CompressFormat.JPEG,
                100,
                it
            )
        }
        return archivo.absolutePath
    }

    fun bitmapToBase64(ruta: String): String? {
        return try {
            val bitmap = cargarBitmap(ruta) ?: return null
            val output = ByteArrayOutputStream()
            bitmap.compress(
                Bitmap.CompressFormat.JPEG,
                90,
                output
            )
            val bytes = output.toByteArray()
            Base64.encodeToString(bytes, Base64.NO_WRAP)
        } catch(e: Exception) {
            null
        }
    }

    fun base64ToBitmap(base64: String): Bitmap? {
        return try {
            val limpio = if(base64.contains(",")){
                base64.substring(base64.indexOf(",") + 1)
            } else {
                base64
            }
            val bytes = Base64.decode(limpio, Base64.DEFAULT)
            BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
        } catch(e: Exception) {
            null
        }
    }

    /**
     * Convierte una Uri de la galería a String Base64, aplicando
     * un reescalado y una compresión JPEG para evitar sobrecargar el servidor.
     */
    fun uriToBase64(context: android.content.Context, uri: android.net.Uri): String? {
        return try {
            val inputStream = context.contentResolver.openInputStream(uri) ?: return null

            // 1. Decodificar los bytes a un objeto Bitmap
            val originalBitmap = BitmapFactory.decodeStream(inputStream)
            inputStream.close()

            if (originalBitmap == null) return null

            // 2. Redimensionar la imagen (máximo 500px para que sea ligero pero nítido)
            val redimensionadoBitmap = optimizarTamanoBitmap(originalBitmap, 500)

            // 3. Comprimir a formato JPEG con calidad al 75%
            val outputStream = ByteArrayOutputStream()
            redimensionadoBitmap.compress(Bitmap.CompressFormat.JPEG, 75, outputStream)

            val bytesComprimidos = outputStream.toByteArray()

            // Liberar memoria de los bitmaps creados
            if (originalBitmap != redimensionadoBitmap) {
                originalBitmap.recycle()
            }
            redimensionadoBitmap.recycle()

            // 4. Codificar los bytes optimizados a Base64
            Base64.encodeToString(bytesComprimidos, Base64.NO_WRAP)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    /**
     * Función interna para cambiar las dimensiones de un Bitmap manteniendo su relación de aspecto
     */
    private fun optimizarTamanoBitmap(bitmap: Bitmap, maxDimension: Int): Bitmap {
        val anchoOriginal = bitmap.width
        val altoOriginal = bitmap.height

        if (anchoOriginal <= maxDimension && altoOriginal <= maxDimension) {
            return bitmap // Ya es lo suficientemente pequeña
        }

        val relacionAspecto = anchoOriginal.toFloat() / altoOriginal.toFloat()
        val nuevoAncho: Int
        val nuevoAlto: Int

        if (relacionAspecto > 1) {
            nuevoAncho = maxDimension
            nuevoAlto = (maxDimension / relacionAspecto).toInt()
        } else {
            nuevoAlto = maxDimension
            nuevoAncho = (maxDimension * relacionAspecto).toInt()
        }

        return Bitmap.createScaledBitmap(bitmap, nuevoAncho, nuevoAlto, true)
    }
}