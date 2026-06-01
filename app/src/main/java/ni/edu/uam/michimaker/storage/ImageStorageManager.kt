package ni.edu.uam.michimaker.storage

import android.content.Context
import ni.edu.uam.michimaker.utils.FileUtils
import java.io.File

class ImageStorageManager(
    private val context: Context
) {

    fun crearArchivo(): File {
        return FileUtils.crearArchivoImagen(context)
    }

    fun eliminarArchivo(
        ruta: String
    ): Boolean {

        return File(ruta).delete()
    }

    fun crearRutaTransformada(): String {

        return File(
            context.filesDir,
            "transformada_${System.currentTimeMillis()}.jpg"
        ).absolutePath
    }
}