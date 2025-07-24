package com.example.facevy

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.facevy.ui.theme.FaceVYTheme
import androidx.activity.enableEdgeToEdge
import androidx.annotation.OptIn
import androidx.camera.core.ExperimentalGetImage
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.motionEventSpy
import androidx.compose.ui.tooling.preview.Preview
import com.example.facevy.permisos.PermisosHelper
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.facevy.camara.cameraManager
import com.example.facevy.ui.theme.PantallaSettings
import com.example.facevy.ui.theme.RutasPantallas
import com.example.facevy.ui.theme.PantallaSettings

/*
en este archivo se llamaran a todas las funciones que permitiran que funcione la app
*/

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalGetImage::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FaceVYTheme {
                val navController = rememberNavController()

                NavHost(
                    navController = navController,
                    startDestination = "splash" // ← Comienza en pantalla de inicio
                ) {
                    composable("splash") {
                        PantallaInicio(navController)
                    }

                    composable(RutasPantallas.PantallaPermisos) {
                        PermisosHelper.PantallaPermisoCamara(navController)
                    }

                    composable(RutasPantallas.PantallaCamara) {
                        cameraManager.PantallaCamara(navController)
                    }

                    composable(RutasPantallas.Settings) {
                        PantallaSettings(
                            navController = navController,
                            context = LocalContext.current
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun PantallaInicio(navController: NavController) {
    var yaNavego by remember { mutableStateOf(false) }

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
        Icon(
            painter = painterResource(id = R.drawable.prueba),
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

        Button(onClick = {
            if (!yaNavego) {
                yaNavego = true
                navController.navigate(RutasPantallas.PantallaPermisos) {
                    popUpTo("splash") { inclusive = true }
                }
            }
        }) {
            Text("Continuar")
        }
    }
}
