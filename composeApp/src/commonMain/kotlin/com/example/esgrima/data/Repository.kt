package com.example.esgrima.data

import com.example.esgrima.model.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

object Repository {
    // Estado único de la aplicación (Singleton)
    var datos = CompeticionData()

    // Configuración JSON para ser flexible
    private val jsonFormatter = Json {
        prettyPrint = true
        ignoreUnknownKeys = true
        coerceInputValues = true
    }

    // --- GUARDADO ---
    fun guardar() {
        try {
            val jsonString = jsonFormatter.encodeToString(datos)
            platformSave("datos_competicion.json", jsonString)
        } catch (e: Exception) {
            println("Error al serializar: ${e.message}")
        }
    }

    // --- CARGA ---
    fun cargar() {
        // En esta versión, cargamos siempre los datos del profesor al inicio
        // Si ya existiera un fichero guardado, aquí leeríamos ese fichero primero.
        cargarDatosProfesor()
    }

    private fun cargarDatosProfesor() {
        try {
            val listaTiradores: List<Tirador> = jsonFormatter.decodeFromString(JSON_TIRADORES_RAW)
            val listaArbitros: List<Arbitro> = jsonFormatter.decodeFromString(JSON_ARBITROS_RAW)

            datos.tiradores.clear()
            datos.arbitros.clear()
            datos.tiradores.addAll(listaTiradores)
            datos.arbitros.addAll(listaArbitros)

            println("Datos cargados OK: ${datos.tiradores.size} tiradores, ${datos.arbitros.size} árbitros.")
        } catch (e: Exception) {
            println("Error procesando JSON inicial: ${e.message}")
            e.printStackTrace()
        }
    }

    // --- GENERACIÓN DE POULES ---
    fun generarPoules(numPistas: Int) {
        datos.poules.clear()
        datos.cuadroEliminatorio.clear()
        if (datos.tiradores.isEmpty()) return

        val grupos = datos.tiradores.shuffled().chunked(6) // Grupos de 6 aprox

        grupos.forEachIndexed { index, miembros ->
            val num = index + 1
            val nuevaPoule = Poule(
                numero = num,
                pista = "Pista $num",
                arbitro = datos.arbitros.firstOrNull(),
                tiradores = miembros
            )
            // Todos contra todos
            for (i in miembros.indices) {
                for (j in i + 1 until miembros.size) {
                    nuevaPoule.asaltos.add(
                        Asalto(
                            id = "P$num-${miembros[i].federateNumber}-${miembros[j].federateNumber}",
                            tirador1 = miembros[i],
                            tirador2 = miembros[j],
                            esPoule = true
                        )
                    )
                }
            }
            datos.poules.add(nuevaPoule)
        }
    }

    // --- RANKING ---
    data class Estadistica(
        val tirador: Tirador,
        val victorias: Int,
        val asaltos: Int,
        val td: Int,
        val tr: Int
    ) {
        val indice get() = td - tr
        val coeficiente get() = if (asaltos > 0) victorias.toDouble() / asaltos else 0.0
    }

    fun calcularRanking(): List<Estadistica> {
        return datos.tiradores.map { t ->
            val misAsaltos = datos.poules.flatMap { it.asaltos }
                .filter { it.finalizado && (it.tirador1.id == t.id || it.tirador2?.id == t.id) }

            var v = 0; var td = 0; var tr = 0

            misAsaltos.forEach { a ->
                val soyT1 = a.tirador1.id == t.id
                val misPuntos = if (soyT1) a.tocados1 else a.tocados2
                val susPuntos = if (soyT1) a.tocados2 else a.tocados1
                td += misPuntos; tr += susPuntos
                if (misPuntos > susPuntos) v++
            }
            Estadistica(t, v, misAsaltos.size, td, tr)
        }.sortedWith(
            compareByDescending<Estadistica> { it.coeficiente }
                .thenByDescending { it.indice }
                .thenByDescending { it.td }
        )
    }

    // --- TABLÓN (ELIMINATORIAS) ---
    fun generarTablon() {
        val ranking = calcularRanking().map { it.tirador }
        if (ranking.size < 2) return
        datos.cuadroEliminatorio.clear()

        // Calcular tamaño del cuadro (potencia de 2: 4, 8, 16...)
        var tamano = 2
        while (tamano < ranking.size) tamano *= 2

        val listaAjustada = ranking.toMutableList()

        // Rellenar huecos con "BYE"
        while (listaAjustada.size < tamano) {
            listaAjustada.add(Tirador("BYE", "", "", 0, 9999, "", "", "ESP"))
        }

        val fase = when(tamano) {
            4 -> Fase.SEMIFINAL
            8 -> Fase.CUARTOS
            16 -> Fase.ELIMINATORIAS_16
            else -> Fase.ELIMINATORIAS_32
        }

        // Emparejar: 1º vs Último, 2º vs Penúltimo...
        for (i in 0 until tamano / 2) {
            val t1 = listaAjustada[i]
            val t2 = listaAjustada[tamano - 1 - i]

            datos.cuadroEliminatorio.add(
                Asalto(
                    id = "E-${fase}-$i",
                    tirador1 = t1,
                    tirador2 = if (t2.firstName == "BYE") null else t2,
                    esPoule = false,
                    rondaEliminatoria = fase
                )
            )
        }
    }
}