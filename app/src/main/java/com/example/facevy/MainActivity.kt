package com.example.facevy

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.facevy.ui.theme.FaceVYTheme
import com.example.facevy.permisos.PermisosHelper
import com.example.facevy.ui.theme.UIPermisosHelper
import android.Manifest
import android.app.Activity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.activity.compose.LocalActivity



import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.ui.platform.LocalContext

/*
en este archivo se llamaran a todas las funciones que permitiran que funcione la app
*/

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FaceVYTheme {

                //pantalla para medir permiso al usuario de usar la camara
                PermisosHelper.PantallaPermisoCamara()

            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    FaceVYTheme {

    }
}





