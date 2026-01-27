package com.example.esgrima.data

import java.io.File

actual fun platformSave(fileName: String, content: String) {
    val file = File(fileName)
    file.writeText(content)
    println("✅ Archivo guardado en PC: ${file.absolutePath}")
}