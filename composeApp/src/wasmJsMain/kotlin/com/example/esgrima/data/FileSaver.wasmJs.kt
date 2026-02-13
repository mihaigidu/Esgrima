package com.example.esgrima.data

/**
 * Implementación para Web (Wasm).
 * Mostramos el contenido por consola para verificar el funcionamiento.
 */
actual fun platformSave(fileName: String, content: String) {
    println("--- SIMULACIÓN DE GUARDADO (WEB) ---")
    println("Archivo: $fileName")
    println("Contenido:\n$content")
    println("------------------------------------")
}
