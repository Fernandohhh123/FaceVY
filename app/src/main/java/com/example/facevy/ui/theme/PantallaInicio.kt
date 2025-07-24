package com.example.facevy.ui.theme

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButtonDefaults.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.facevy.R
import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.facevy.permisos.UIPermisosHelper

@Composable
fun PantallaInicio(navController: NavController) {
    // Control para evitar navegación doble
    var yaNavego by remember { mutableStateOf(false) }

    // Navegación automática después de 3 segundos
    LaunchedEffect(Unit) {
        kotlinx.coroutines.delay(3000)
        if (!yaNavego) {
            yaNavego = true
            navController.navigate(RutasPantallas.PantallaPermisos) {
                popUpTo("splash") { inclusive = true }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Logo de la app

            Image(
                painterResource (id = R.drawable.loggo_removebg_preview),
                contentDescription = "Logo FaceVY",
                modifier = Modifier.size(120.dp)
            )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "FaceVY",
            style = MaterialTheme.typography.headlineLarge
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Validación facial segura",
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Botón para continuar manualmente
        Button(
            onClick = {
                if (!yaNavego) {
                    yaNavego = true
                    navController.navigate(RutasPantallas.PantallaPermisos) {
                        popUpTo("splash") { inclusive = true }
                    }
                }
            }
        ) {
            Text("Continuar")
        }
    }
}
