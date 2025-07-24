package com.example.facevy.ui.theme

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import android.content.Context
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.facevy.utilidades.SaveManager

@Composable
fun PantallaSettings(navController: NavController, context: Context) {
    val carpetaFotos = SaveManager.obtenerCarpetaFotos(context)

    BackHandler {
        navController.popBackStack()
    }

    Surface(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Pantalla de Configuración", style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(16.dp))

            Text("Ruta carpeta fotos:")
            Text(text = carpetaFotos.absolutePath, style = MaterialTheme.typography.bodyMedium)

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = { navController.popBackStack() }) {
                Text("Regresar")
            }
        }
    }
}
