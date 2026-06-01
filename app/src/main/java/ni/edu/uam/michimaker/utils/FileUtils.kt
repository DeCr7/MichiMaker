package ni.edu.uam.michimaker.utils

import android.content.Context
import java.io.File

object FileUtils {

    fun crearArchivoImagen(
        context: Context
    ): File {

        val timestamp =
            System.currentTimeMillis()

        return File(
            context.filesDir,
            "michi_$timestamp.jpg"
        )
    }
}