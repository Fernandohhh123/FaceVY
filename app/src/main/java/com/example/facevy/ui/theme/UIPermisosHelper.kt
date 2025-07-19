package com.example.facevy.ui.theme

import android.Manifest
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/*
Este archivo contiene la interfaz grafica relacionada con la solicitud de permisos
 */

object UIPermisosHelper {
    @Composable
    fun textPedirPermisoCamara(
        tienePermiso: Boolean,
        onClickPedirPermiso: () -> Unit
    ) {
        Surface(
            modifier = Modifier.fillMaxSize()
        ) {
            if (tienePermiso) {
                Text(
                    text = "Permiso concedido. Cargando cámara...",
                    modifier = Modifier.padding(16.dp)
                )
            } else {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = "Necesitamos permiso para usar la cámara")
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(onClick = onClickPedirPermiso) {
                        Text(text = "Conceder permiso")
                    }
                }
            }
        }
    }
}