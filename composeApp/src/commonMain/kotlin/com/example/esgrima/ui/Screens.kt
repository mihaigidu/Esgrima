package com.example.esgrima.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.esgrima.data.Repository
import com.example.esgrima.model.*

@Composable
fun TiradoresScreen() {
    var nombre by remember { mutableStateOf("") }
    var club by remember { mutableStateOf("") }
    var lista by remember { mutableStateOf(Repository.datos.tiradores.toList()) }

    Column(Modifier.padding(16.dp).fillMaxSize()) {
        Text("Gestión de Tiradores", style = MaterialTheme.typography.h5, fontWeight = FontWeight.Bold)
        Text("${lista.size} inscritos actualmente", color = Color.Gray)
        
        Spacer(Modifier.height(16.dp))
        
        Card(elevation = 4.dp, shape = RoundedCornerShape(12.dp)) {
            Column(Modifier.padding(16.dp)) {
                OutlinedTextField(
                    value = nombre, 
                    onValueChange = { nombre = it }, 
                    label = { Text("Nombre Completo") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = club, 
                    onValueChange = { club = it }, 
                    label = { Text("Club / Entidad") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(12.dp))
                Button(
                    onClick = {
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
                    },
                    modifier = Modifier.fillMaxWidth().height(48.dp)
                ) {
                    Icon(Icons.Default.Add, null)
                    Spacer(Modifier.width(8.dp))
                    Text("INSCRIBIR TIRADOR")
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        LazyColumn(Modifier.weight(1f)) {
            items(lista) { t ->
                Card(Modifier.padding(vertical = 4.dp).fillMaxWidth(), elevation = 2.dp, shape = RoundedCornerShape(8.dp)) {
                    Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                        Surface(Modifier.size(40.dp), shape = RoundedCornerShape(20.dp), color = MaterialTheme.colors.primary.copy(alpha = 0.1f)) {
                            Box(contentAlignment = Alignment.Center) {
                                Text("🤺", fontSize = 20.sp)
                            }
                        }
                        Spacer(Modifier.width(16.dp))
                        Column {
                            Text(t.nombreCompleto, fontWeight = FontWeight.Bold)
                            Text(t.club, style = MaterialTheme.typography.caption)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ArbitrosScreen() {
    var nombre by remember { mutableStateOf("") }
    var licencia by remember { mutableStateOf("") }
    var lista by remember { mutableStateOf(Repository.datos.arbitros.toList()) }

    Column(Modifier.padding(16.dp).fillMaxSize()) {
        Text("Gestión de Árbitros", style = MaterialTheme.typography.h5, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(16.dp))

        Card(elevation = 4.dp, shape = RoundedCornerShape(12.dp)) {
            Column(Modifier.padding(16.dp)) {
                OutlinedTextField(
                    value = nombre, onValueChange = { nombre = it }, 
                    label = { Text("Nombre del Árbitro") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = licencia, onValueChange = { licencia = it }, 
                    label = { Text("Número de Licencia") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(12.dp))
                Button(onClick = {
                    if (nombre.isNotBlank()) {
                        val numLicencia = licencia.toIntOrNull() ?: (0..99999).random()
                        val nuevo = Arbitro(
                            firstName = nombre, lastName = "", gender = "X", 
                            age = 30, federateNumber = numLicencia, club = "FEDERACIÓN",
                            country = "ESP", modality = listOf("Espada")
                        )
                        Repository.datos.arbitros.add(nuevo)
                        lista = Repository.datos.arbitros.toList()
                        nombre = ""; licencia = ""
                    }
                }, modifier = Modifier.fillMaxWidth().height(48.dp)) {
                    Text("AÑADIR ÁRBITRO")
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        LazyColumn {
            items(lista) { a ->
                Card(Modifier.padding(vertical = 4.dp).fillMaxWidth(), elevation = 2.dp) {
                    Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                        Text("⚖️", fontSize = 24.sp)
                        Spacer(Modifier.width(16.dp))
                        Column {
                            Text(a.nombreCompleto, fontWeight = FontWeight.Bold)
                            Text("Licencia: ${a.federateNumber}", style = MaterialTheme.typography.caption)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PoulesScreen() {
    var poules by remember { mutableStateOf(Repository.datos.poules.toList()) }
    var ranking by remember { mutableStateOf(Repository.calcularRanking()) }

    Column(Modifier.padding(16.dp).fillMaxSize().verticalScroll(rememberScrollState())) {
        Text("Competición de Poules", style = MaterialTheme.typography.h5, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(12.dp))

        Button(
            onClick = {
                Repository.generarPoules(4)
                poules = Repository.datos.poules.toList()
                ranking = Repository.calcularRanking()
            },
            modifier = Modifier.fillMaxWidth()
        ) { 
            Text("REGENERAR GRUPOS (POULES)") 
        }

        Spacer(Modifier.height(24.dp))

        Text("Resultados y Cruces", style = MaterialTheme.typography.h6)
        poules.forEach { p ->
            Card(Modifier.padding(vertical = 8.dp).fillMaxWidth(), elevation = 4.dp, shape = RoundedCornerShape(8.dp)) {
                Column(Modifier.padding(16.dp)) {
                    Text("POULE ${p.numero} - ${p.pista}", color = MaterialTheme.colors.primary, fontWeight = FontWeight.Bold)
                    Divider(Modifier.padding(vertical = 8.dp))
                    p.asaltos.forEach { asalto ->
                        RowAsalto(asalto) { ranking = Repository.calcularRanking() }
                    }
                }
            }
        }

        Spacer(Modifier.height(24.dp))
        
        Text("Clasificación Provisional", style = MaterialTheme.typography.h6)
        Card(elevation = 2.dp, modifier = Modifier.fillMaxWidth()) {
            Column(Modifier.padding(8.dp)) {
                ranking.forEachIndexed { index, s ->
                    Row(Modifier.padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
                        Text("${index + 1}º", Modifier.width(30.dp), fontWeight = FontWeight.Bold)
                        Column(Modifier.weight(1f)) {
                            Text(s.tirador.nombreCompleto)
                            Text("V/M: ${s.victorias} | Ind: ${s.indice}", style = MaterialTheme.typography.caption)
                        }
                    }
                    if (index < ranking.size - 1) Divider()
                }
            }
        }
    }
}

@Composable
fun TablonScreen() {
    var asaltos by remember { mutableStateOf(Repository.datos.cuadroEliminatorio.toList()) }
    Column(Modifier.padding(16.dp).fillMaxSize()) {
        Text("Tablón Eliminatorio", style = MaterialTheme.typography.h5, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(16.dp))

        Button(onClick = {
            Repository.generarTablon()
            asaltos = Repository.datos.cuadroEliminatorio.toList()
        }, modifier = Modifier.fillMaxWidth()) {
            Text("GENERAR ÁRBOL FINAL")
        }

        Spacer(Modifier.height(16.dp))

        if (asaltos.isEmpty()) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Finaliza primero las poules para generar el tablón.", color = Color.Gray)
            }
        }

        LazyColumn {
            items(asaltos) { a ->
                Card(Modifier.padding(vertical = 8.dp).fillMaxWidth(), elevation = 4.dp) {
                    Column(Modifier.padding(16.dp)) {
                        Text(
                            text = a.rondaEliminatoria?.name ?: "ELIMINATORIA", 
                            color = MaterialTheme.colors.secondary, 
                            fontWeight = FontWeight.Bold, 
                            fontSize = 12.sp
                        )
                        Spacer(Modifier.height(8.dp))
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

    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 8.dp)) {
        Text(asalto.tirador1.nombreCompleto.split(" ").first(), Modifier.weight(1f), fontSize = 14.sp)

        OutlinedTextField(
            value = t1,
            onValueChange = { 
                t1 = it
                asalto.tocados1 = it.toIntOrNull() ?: 0
                asalto.finalizado = true
                onUpdate() 
            },
            modifier = Modifier.width(55.dp),
            singleLine = true,
            textStyle = LocalTextStyle.current.copy(textAlign = androidx.compose.ui.text.style.TextAlign.Center)
        )

        Text(" vs ", Modifier.padding(horizontal = 4.dp), color = Color.Gray)

        OutlinedTextField(
            value = t2,
            onValueChange = { 
                t2 = it
                asalto.tocados2 = it.toIntOrNull() ?: 0
                asalto.finalizado = true
                onUpdate() 
            },
            modifier = Modifier.width(55.dp),
            singleLine = true,
            textStyle = LocalTextStyle.current.copy(textAlign = androidx.compose.ui.text.style.TextAlign.Center)
        )

        Text(asalto.tirador2?.nombreCompleto?.split(" ")?.first() ?: "BYE", Modifier.weight(1f), textAlign = androidx.compose.ui.text.style.TextAlign.End, fontSize = 14.sp)
    }
}