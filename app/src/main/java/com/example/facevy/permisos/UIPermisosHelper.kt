package com.example.facevy.permisos

import android.app.AlertDialog
import android.content.Context
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
import androidx.navigation.NavController

/*
Este archivo contiene la interfaz grafica relacionada con la solicitud de permisos
 */


// boton para pedir permiso al usuario de usar la camara del dispositivo
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

object UiPermisosHelper {
    @Composable
    fun PantallaPermisoCamara(navController: NavController) {
        // Your permission handling UI and logic here
    }

    fun showRationale(context: Context, onAccept: () -> Unit) {
        AlertDialog.Builder(context)
            .setTitle("Permisos necesarios")
            .setMessage("FaceVY necesita acceso a la cámara")
            .setPositiveButton("Aceptar") { _, _ -> onAccept() }
            .show()
    }
}