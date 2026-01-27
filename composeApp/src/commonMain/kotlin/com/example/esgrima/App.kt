package com.example.esgrima

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.esgrima.data.Repository
import com.example.esgrima.ui.*

// Definimos unos colores más "modernos"
private val EsgrimaPrimary = Color(0xFF1A237E) // Azul Noche
private val EsgrimaSecondary = Color(0xFFC5CAE9)

@Composable
fun App() {
    var isLoggedIn by remember { mutableStateOf(false) }
    var isRegisteringTirador by remember { mutableStateOf(false) }
    var screen by remember { mutableStateOf("tiradores") }

    LaunchedEffect(Unit) { Repository.cargar() }

    val colors = lightColors(
        primary = EsgrimaPrimary,
        primaryVariant = Color(0xFF000051),
        secondary = Color(0xFFFFC107) // Un toque de dorado/amarillo para resaltar
    )

    MaterialTheme(colors = colors) {
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
            if (!isLoggedIn && !isRegisteringTirador) {
                // PANTALLA DE BIENVENIDA
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(24.dp)
                    ) {
                        Icon(Icons.Default.Star, null, modifier = Modifier.size(80.dp), tint = EsgrimaPrimary)
                        Spacer(Modifier.height(16.dp))
                        Text("ESGRIMA MANAGER", style = MaterialTheme.typography.h4)
                        Text("Edición 2026", style = MaterialTheme.typography.subtitle1, color = Color.Gray)
                        Spacer(Modifier.height(48.dp))
                        Button(
                            onClick = { isLoggedIn = true }, 
                            modifier = Modifier.fillMaxWidth().height(56.dp),
                            shape = MaterialTheme.shapes.medium
                        ) {
                            Text("ACCESO ADMINISTRADOR")
                        }
                        Spacer(Modifier.height(16.dp))
                        OutlinedButton(
                            onClick = { isRegisteringTirador = true }, 
                            modifier = Modifier.fillMaxWidth().height(56.dp),
                            shape = MaterialTheme.shapes.medium
                        ) {
                            Text("INSCRIPCIÓN PÚBLICA")
                        }
                    }
                }
            } else {
                // APP PRINCIPAL (ADMIN O REGISTRO)
                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = { 
                                Text(if (isRegisteringTirador) "Inscripción" else "Esgrima Admin") 
                            },
                            elevation = 4.dp,
                            actions = {
                                if (isLoggedIn) {
                                    IconButton(onClick = { Repository.guardar() }) {
                                        Icon(Icons.Default.Check, "Guardar")
                                    }
                                    IconButton(onClick = { isLoggedIn = false; screen = "tiradores" }) {
                                        Icon(Icons.Default.ExitToApp, "Salir")
                                    }
                                } else {
                                    IconButton(onClick = { isRegisteringTirador = false }) {
                                        Icon(Icons.Default.Close, "Cerrar")
                                    }
                                }
                            }
                        )
                    },
                    bottomBar = {
                        // Cambiamos NavigationRail por BottomNavigation para móvil
                        if (isLoggedIn) {
                            BottomNavigation(elevation = 8.dp) {
                                BottomNavigationItem(
                                    selected = screen == "tiradores",
                                    onClick = { screen = "tiradores" },
                                    icon = { Icon(Icons.Default.Person, null) },
                                    label = { Text("Tiradores") }
                                )
                                BottomNavigationItem(
                                    selected = screen == "arbitros",
                                    onClick = { screen = "arbitros" },
                                    icon = { Icon(Icons.Default.Face, null) },
                                    label = { Text("Árbitros") }
                                )
                                BottomNavigationItem(
                                    selected = screen == "poules",
                                    onClick = { screen = "poules" },
                                    icon = { Icon(Icons.Default.List, null) },
                                    label = { Text("Poules") }
                                )
                                BottomNavigationItem(
                                    selected = screen == "tablon",
                                    onClick = { screen = "tablon" },
                                    icon = { Icon(Icons.Default.DateRange, null) },
                                    label = { Text("Tablón") }
                                )
                            }
                        }
                    }
                ) { paddingValues ->
                    Box(Modifier.padding(paddingValues).fillMaxSize()) {
                        when {
                            isRegisteringTirador -> TiradoresScreen()
                            screen == "tiradores" -> TiradoresScreen()
                            screen == "arbitros" -> ArbitrosScreen()
                            screen == "poules" -> PoulesScreen()
                            screen == "tablon" -> TablonScreen()
                        }
                    }
                }
            }
        }
    }
}