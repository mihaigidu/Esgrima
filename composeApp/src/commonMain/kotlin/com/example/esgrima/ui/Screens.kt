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
import androidx.compose.material.icons.filled.Delete
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
fun TiradoresScreen(canEdit: Boolean = true) {
    var nombre by remember { mutableStateOf("") }
    var club by remember { mutableStateOf("") }
    var lista by remember { mutableStateOf(Repository.datos.tiradores.toList()) }

    Column(Modifier.padding(16.dp).fillMaxSize()) {
        Text("Gestión de Tiradores", style = MaterialTheme.typography.h5, fontWeight = FontWeight.Bold)
        Text("${lista.size} tiradores en el sistema", color = Color.Gray)
        
        Spacer(Modifier.height(16.dp))
        
        if (canEdit) {
            Card(elevation = 4.dp, shape = RoundedCornerShape(12.dp)) {
                Column(Modifier.padding(16.dp)) {
                    Row(Modifier.fillMaxWidth()) {
                        OutlinedTextField(
                            value = nombre, onValueChange = { nombre = it }, 
                            label = { Text("Nombre Completo") },
                            modifier = Modifier.weight(1f)
                        )
                        Spacer(Modifier.width(8.dp))
                        OutlinedTextField(
                            value = club, onValueChange = { club = it }, 
                            label = { Text("Club / Entidad") },
                            modifier = Modifier.weight(1f)
                        )
                    }
                    Spacer(Modifier.height(12.dp))
                    Button(
                        onClick = {
                            if (nombre.isNotBlank()) {
                                val nuevo = Tirador(
                                    firstName = nombre, lastName = "", club = club,
                                    gender = "X", age = 18, federateNumber = (0..999999).random(),
                                    country = "ESP", modality = Repository.datos.arma
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
        }

        LazyColumn(Modifier.weight(1f)) {
            items(lista) { t ->
                Card(Modifier.padding(vertical = 4.dp).fillMaxWidth(), elevation = 2.dp) {
                    Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                        Text("🤺", fontSize = 20.sp)
                        Spacer(Modifier.width(16.dp))
                        Column(Modifier.weight(1f)) {
                            Text(t.nombreCompleto, fontWeight = FontWeight.Bold)
                            Text(t.club, style = MaterialTheme.typography.caption)
                        }
                        if (canEdit) {
                            IconButton(onClick = {
                                Repository.datos.tiradores.remove(t)
                                lista = Repository.datos.tiradores.toList()
                            }) {
                                Icon(Icons.Default.Delete, "Borrar", tint = Color.Gray)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ArbitrosScreen(canEdit: Boolean = true) {
    var nombre by remember { mutableStateOf("") }
    var licencia by remember { mutableStateOf("") }
    var lista by remember { mutableStateOf(Repository.datos.arbitros.toList()) }

    Column(Modifier.padding(16.dp).fillMaxSize()) {
        Text("Gestión de Árbitros", style = MaterialTheme.typography.h5, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(16.dp))

        if (canEdit) {
            Card(elevation = 4.dp, shape = RoundedCornerShape(12.dp)) {
                Column(Modifier.padding(16.dp)) {
                    OutlinedTextField(value = nombre, onValueChange = { nombre = it }, label = { Text("Nombre") }, modifier = Modifier.fillMaxWidth())
                    Spacer(Modifier.height(8.dp))
                    OutlinedTextField(value = licencia, onValueChange = { licencia = it }, label = { Text("Licencia") }, modifier = Modifier.fillMaxWidth())
                    Spacer(Modifier.height(12.dp))
                    Button(onClick = {
                        if (nombre.isNotBlank()) {
                            val nuevo = Arbitro(nombre, "", "X", 30, licencia.toIntOrNull() ?: 0, "FED", "ESP", listOf(Repository.datos.arma))
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
        }

        LazyColumn(Modifier.weight(1f)) {
            items(lista) { a ->
                Card(Modifier.padding(vertical = 4.dp).fillMaxWidth(), elevation = 2.dp) {
                    Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                        Text("⚖️", fontSize = 24.sp)
                        Spacer(Modifier.width(16.dp))
                        Column(Modifier.weight(1f)) {
                            Text(a.nombreCompleto, fontWeight = FontWeight.Bold)
                            Text("Licencia: ${a.federateNumber}", style = MaterialTheme.typography.caption)
                        }
                        if (canEdit) {
                            IconButton(onClick = {
                                Repository.datos.arbitros.remove(a)
                                lista = Repository.datos.arbitros.toList()
                            }) {
                                Icon(Icons.Default.Delete, null, tint = Color.Gray)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AjustesScreen() {
    var nombre by remember { mutableStateOf(Repository.datos.nombre) }
    var entidad by remember { mutableStateOf(Repository.datos.entidadOrganizadora) }
    var lugar by remember { mutableStateOf(Repository.datos.lugar) }
    var fecha by remember { mutableStateOf(Repository.datos.fecha) }
    val armas = listOf("Espada", "Florete", "Sable")
    var expanded by remember { mutableStateOf(false) }

    Column(Modifier.padding(16.dp).fillMaxSize().verticalScroll(rememberScrollState())) {
        Text("Configuración de la Competición", style = MaterialTheme.typography.h5, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(16.dp))

        Card(elevation = 4.dp, shape = RoundedCornerShape(12.dp)) {
            Column(Modifier.padding(16.dp)) {
                OutlinedTextField(value = nombre, onValueChange = { nombre = it; Repository.datos.nombre = it }, label = { Text("Nombre del Torneo") }, modifier = Modifier.fillMaxWidth())
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(value = entidad, onValueChange = { entidad = it; Repository.datos.entidadOrganizadora = it }, label = { Text("Entidad Organizadora") }, modifier = Modifier.fillMaxWidth())
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(value = lugar, onValueChange = { lugar = it; Repository.datos.lugar = it }, label = { Text("Lugar") }, modifier = Modifier.fillMaxWidth())
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(value = fecha, onValueChange = { fecha = it; Repository.datos.fecha = it }, label = { Text("Fecha") }, modifier = Modifier.fillMaxWidth())
                
                Spacer(Modifier.height(16.dp))
                Text("Arma", style = MaterialTheme.typography.subtitle2)
                Box {
                    OutlinedButton(onClick = { expanded = true }, modifier = Modifier.fillMaxWidth()) { Text(Repository.datos.arma) }
                    DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                        armas.forEach { arma ->
                            DropdownMenuItem(onClick = { Repository.datos.arma = arma; expanded = false }) { Text(arma) }
                        }
                    }
                }
                Spacer(Modifier.height(24.dp))
                Button(onClick = { Repository.guardar() }, modifier = Modifier.fillMaxWidth()) { Text("GUARDAR TODO") }
            }
        }
    }
}

@Composable
fun PoulesScreen(isAdmin: Boolean = false) {
    var poules by remember { mutableStateOf(Repository.datos.poules.toList()) }
    var ranking by remember { mutableStateOf(Repository.calcularRanking()) }
    var numPistas by remember { mutableStateOf(4) }

    Column(Modifier.padding(16.dp).fillMaxSize().verticalScroll(rememberScrollState())) {
        Text("Fase de Poules", style = MaterialTheme.typography.h5, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(12.dp))

        if (isAdmin) {
            Card(elevation = 4.dp, shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth()) {
                Column(Modifier.padding(16.dp)) {
                    Text("Configuración de Grupos", fontWeight = FontWeight.Bold)
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("Número de Pistas: $numPistas")
                        Slider(
                            value = numPistas.toFloat(),
                            onValueChange = { numPistas = it.toInt() },
                            valueRange = 1f..10f,
                            steps = 8,
                            modifier = Modifier.weight(1f).padding(horizontal = 16.dp)
                        )
                    }
                    Button(
                        onClick = {
                            Repository.generarPoules(numPistas)
                            poules = Repository.datos.poules.toList()
                            ranking = Repository.calcularRanking()
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) { Text("GENERAR POULES") }
                }
            }
            Spacer(Modifier.height(24.dp))
        }

        Text("Resultados", style = MaterialTheme.typography.h6)
        poules.forEach { p ->
            Card(Modifier.padding(vertical = 8.dp).fillMaxWidth(), elevation = 4.dp) {
                Column(Modifier.padding(16.dp)) {
                    Text("POULE ${p.numero} - ${p.pista}", color = MaterialTheme.colors.primary, fontWeight = FontWeight.Bold)
                    p.asaltos.forEach { asalto ->
                        RowAsalto(asalto, limite = 5) { ranking = Repository.calcularRanking() }
                    }
                }
            }
        }

        Spacer(Modifier.height(24.dp))
        Text("Clasificación Provisional", style = MaterialTheme.typography.h6)
        Card(elevation = 2.dp, modifier = Modifier.fillMaxWidth()) {
            Column(Modifier.padding(8.dp)) {
                ranking.forEachIndexed { index, s ->
                    Row(Modifier.padding(8.dp)) {
                        Text("${index + 1}º", Modifier.width(30.dp), fontWeight = FontWeight.Bold)
                        Text(s.tirador.nombreCompleto, Modifier.weight(1f))
                        Text("${s.victorias}V - Ind: ${s.indice}")
                    }
                    if (index < ranking.size - 1) Divider()
                }
            }
        }
    }
}

@Composable
fun TablonScreen(isAdmin: Boolean = false) {
    var asaltos by remember { mutableStateOf(Repository.datos.cuadroEliminatorio.toList()) }
    Column(Modifier.padding(16.dp).fillMaxSize()) {
        Text("Tablón Eliminatorio", style = MaterialTheme.typography.h5, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(16.dp))

        if (isAdmin) {
            Button(onClick = {
                Repository.generarTablon()
                asaltos = Repository.datos.cuadroEliminatorio.toList()
            }, modifier = Modifier.fillMaxWidth()) { Text("GENERAR CUADRO FINAL") }
            Spacer(Modifier.height(16.dp))
        }

        LazyColumn(Modifier.weight(1f)) {
            items(asaltos) { a ->
                Card(Modifier.padding(vertical = 8.dp).fillMaxWidth(), elevation = 4.dp) {
                    Column(Modifier.padding(16.dp)) {
                        Text(a.rondaEliminatoria?.name ?: "ASALTO", color = MaterialTheme.colors.secondary, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        RowAsalto(a, limite = 15) {}
                    }
                }
            }
        }
    }
}

@Composable
fun RowAsalto(asalto: Asalto, limite: Int, onUpdate: () -> Unit) {
    var t1 by remember { mutableStateOf(asalto.tocados1.toString()) }
    var t2 by remember { mutableStateOf(asalto.tocados2.toString()) }

    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 4.dp)) {
        Text(asalto.tirador1.nombreCompleto.split(" ").first(), Modifier.weight(1f))
        OutlinedTextField(
            value = t1,
            onValueChange = { 
                t1 = it; asalto.tocados1 = it.toIntOrNull() ?: 0
                if (asalto.tocados1 >= limite) asalto.finalizado = true
                onUpdate() 
            },
            modifier = Modifier.width(50.dp),
            singleLine = true
        )
        Text(" - ", Modifier.padding(horizontal = 4.dp))
        OutlinedTextField(
            value = t2,
            onValueChange = { 
                t2 = it; asalto.tocados2 = it.toIntOrNull() ?: 0
                if (asalto.tocados2 >= limite) asalto.finalizado = true
                onUpdate() 
            },
            modifier = Modifier.width(50.dp),
            singleLine = true
        )
        Text(asalto.tirador2?.nombreCompleto?.split(" ")?.first() ?: "BYE", Modifier.weight(1f), textAlign = androidx.compose.ui.text.style.TextAlign.End)
    }
}