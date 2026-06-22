package ni.edu.uam.michimaker.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream


object ImageUtils {


    fun cargarBitmap(
        ruta: String
    ): Bitmap? {

        return try {

            BitmapFactory.decodeFile(ruta)

        } catch (e: Exception) {

            null
        }
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





    fun bitmapToBase64(
        ruta: String
    ): String? {


        return try {


            val bitmap =
                cargarBitmap(ruta)
                    ?: return null



            val output =
                ByteArrayOutputStream()



            bitmap.compress(
                Bitmap.CompressFormat.JPEG,
                90,
                output
            )



            val bytes =
                output.toByteArray()



            Base64.encodeToString(
                bytes,
                Base64.NO_WRAP
            )


        } catch(e: Exception) {


            null
        }
    }





    fun base64ToBitmap(
        base64: String
    ): Bitmap? {


        return try {


            val limpio =
                if(base64.contains(",")){

                    base64.substring(
                        base64.indexOf(",") + 1
                    )

                } else {

                    base64
                }



            val bytes =
                Base64.decode(
                    limpio,
                    Base64.DEFAULT
                )



            BitmapFactory.decodeByteArray(
                bytes,
                0,
                bytes.size
            )


        } catch(e: Exception) {


            null
        }
    }
}