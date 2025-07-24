package com.example.facevy.camara

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CameraViewModel : ViewModel() {
    private val _faceDetected = MutableLiveData<Boolean>()
    val faceDetected: LiveData<Boolean> = _faceDetected

    fun validateFace(face: Bitmap) {
        // Lógica de comparación (ej: con ML Kit)
        _faceDetected.value = true // Cambia a false si falla
    }
}