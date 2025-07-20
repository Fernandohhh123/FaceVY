package com.example.facevy.camara

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.LocalLifecycleOwner
import android.content.Context
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner



/*
Este archivo contiene cosas para gestionar el funcionamiento de la camara
 */

object cameraManager {

    //escena o pantall principal, camara y mas funciones
    @Composable
    fun PantallaCamara() {
        val contexto = LocalContext.current
        val cicloDeVida = LocalLifecycleOwner.current

        Surface(modifier = Modifier.fillMaxSize()) {
            CameraPreviewView(context = contexto, lifecycleOwner = cicloDeVida)
        }
    }


    //funcion para abrir y mostrar lo que capte la camara
    @Composable
    fun CameraPreviewView(
        context: Context,
        lifecycleOwner: LifecycleOwner
    ) {
        AndroidView(
            factory = {
                val previewView = PreviewView(context)

                val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
                cameraProviderFuture.addListener({
                    val cameraProvider = cameraProviderFuture.get()

                    val preview = Preview.Builder().build().also {
                        it.setSurfaceProvider(previewView.surfaceProvider)
                    }

                    //camara a usar
                    val selector = CameraSelector.DEFAULT_BACK_CAMERA

                    try {
                        cameraProvider.unbindAll()
                        cameraProvider.bindToLifecycle(
                            lifecycleOwner,
                            selector,
                            preview
                        )
                    } catch (exc: Exception) {
                        exc.printStackTrace()
                    }
                }, ContextCompat.getMainExecutor(context))

                previewView
            },
            modifier = Modifier.fillMaxSize()
        ) // androidView
    } // camera preview view

}