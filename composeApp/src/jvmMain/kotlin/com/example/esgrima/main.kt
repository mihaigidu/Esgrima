package com.example.esgrima

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
    Window(onCloseRequest = ::exitApplication, title = "Esgrima Manager PC") {
        // --- ¡ESTA LÍNEA ES LA CLAVE! ---
        // Si falta esto, la ventana se abre pero no pinta nada dentro.
        App()
        // --------------------------------
    }
}