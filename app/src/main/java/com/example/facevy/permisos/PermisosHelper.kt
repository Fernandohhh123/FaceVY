package com.example.facevy.permisos

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.example.facevy.ui.theme.RutasPantallas

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.facevy.R
import kotlinx.coroutines.delay
import com.example.facevy.permisos.UIPermisosHelper
import com.example.facevy.permisos.UiPermisosHelper



//esto nos permite solicitar los permisos de la camara
// funciones que proporciona el objeto
//  > tienePermisosCamara(Activity) -> funcion para verificar que el permiso fue otorgado
//  > solicitarPermisoCamara(Activity) -> esta funcion es para solicitar el permiso si no esta concedido
//  > PantallaPermisoCamara() -> pantalla para pedir permiso al usuario
object PermisosHelper {
    private const val PERMISO_CAMARA = Manifest.permission.CAMERA

    @Composable
    fun PantallaPermisoCamara(navController: NavController) {
        val actividad = LocalActivity.current
        val contexto = LocalContext.current
        var tienePermiso by remember { mutableStateOf(false) }
        var mostrarError by remember { mutableStateOf(false) }
        var mensajeError by remember { mutableStateOf("") }

        val launcher = rememberLauncherForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { concedido ->
            tienePermiso = concedido
            if (concedido) {
                // Navegación segura con try-catch
                try {
                    navController.navigate(RutasPantallas.PantallaCamara) {
                        // Configuración importante para evitar problemas:
                        launchSingleTop = true
                        popUpTo(RutasPantallas.PantallaCamara) { inclusive = true }
                    }
                } catch (e: Exception) {
                    mensajeError = "Error de navegación: ${e.localizedMessage}"
                    mostrarError = true
                }
            } else {
                mensajeError = "Permiso denegado. Por favor, activa los permisos en configuración."
                mostrarError = true
            }
        }

        // Verificar permisos al inicio
        LaunchedEffect(Unit) {
            actividad?.let {
                tienePermiso = ContextCompat.checkSelfPermission(
                    it,
                    PERMISO_CAMARA
                ) == PackageManager.PERMISSION_GRANTED

                if (tienePermiso) {
                    navController.navigate(RutasPantallas.PantallaCamara) {
                        launchSingleTop = true
                        popUpTo(RutasPantallas.PantallaCamara) { inclusive = true }
                    }
                }
            }
        }

        // UI con manejo mejorado del botón Continuar
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // ... (tu contenido UI existente) ...

            Button(
                onClick = {
                    if (tienePermiso) {
                        try {
                            navController.navigate(RutasPantallas.PantallaCamara) {
                                launchSingleTop = true
                                popUpTo(RutasPantallas.PantallaCamara) { inclusive = true }
                            }
                        } catch (e: Exception) {
                            mensajeError = "Error al navegar: ${e.localizedMessage}"
                            mostrarError = true
                        }
                    } else {
                        actividad?.let {
                            if (ActivityCompat.shouldShowRequestPermissionRationale(
                                    it,
                                    PERMISO_CAMARA
                                )
                            ) {
                                mensajeError = "La app necesita permisos de cámara para funcionar"
                                mostrarError = true
                            }
                            launcher.launch(PERMISO_CAMARA)
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (tienePermiso) "Continuar" else "Solicitar Permiso")
            }

            if (mostrarError) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = mensajeError,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }

    }
}