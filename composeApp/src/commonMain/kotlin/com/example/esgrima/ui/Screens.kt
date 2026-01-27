package com.example.esgrima.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.esgrima.data.Repository
import com.example.esgrima.model.*

@Composable
fun LoginScreen(onLogin: () -> Unit) {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("Esgrima Manager 2026", style = MaterialTheme.typography.h4, color = MaterialTheme.colors.primary)
            Spacer(Modifier.height(16.dp))
            Button(onClick = onLogin) { Text("Entrar como Administrador") }
        }
    }
}

@Composable
fun TiradoresScreen() {
    var nombre by remember { mutableStateOf("") }
    var club by remember { mutableStateOf("") }
    // Estado local para refrescar la lista
    var lista by remember { mutableStateOf(Repository.datos.tiradores.toList()) }

    Column(Modifier.padding(16.dp)) {
        Text("Tiradores Inscritos: ${lista.size}", style = MaterialTheme.typography.h5)
        Spacer(Modifier.height(10.dp))
        Row {
            OutlinedTextField(value = nombre, onValueChange = { nombre = it }, label = { Text("Nombre") })
            Spacer(Modifier.width(8.dp))
            OutlinedTextField(value = club, onValueChange = { club = it }, label = { Text("Club") })
            Spacer(Modifier.width(8.dp))
            Button(onClick = {
                if (nombre.isNotBlank()) {
                    val nuevo = Tirador(
                        firstName = nombre, lastName = "", club = club,
                        gender = "X", age = 18, federateNumber = (0..999999).random(),
                        country = "ESP", modality = "Espada"
                    )
                    Repository.datos.tiradores.add(nuevo)
                    lista = Repository.datos.tiradores.toList()
                    nombre = ""; club = ""
                }
            }) { 
                Icon(Icons.Default.Add, contentDescription = "Añadir")
                Spacer(Modifier.width(4.dp))
                Text("Inscribir") 
            }
        }
        Spacer(Modifier.height(16.dp))
        LazyColumn {
            items(lista) { t ->
                Card(Modifier.padding(4.dp).fillMaxWidth(), elevation = 2.dp) {
                    Text("🤺 ${t.nombreCompleto} (${t.modality}) - ${t.club}", Modifier.padding(16.dp))
                }
            }
        }
    }
}

@Composable
fun ArbitrosScreen() {
    val lista by remember { mutableStateOf(Repository.datos.arbitros.toList()) }
    Column(Modifier.padding(16.dp)) {
        Text("Árbitros Disponibles", style = MaterialTheme.typography.h5)
        Spacer(Modifier.height(10.dp))
        LazyColumn {
            items(lista) { a ->
                Card(Modifier.padding(4.dp).fillMaxWidth(), elevation = 2.dp) {
                    Text("⚖️ ${a.nombreCompleto} (Licencia: ${a.federateNumber})", Modifier.padding(16.dp))
                }
            }
        }
    }
}

@Composable
fun PoulesScreen() {
    var poules by remember { mutableStateOf(Repository.datos.poules.toList()) }
    var ranking by remember { mutableStateOf(Repository.calcularRanking()) }

    Row(Modifier.fillMaxSize()) {
        // ZONA DE POULES
        Column(Modifier.weight(0.7f).padding(16.dp).verticalScroll(rememberScrollState())) {
            Button(onClick = {
                Repository.generarPoules(4)
                poules = Repository.datos.poules.toList()
                ranking = Repository.calcularRanking()
            }) { Text("Generar / Resetear Poules") }

            Spacer(Modifier.height(10.dp))

            poules.forEach { p ->
                Card(Modifier.padding(vertical = 8.dp).fillMaxWidth(), elevation = 4.dp) {
                    Column(Modifier.padding(16.dp)) {
                        Text("Poule ${p.numero} - ${p.pista}", style = MaterialTheme.typography.h6, color = MaterialTheme.colors.primaryVariant)
                        Divider(Modifier.padding(vertical = 8.dp))
                        p.asaltos.forEach { asalto ->
                            RowAsalto(asalto) { ranking = Repository.calcularRanking() }
                        }
                    }
                }
            }
        }
        // ZONA DE RANKING
        Column(Modifier.weight(0.3f).fillMaxHeight().background(Color(0xFFEEEEEE)).padding(16.dp)) {
            Text("Clasificación (V/M)", style = MaterialTheme.typography.h6)
            Spacer(Modifier.height(10.dp))
            LazyColumn {
                items(ranking) { s ->
                    Column(Modifier.padding(vertical = 4.dp)) {
                        Row {
                            Text("${s.victorias}V", style = MaterialTheme.typography.body2, color = Color.Blue)
                            Spacer(Modifier.width(8.dp))
                            Text(s.tirador.nombreCompleto, style = MaterialTheme.typography.body2)
                        }
                        Text("Ind: ${s.indice} (TD:${s.td} TR:${s.tr})", style = MaterialTheme.typography.caption, color = Color.Gray)
                        Divider()
                    }
                }
            }
        }
    }
}

@Composable
fun TablonScreen() {
    var asaltos by remember { mutableStateOf(Repository.datos.cuadroEliminatorio.toList()) }
    Column(Modifier.padding(16.dp)) {
        Button(onClick = {
            Repository.generarTablon()
            asaltos = Repository.datos.cuadroEliminatorio.toList()
        }) { Text("Generar Tablón Eliminatorio") }

        Spacer(Modifier.height(10.dp))

        if (asaltos.isEmpty()) Text("Primero debes terminar las poules.")

        LazyColumn {
            items(asaltos) { a ->
                Card(Modifier.padding(8.dp).fillMaxWidth(), elevation = 4.dp) {
                    Column(Modifier.padding(16.dp)) {
                        Text("Fase: ${a.rondaEliminatoria}", color = MaterialTheme.colors.secondary, style = MaterialTheme.typography.caption)
                        RowAsalto(a) {}
                    }
                }
            }
        }
    }
}

@Composable
fun RowAsalto(asalto: Asalto, onUpdate: () -> Unit) {
    var t1 by remember { mutableStateOf(asalto.tocados1.toString()) }
    var t2 by remember { mutableStateOf(asalto.tocados2.toString()) }

    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 4.dp)) {
        Text(asalto.tirador1.nombreCompleto, Modifier.weight(1f))

        OutlinedTextField(
            value = t1,
            onValueChange = { 
                t1 = it
                asalto.tocados1 = it.toIntOrNull() ?: 0
                asalto.finalizado = true
                onUpdate() 
            },
            modifier = Modifier.width(60.dp),
            singleLine = true
        )

        Text("-", Modifier.padding(horizontal = 8.dp))

        OutlinedTextField(
            value = t2,
            onValueChange = { 
                t2 = it
                asalto.tocados2 = it.toIntOrNull() ?: 0
                asalto.finalizado = true
                onUpdate() 
            },
            modifier = Modifier.width(60.dp),
            singleLine = true
        )

        Text(asalto.tirador2?.nombreCompleto ?: "BYE", Modifier.weight(1f), textAlign = androidx.compose.ui.text.style.TextAlign.End)
    }
}