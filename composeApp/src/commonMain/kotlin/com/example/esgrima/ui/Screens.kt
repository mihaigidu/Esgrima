package com.example.esgrima.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.esgrima.data.Repository
import com.example.esgrima.model.*

// Lista común de federaciones para toda la app
private val LISTA_PAISES = listOf(
    "ESPAÑA", "FRANCIA", "ITALIA", "GERMANY", "PORTUGAL", "GBR", "USA", "HUN", "UKR",
    "CHN", "JPN", "CAN", "MEX", "ARG", "BRA", "CHI", "COL", "ROU"
).sorted()

@Composable
fun TiradoresScreen(canEdit: Boolean = true) {
    var nombre by remember { mutableStateOf("") }
    var apellidos by remember { mutableStateOf("") }
    var club by remember { mutableStateOf("") }
    var licencia by remember { mutableStateOf("") }
    var edad by remember { mutableStateOf("") }
    var genero by remember { mutableStateOf("M") }
    var pais by remember { mutableStateOf("ESP") }
    var paisExpanded by remember { mutableStateOf(false) }
    
    var isFormVisible by remember { mutableStateOf(false) }
    var lista by remember { mutableStateOf(Repository.datos.tiradores.toList()) }

    Column(Modifier.padding(16.dp).fillMaxSize()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text("Inscripción de Tiradores", style = MaterialTheme.typography.h5, fontWeight = FontWeight.Bold)
                Text("${lista.size} participantes inscritos en ${Repository.datos.arma}", color = Color.Gray)
            }
            if (canEdit) {
                IconButton(onClick = { isFormVisible = !isFormVisible }) {
                    Icon(
                        if (isFormVisible) Icons.Default.KeyboardArrowUp else Icons.Default.Add,
                        contentDescription = if (isFormVisible) "Ocultar formulario" else "Mostrar formulario",
                        tint = MaterialTheme.colors.primary
                    )
                }
            }
        }
        
        Spacer(Modifier.height(16.dp))
        
        if (canEdit) {
            AnimatedVisibility(visible = isFormVisible) {
                Card(elevation = 4.dp, shape = RoundedCornerShape(12.dp), modifier = Modifier.padding(bottom = 16.dp)) {
                    Column(Modifier.padding(16.dp)) {
                        Text("Datos del Tirador", style = MaterialTheme.typography.subtitle2, color = MaterialTheme.colors.primary)
                        Spacer(Modifier.height(12.dp))
                        
                        Row(Modifier.fillMaxWidth()) {
                            OutlinedTextField(value = nombre, onValueChange = { nombre = it }, label = { Text("Nombre") }, modifier = Modifier.weight(1f), singleLine = true)
                            Spacer(Modifier.width(8.dp))
                            OutlinedTextField(value = apellidos, onValueChange = { apellidos = it }, label = { Text("Apellidos") }, modifier = Modifier.weight(1f), singleLine = true)
                        }
                        
                        Spacer(Modifier.height(8.dp))
                        
                        Row(Modifier.fillMaxWidth()) {
                            OutlinedTextField(value = club, onValueChange = { club = it }, label = { Text("Club") }, modifier = Modifier.weight(1f), singleLine = true)
                            Spacer(Modifier.width(8.dp))
                            OutlinedTextField(value = licencia, onValueChange = { licencia = it }, label = { Text("Nº Licencia") }, modifier = Modifier.weight(1f), singleLine = true)
                        }
                        
                        Spacer(Modifier.height(8.dp))
                        
                        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                            OutlinedTextField(value = edad, onValueChange = { edad = it }, label = { Text("Edad") }, modifier = Modifier.weight(0.5f), singleLine = true)
                            Spacer(Modifier.width(8.dp))
                            
                            Box(modifier = Modifier.weight(1f)) {
                                OutlinedButton(
                                    onClick = { paisExpanded = true }, 
                                    modifier = Modifier.fillMaxWidth().height(56.dp).padding(top = 4.dp),
                                    shape = RoundedCornerShape(4.dp),
                                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Black)
                                ) {
                                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                                        Text(pais)
                                        Icon(Icons.Default.ArrowDropDown, null)
                                    }
                                }
                                DropdownMenu(expanded = paisExpanded, onDismissRequest = { paisExpanded = false }) {
                                    LISTA_PAISES.forEach { p -> DropdownMenuItem(onClick = { pais = p; paisExpanded = false }) { Text(p) } }
                                }
                            }
                            
                            Spacer(Modifier.width(16.dp))
                            
                            Column {
                                Text("Género", style = MaterialTheme.typography.caption)
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.clickable { genero = "M" }) {
                                        RadioButton(selected = genero == "M", onClick = { genero = "M" })
                                        Text("M", fontSize = 12.sp)
                                    }
                                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.clickable { genero = "F" }) {
                                        RadioButton(selected = genero == "F", onClick = { genero = "F" })
                                        Text("F", fontSize = 12.sp)
                                    }
                                }
                            }
                        }
                        
                        Spacer(Modifier.height(16.dp))
                        
                        Button(
                            onClick = {
                                if (nombre.isNotBlank() && apellidos.isNotBlank()) {
                                    val nuevo = Tirador(
                                        firstName = nombre, lastName = apellidos, club = club, gender = genero, 
                                        age = edad.toIntOrNull() ?: 18, 
                                        federateNumber = licencia.toIntOrNull() ?: (0..999999).random(),
                                        country = pais, modality = Repository.datos.arma
                                    )
                                    Repository.datos.tiradores.add(nuevo)
                                    lista = Repository.datos.tiradores.toList()
                                    nombre = ""; apellidos = ""; club = ""; licencia = ""; edad = ""; pais = "ESP"
                                    isFormVisible = false // Ocultar tras añadir
                                }
                            },
                            modifier = Modifier.fillMaxWidth().height(48.dp)
                        ) {
                            Icon(Icons.Default.Add, null)
                            Spacer(Modifier.width(8.dp))
                            Text("CONFIRMAR INSCRIPCIÓN", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        Text("Lista de Inscritos", style = MaterialTheme.typography.h6)
        LazyColumn(Modifier.weight(1f)) {
            items(lista) { t ->
                Card(Modifier.padding(vertical = 4.dp).fillMaxWidth(), elevation = 2.dp) {
                    Row(Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                        Surface(Modifier.size(32.dp), shape = RoundedCornerShape(16.dp), color = MaterialTheme.colors.primary.copy(alpha = 0.1f)) {
                            Box(contentAlignment = Alignment.Center) { Text(if(t.gender == "F") "👩" else "👨", fontSize = 16.sp) }
                        }
                        Spacer(Modifier.width(12.dp))
                        Column(Modifier.weight(1f)) {
                            Text("${t.firstName} ${t.lastName}", fontWeight = FontWeight.Bold)
                            Text("${t.club} • ${t.country} • Lic: ${t.federateNumber}", style = MaterialTheme.typography.caption)
                        }
                        if (canEdit) {
                            IconButton(onClick = { Repository.datos.tiradores.remove(t); lista = Repository.datos.tiradores.toList() }) {
                                Icon(Icons.Default.Delete, null, tint = Color.Gray, modifier = Modifier.size(20.dp))
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
    var apellidos by remember { mutableStateOf("") }
    var club by remember { mutableStateOf("") }
    var licencia by remember { mutableStateOf("") }
    var edad by remember { mutableStateOf("") }
    var genero by remember { mutableStateOf("M") }
    var pais by remember { mutableStateOf("ESP") }
    var paisExpanded by remember { mutableStateOf(false) }
    
    var isFormVisible by remember { mutableStateOf(false) }
    var lista by remember { mutableStateOf(Repository.datos.arbitros.toList()) }

    Column(Modifier.padding(16.dp).fillMaxSize()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text("Gestión de Árbitros", style = MaterialTheme.typography.h5, fontWeight = FontWeight.Bold)
                Text("${lista.size} técnicos registrados", color = Color.Gray)
            }
            if (canEdit) {
                IconButton(onClick = { isFormVisible = !isFormVisible }) {
                    Icon(
                        if (isFormVisible) Icons.Default.KeyboardArrowUp else Icons.Default.Add,
                        contentDescription = if (isFormVisible) "Ocultar formulario" else "Mostrar formulario",
                        tint = MaterialTheme.colors.primary
                    )
                }
            }
        }
        
        Spacer(Modifier.height(16.dp))

        if (canEdit) {
            AnimatedVisibility(visible = isFormVisible) {
                Card(elevation = 4.dp, shape = RoundedCornerShape(12.dp), modifier = Modifier.padding(bottom = 16.dp)) {
                    Column(Modifier.padding(16.dp)) {
                        Text("Datos del Árbitro", style = MaterialTheme.typography.subtitle2, color = MaterialTheme.colors.primary)
                        Spacer(Modifier.height(12.dp))
                        
                        Row(Modifier.fillMaxWidth()) {
                            OutlinedTextField(value = nombre, onValueChange = { nombre = it }, label = { Text("Nombre") }, modifier = Modifier.weight(1f), singleLine = true)
                            Spacer(Modifier.width(8.dp))
                            OutlinedTextField(value = apellidos, onValueChange = { apellidos = it }, label = { Text("Apellidos") }, modifier = Modifier.weight(1f), singleLine = true)
                        }
                        
                        Spacer(Modifier.height(8.dp))
                        
                        Row(Modifier.fillMaxWidth()) {
                            OutlinedTextField(value = club, onValueChange = { club = it }, label = { Text("Comité / Club") }, modifier = Modifier.weight(1f), singleLine = true)
                            Spacer(Modifier.width(8.dp))
                            OutlinedTextField(value = licencia, onValueChange = { licencia = it }, label = { Text("Nº Licencia") }, modifier = Modifier.weight(1f), singleLine = true)
                        }
                        
                        Spacer(Modifier.height(8.dp))
                        
                        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                            OutlinedTextField(value = edad, onValueChange = { edad = it }, label = { Text("Edad") }, modifier = Modifier.weight(0.4f), singleLine = true)
                            Spacer(Modifier.width(8.dp))
                            
                            Box(modifier = Modifier.weight(0.8f)) {
                                OutlinedButton(
                                    onClick = { paisExpanded = true }, 
                                    modifier = Modifier.fillMaxWidth().height(56.dp).padding(top = 4.dp),
                                    shape = RoundedCornerShape(4.dp),
                                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Black)
                                ) {
                                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                                        Text(pais)
                                        Icon(Icons.Default.ArrowDropDown, null)
                                    }
                                }
                                DropdownMenu(expanded = paisExpanded, onDismissRequest = { paisExpanded = false }) {
                                    LISTA_PAISES.forEach { p -> DropdownMenuItem(onClick = { pais = p; paisExpanded = false }) { Text(p) } }
                                }
                            }
                            
                            Spacer(Modifier.width(12.dp))
                            
                            Column {
                                Text("Género", style = MaterialTheme.typography.caption)
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.clickable { genero = "M" }) {
                                        RadioButton(selected = genero == "M", onClick = { genero = "M" })
                                        Text("M", fontSize = 12.sp)
                                    }
                                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.clickable { genero = "F" }) {
                                        RadioButton(selected = genero == "F", onClick = { genero = "F" })
                                        Text("F", fontSize = 12.sp)
                                    }
                                }
                            }
                        }
                        
                        Spacer(Modifier.height(16.dp))
                        Button(onClick = {
                            if (nombre.isNotBlank() && apellidos.isNotBlank()) {
                                val nuevo = Arbitro(
                                    firstName = nombre, lastName = apellidos, gender = genero, 
                                    age = edad.toIntOrNull() ?: 30, federateNumber = licencia.toIntOrNull() ?: 0, 
                                    club = club, country = pais, modality = listOf(Repository.datos.arma)
                                )
                                Repository.datos.arbitros.add(nuevo)
                                lista = Repository.datos.arbitros.toList()
                                nombre = ""; apellidos = ""; club = ""; licencia = ""; edad = ""
                                isFormVisible = false // Ocultar tras añadir
                            }
                        }, modifier = Modifier.fillMaxWidth().height(48.dp)) {
                            Text("REGISTRAR ÁRBITRO", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        LazyColumn(Modifier.weight(1f)) {
            items(lista) { a ->
                Card(Modifier.padding(vertical = 4.dp).fillMaxWidth(), elevation = 2.dp) {
                    Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                        Text("⚖️", fontSize = 24.sp)
                        Spacer(Modifier.width(16.dp))
                        Column(Modifier.weight(1f)) {
                            Text("${a.firstName} ${a.lastName}", fontWeight = FontWeight.Bold)
                            Text("${a.club} (${a.country}) • Licencia: ${a.federateNumber}", style = MaterialTheme.typography.caption)
                        }
                        if (canEdit) {
                            IconButton(onClick = { Repository.datos.arbitros.remove(a); lista = Repository.datos.arbitros.toList() }) {
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
    var armaSeleccionada by remember { mutableStateOf(Repository.datos.arma) }
    val armas = listOf("Espada", "Florete", "Sable")
    var expanded by remember { mutableStateOf(false) }

    Column(Modifier.padding(16.dp).fillMaxSize().verticalScroll(rememberScrollState())) {
        Text("Configuración de la Competición", style = MaterialTheme.typography.h5, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(16.dp))

        Card(elevation = 4.dp, shape = RoundedCornerShape(12.dp)) {
            Column(Modifier.padding(16.dp)) {
                OutlinedTextField(value = nombre, onValueChange = { nombre = it; Repository.datos.nombre = it }, label = { Text("Nombre del Torneo") }, modifier = Modifier.fillMaxWidth())
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(value = entidad, onValueChange = { entidad = it; Repository.datos.entidadOrganizadora = it }, label = { Text("Entidad") }, modifier = Modifier.fillMaxWidth())
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(value = lugar, onValueChange = { lugar = it; Repository.datos.lugar = it }, label = { Text("Lugar") }, modifier = Modifier.fillMaxWidth())
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(value = fecha, onValueChange = { fecha = it; Repository.datos.fecha = it }, label = { Text("Fecha") }, modifier = Modifier.fillMaxWidth())
                
                Spacer(Modifier.height(16.dp))
                Text("Arma Principal", style = MaterialTheme.typography.subtitle2)
                Box {
                    OutlinedButton(
                        onClick = { expanded = true }, 
                        modifier = Modifier.fillMaxWidth().height(56.dp).padding(top = 4.dp),
                        shape = RoundedCornerShape(4.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Black)
                    ) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(armaSeleccionada)
                            Icon(Icons.Default.ArrowDropDown, null)
                        }
                    }
                    DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                        armas.forEach { arma ->
                            DropdownMenuItem(onClick = { 
                                armaSeleccionada = arma
                                Repository.datos.arma = arma
                                expanded = false 
                            }) { Text(arma) }
                        }
                    }
                }
                Spacer(Modifier.height(24.dp))
                Button(onClick = { Repository.guardar() }, modifier = Modifier.fillMaxWidth().height(48.dp)) { 
                    Text("GUARDAR CAMBIOS", fontWeight = FontWeight.Bold) 
                }
            }
        }
    }
}

@Composable
fun PoulesScreen(isAdmin: Boolean = false) {
    var poules by remember { mutableStateOf(Repository.datos.poules.toList()) }
    var ranking by remember { mutableStateOf(Repository.calcularRanking()) }
    var numPistasInput by remember { mutableStateOf("4") }

    Column(Modifier.padding(16.dp).fillMaxSize().verticalScroll(rememberScrollState())) {
        Text("Fase de Poules", style = MaterialTheme.typography.h5, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(12.dp))

        if (isAdmin) {
            Card(elevation = 4.dp, shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth()) {
                Column(Modifier.padding(16.dp)) {
                    Text("Configuración de Pistas", fontWeight = FontWeight.Bold)
                    Text("El administrador decide el número de grupos según pistas disponibles.", style = MaterialTheme.typography.caption)
                    Spacer(Modifier.height(8.dp))
                    
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        OutlinedTextField(
                            value = numPistasInput, 
                            onValueChange = { numPistasInput = it.filter { char -> char.isDigit() } }, 
                            label = { Text("Número de Pistas") },
                            modifier = Modifier.width(150.dp),
                            singleLine = true
                        )
                        Spacer(Modifier.width(16.dp))
                        Button(
                            onClick = { 
                                val num = numPistasInput.toIntOrNull() ?: 1
                                if (num > 0) {
                                    Repository.generarPoules(num)
                                    poules = Repository.datos.poules.toList()
                                    ranking = Repository.calcularRanking()
                                }
                            }, 
                            modifier = Modifier.fillMaxWidth().height(56.dp)
                        ) { Text("GENERAR POULES") }
                    }
                }
            }
            Spacer(Modifier.height(24.dp))
        }

        Text("Resultados", style = MaterialTheme.typography.h6)
        if (poules.isEmpty()) Text("No hay poules generadas.", color = Color.Gray)
        poules.forEach { p ->
            Card(Modifier.padding(vertical = 8.dp).fillMaxWidth(), elevation = 4.dp) {
                Column(Modifier.padding(16.dp)) {
                    Text("POULE ${p.numero} - ${p.pista}", color = MaterialTheme.colors.primary, fontWeight = FontWeight.Bold)
                    p.asaltos.forEach { asalto -> RowAsalto(asalto, limite = 5) { ranking = Repository.calcularRanking() } }
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
                        Text("${s.tirador.firstName} ${s.tirador.lastName}", Modifier.weight(1f))
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
            Button(onClick = { Repository.generarTablon(); asaltos = Repository.datos.cuadroEliminatorio.toList() }, modifier = Modifier.fillMaxWidth().height(48.dp)) { Text("GENERAR CUADRO FINAL") }
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
        Text("${asalto.tirador1.firstName} ${asalto.tirador1.lastName}", Modifier.weight(1f))
        OutlinedTextField(value = t1, onValueChange = { t1 = it; asalto.tocados1 = it.toIntOrNull() ?: 0; asalto.finalizado = (asalto.tocados1 >= limite || (asalto.tocados2 >= limite)); onUpdate() }, modifier = Modifier.width(65.dp), singleLine = true)
        Text(" - ", Modifier.padding(horizontal = 4.dp))
        OutlinedTextField(value = t2, onValueChange = { t2 = it; asalto.tocados2 = it.toIntOrNull() ?: 0; asalto.finalizado = (asalto.tocados1 >= limite || (asalto.tocados2 >= limite)); onUpdate() }, modifier = Modifier.width(65.dp), singleLine = true)
        Text(if (asalto.tirador2 != null) "${asalto.tirador2.firstName} ${asalto.tirador2.lastName}" else "BYE", Modifier.weight(1f), textAlign = androidx.compose.ui.text.style.TextAlign.End)
    }
}