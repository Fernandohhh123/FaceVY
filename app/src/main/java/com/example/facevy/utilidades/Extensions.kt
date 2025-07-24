package com.example.facevy.utilidades

import android.content.Context
import android.graphics.Bitmap
import android.widget.Toast
import android.graphics.*
/*
este archivo contiene funciones generales
 */

class Extensions {
    fun Context.showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    fun Bitmap.toGrayScale(): Bitmap {
        val output = if (this.isMutable) this.copy(Bitmap.Config.ARGB_8888, true)
        else this.copy(Bitmap.Config.ARGB_8888, false)

        // 2. Aplicar filtro de escala de grises
        val canvas = Canvas(output)
        val paint = Paint()
        val colorMatrix = ColorMatrix().apply {
            setSaturation(0f) // 0 = Escala de grises
        }
        val filter = ColorMatrixColorFilter(colorMatrix)
        paint.colorFilter = filter
        canvas.drawBitmap(output, 0f, 0f, paint)

        return output
}
}