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
import com.example.facevy.ui.theme.UIPermisosHelper
import com.example.facevy.camara.cameraManager
import com.example.facevy.ui.theme.RutasPantallas


//esto nos permite solicitar los permisos de la camara
// funciones que proporciona el objeto
//  > tienePermisosCamara(Activity) -> funcion para verificar que el permiso fue otorgado
//  > solicitarPermisoCamara(Activity) -> esta funcion es para solicitar el permiso si no esta concedido
//  > PantallaPermisoCamara() -> pantalla para pedir permiso al usuario
object PermisosHelper {
    const val PERMISO_CAMARA = android.Manifest.permission.CAMERA
    const val CODIGO_SOLICITUD_CAMARA = 100



    //funcion para verificar que el permiso fue otorgado
    fun tienePermisoCamara(activity: Activity): Boolean {
        //solicita el permiso y lo compara con PERMISSION_GRANTED y retorna true si la comparacion es verdadera
        return ContextCompat.checkSelfPermission(activity,PERMISO_CAMARA) == PackageManager.PERMISSION_GRANTED
    }

    // esta funcion es para solicitar el permiso si no esta concedido
    //  la funcion se usa cuando se le quiere pedir permiso al usuario
    fun solicitarPermisoCamara(activity: Activity) {

        // mostramos el dialogo para permitir o denegar el acceso a la camara
        ActivityCompat.requestPermissions(activity,arrayOf(PERMISO_CAMARA),CODIGO_SOLICITUD_CAMARA)
    }






    //aqui esta la pantalla para pedirle permiso al usuario de acceder a la camara
    @Composable
    fun PantallaPermisoCamara(navController: NavController) {
        val actividad = LocalActivity.current
        val tienePermiso = remember {
            mutableStateOf(actividad?.let { PermisosHelper.tienePermisoCamara(it) } ?: false)
        }

        val launcher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission()
        ) {
            concedido -> tienePermiso.value = concedido
        }

        //vamos a la pantalla de la camara
        // Si ya tiene permiso, ir directo a la pantalla de camara
        LaunchedEffect(tienePermiso.value) {
            if (tienePermiso.value) {
                navController.navigate(RutasPantallas.Camara)
            }
        }

        //llamamos la funcion que muestra el texto para pedir los permisos, solo el texto
        UIPermisosHelper.textPedirPermisoCamara(
            tienePermiso = tienePermiso.value,
            onClickPedirPermiso = { launcher.launch(Manifest.permission.CAMERA) }
        )
    } // PantallaPermisoCamara()



} // object PermisosHelper



