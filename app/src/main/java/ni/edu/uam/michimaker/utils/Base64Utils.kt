package ni.edu.uam.michimaker.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64

object Base64Utils {

    fun decodeBitmap(
        imagenBase64: String
    ): Bitmap? {

        return try {

            val bytes =
                Base64.decode(
                    imagenBase64,
                    Base64.DEFAULT
                )

            BitmapFactory.decodeByteArray(
                bytes,
                0,
                bytes.size
            )

        } catch (e: Exception) {

            null
        }
    }
}