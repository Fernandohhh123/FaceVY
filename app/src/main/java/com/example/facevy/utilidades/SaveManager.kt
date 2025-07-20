package com.example.facevy.utilidades

/*
Archivo relacionado con guardar cosas como la ruta de las fotos o
 configuraciones de la app para el usuario
 */



import android.content.Context
import java.io.File

object SaveManager {

    fun obtenerCarpetaFotos(context: Context): File {
        val nombreCarpeta = "FaceVY_Fotos"
        val directorio = File(context.getExternalFilesDir(null), nombreCarpeta)

        if (!directorio.exists()) {
            directorio.mkdirs()
        }

        return directorio
    }
}
