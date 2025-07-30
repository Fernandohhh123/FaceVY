package com.example.facevy.camara


import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageFormat
import android.graphics.Matrix
import android.graphics.YuvImage
import android.os.Environment
import android.os.Handler
import android.os.Looper
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import android.app.Activity
import android.content.Context
import android.graphics.Rect
import android.media.Image
import android.util.Size
import androidx.activity.compose.BackHandler
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size as ComposeSize
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavController
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.*


private var guardandoImagen = false

object cameraManager {

    @Composable
    @androidx.camera.core.ExperimentalGetImage
    fun PantallaCamara(navController: NavController) {
        val contexto = LocalContext.current
        val cicloDeVida = LocalLifecycleOwner.current

        var mostrarDialogo by remember { mutableStateOf(false) }
        var cameraSelector by remember { mutableStateOf(CameraSelector.DEFAULT_BACK_CAMERA) }

        BackHandler {
            mostrarDialogo = true
        }

        if (mostrarDialogo) {
            AlertDialog(
                onDismissRequest = { mostrarDialogo = false },
                title = { Text("¿Salir?") },
                text = { Text("¿Salir de FaceVY?") },
                confirmButton = {
                    Button(onClick = {
                        mostrarDialogo = false
                        (contexto as? Activity)?.finish()
                    }) {
                        Text("Sí")
                    }
                },
                dismissButton = {
                    Button(onClick = { mostrarDialogo = false }) {
                        Text("No")
                    }
                }
            )
        }

        Box(modifier = Modifier.fillMaxSize()) {

            CameraPreviewConDeteccion(
                context = contexto,
                lifecycleOwner = cicloDeVida,
                cameraSelector = cameraSelector
            )

            Button(
                onClick = { navController.navigate("settings") },
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.TopEnd)
            ) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = "Ir a Configuración"
                )
            }

            Button(
                onClick = {
                    cameraSelector = if (cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA)
                        CameraSelector.DEFAULT_FRONT_CAMERA
                    else
                        CameraSelector.DEFAULT_BACK_CAMERA
                },
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.TopStart)
            ) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = "Cambiar cámara"
                )
            }
        }
    }

    @Composable
    @androidx.camera.core.ExperimentalGetImage
    fun CameraPreviewConDeteccion(
        context: Context,
        lifecycleOwner: LifecycleOwner,
        cameraSelector: CameraSelector
    ) {
        val previewView = remember { PreviewView(context) }
        val listaCaras = remember { mutableStateListOf<Rect>() }
        val imageSize = remember { mutableStateOf(Size(0, 0)) }

        LaunchedEffect(cameraSelector) {
            val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
            val cameraProvider = cameraProviderFuture.get()

            val preview = Preview.Builder()
                .setTargetAspectRatio(AspectRatio.RATIO_16_9)
                .build()
                .also {
                    it.setSurfaceProvider(previewView.surfaceProvider)
                }

            val options = FaceDetectorOptions.Builder()
                .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_FAST)
                .enableTracking()
                .build()

            val detector = FaceDetection.getClient(options)

            val imageAnalyzer = ImageAnalysis.Builder()
                .setTargetAspectRatio(AspectRatio.RATIO_16_9)
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build()
                .also {
                    it.setAnalyzer(ContextCompat.getMainExecutor(context)) { imageProxy ->
                        procesarImagen(detector, imageProxy, listaCaras, imageSize)
                    }
                }

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    lifecycleOwner,
                    cameraSelector,
                    preview,
                    imageAnalyzer
                )
            } catch (exc: Exception) {
                exc.printStackTrace()
            }
        }

        Box(modifier = Modifier.fillMaxSize()) {
            AndroidView(
                factory = { previewView },
                modifier = Modifier.fillMaxSize()
            )

            Canvas(modifier = Modifier.fillMaxSize()) {
                val imgSize = imageSize.value
                if (imgSize.width != 0 && imgSize.height != 0) {
                    val scaleX = size.width / imgSize.width.toFloat()
                    val scaleY = size.height / imgSize.height.toFloat()

                    for (faceRect in listaCaras) {
                        val left = faceRect.left * scaleX
                        val top = faceRect.top * scaleY
                        val right = faceRect.right * scaleX
                        val bottom = faceRect.bottom * scaleY

                        val drawLeft = if (cameraSelector == CameraSelector.DEFAULT_FRONT_CAMERA)
                            size.width - left - (right - left) else left

                        drawRect(
                            color = Color.Yellow,
                            topLeft = Offset(drawLeft, top),
                            size = ComposeSize(right - left, bottom - top),
                            style = Stroke(width = 5f)
                        )
                    }
                }
            }
        }
    }

    @androidx.camera.core.ExperimentalGetImage
    private fun procesarImagen(
        detector: FaceDetector,
        imageProxy: ImageProxy,
        listaCaras: MutableList<Rect>,
        imageSize: MutableState<Size>
    ) {
        val mediaImage = imageProxy.image
        if (mediaImage != null) {
            val rotation = imageProxy.imageInfo.rotationDegrees
            if (rotation == 90 || rotation == 270) {
                imageSize.value = Size(imageProxy.height, imageProxy.width)
            } else {
                imageSize.value = Size(imageProxy.width, imageProxy.height)
            }

            val image = InputImage.fromMediaImage(mediaImage, rotation)

            detector.process(image)
                .addOnSuccessListener { faces ->
                    listaCaras.clear()
                    if (faces.isNotEmpty()) {
                        for (face in faces) {
                            listaCaras.add(face.boundingBox)
                        }

                        // Captura automática solo la primera vez que detecta una cara
                        if (!guardandoImagen) {
                            guardandoImagen = true
                            guardarImagenDeRostro(mediaImage, rotation, faces[0].boundingBox) {
                                // Resetear para permitir otra captura después de un tiempo
                                Handler(Looper.getMainLooper()).postDelayed({
                                    guardandoImagen = false
                                }, 5000)
                            }
                        }
                    }
                    imageProxy.close()
                }
                .addOnFailureListener {
                    imageProxy.close()
                }
        } else {
            imageProxy.close()
        }
    }



    private fun guardarImagenDeRostro(
        mediaImage: Image,
        rotation: Int,
        boundingBox: Rect,
        onGuardado: () -> Unit
    ) {
        val bitmap = BitmapUtils.mediaImageToBitmap(mediaImage, rotation) ?: return
        val rostroBitmap = Bitmap.createBitmap(
            bitmap,
            boundingBox.left.coerceAtLeast(0),
            boundingBox.top.coerceAtLeast(0),
            boundingBox.width().coerceAtMost(bitmap.width - boundingBox.left),
            boundingBox.height().coerceAtMost(bitmap.height - boundingBox.top)
        )

        val filename = "rostro_${System.currentTimeMillis()}.jpg"
        val file = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), filename)

        try {
            val out = FileOutputStream(file)
            rostroBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out)
            out.flush()
            out.close()
            onGuardado()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


}

object BitmapUtils {
    fun mediaImageToBitmap(image: Image, rotation: Int): Bitmap? {
        val yBuffer = image.planes[0].buffer
        val uBuffer = image.planes[1].buffer
        val vBuffer = image.planes[2].buffer

        val ySize = yBuffer.remaining()
        val uSize = uBuffer.remaining()
        val vSize = vBuffer.remaining()

        val nv21 = ByteArray(ySize + uSize + vSize)
        yBuffer.get(nv21, 0, ySize)
        vBuffer.get(nv21, ySize, vSize)
        uBuffer.get(nv21, ySize + vSize, uSize)

        val yuvImage = YuvImage(nv21, ImageFormat.NV21, image.width, image.height, null)
        val out = ByteArrayOutputStream()
        yuvImage.compressToJpeg(Rect(0, 0, image.width, image.height), 100, out)
        val yuv = out.toByteArray()
        var bitmap = BitmapFactory.decodeByteArray(yuv, 0, yuv.size)

        val matrix = Matrix().apply { postRotate(rotation.toFloat()) }
        bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
        return bitmap
    }
}
