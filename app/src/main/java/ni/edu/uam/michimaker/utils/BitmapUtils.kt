package ni.edu.uam.michimaker.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import androidx.core.content.ContextCompat

object BitmapUtils {

    fun drawableToBitmap(
        context: Context,
        drawableRes: Int
    ): Bitmap {

        val drawable =
            ContextCompat.getDrawable(
                context,
                drawableRes
            )!!

        val bitmap =
            Bitmap.createBitmap(
                drawable.intrinsicWidth,
                drawable.intrinsicHeight,
                Bitmap.Config.ARGB_8888
            )

        val canvas =
            Canvas(bitmap)

        drawable.setBounds(
            0,
            0,
            canvas.width,
            canvas.height
        )

        drawable.draw(canvas)

        return bitmap
    }
}