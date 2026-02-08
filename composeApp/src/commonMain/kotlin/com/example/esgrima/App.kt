package com.example.esgrima

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.esgrima.data.Repository
import com.example.esgrima.ui.*

private val EsgrimaPrimary = Color(0xFF1A237E)

@Composable
fun App() {
    var isLoggedIn by remember { mutableStateOf(false) }
    var userRole by remember { mutableStateOf("") } // "ADMIN", "ARBITRO" o "USER"
    var showLoginFields by remember { mutableStateOf(false) }
    
    var user by remember { mutableStateOf("") }
    var pass by remember { mutableStateOf("") }
    var loginError by remember { mutableStateOf(false) }

    var screen by remember { mutableStateOf("tiradores") }

    LaunchedEffect(Unit) { Repository.cargar() }

    MaterialTheme(colors = lightColors(primary = EsgrimaPrimary)) {
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
            
            if (!isLoggedIn) {
                // PANTALLA DE INICIO / LOGIN
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(24.dp).widthIn(max = 400.dp)
                    ) {
                        Icon(Icons.Default.Star, null, modifier = Modifier.size(80.dp), tint = EsgrimaPrimary)
                        Text("ESGRIMA MANAGER", style = MaterialTheme.typography.h4)
                        Spacer(Modifier.height(32.dp))

                        if (!showLoginFields) {
                            Button(onClick = { showLoginFields = true }, modifier = Modifier.fillMaxWidth().height(56.dp)) {
                                Text("ACCESO GESTIÓN")
                            }
                            Spacer(Modifier.height(16.dp))
                            OutlinedButton(onClick = { showLoginFields = true }, modifier = Modifier.fillMaxWidth().height(56.dp)) {
                                Text("LISTA DE TIRADORES")
                            }
                        } else {
                            // CAMPOS DE LOGIN
                            Text("Identifíquese para continuar", style = MaterialTheme.typography.subtitle1)
                            Spacer(Modifier.height(16.dp))
                            
                            OutlinedTextField(
                                value = user, 
                                onValueChange = { user = it; loginError = false }, 
                                label = { Text("Usuario") },
                                modifier = Modifier.fillMaxWidth()
                            )
                            Spacer(Modifier.height(8.dp))
                            OutlinedTextField(
                                value = pass, 
                                onValueChange = { pass = it; loginError = false }, 
                                label = { Text("Contraseña") },
                                visualTransformation = PasswordVisualTransformation(),
                                modifier = Modifier.fillMaxWidth()
                            )
                            
                            if (loginError) {
                                Text("Credenciales incorrectas", color = Color.Red, style = MaterialTheme.typography.caption)
                            }
                            
                            Spacer(Modifier.height(16.dp))
                            Button(
                                onClick = { 
                                    if (user == "admin" && pass == "admin") {
                                        isLoggedIn = true
                                        userRole = "ADMIN"
                                        showLoginFields = false
                                    } else if (user == "arbitro" && pass == "arbitro") {
                                        isLoggedIn = true
                                        userRole = "ARBITRO"
                                        showLoginFields = false
                                    } else if (user == "user" && pass == "user") {
                                        isLoggedIn = true
                                        userRole = "USER"
                                        showLoginFields = false
                                    } else {
                                        loginError = true
                                    }
                                }, 
                                modifier = Modifier.fillMaxWidth().height(56.dp)
                            ) {
                                Text("ACCEDER")
                            }
                            TextButton(onClick = { showLoginFields = false }) {
                                Text("Volver")
                            }
                            
                            Spacer(Modifier.height(16.dp))
                            Text("Consultas: user / user", style = MaterialTheme.typography.caption, color = Color.Gray)
                            Text("Gestión: admin / admin o arbitro / arbitro", style = MaterialTheme.typography.caption, color = Color.Gray)
                        }
                    }
                }
            } else {
                // APP PRINCIPAL
                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = { 
                                val titulo = when (userRole) {
                                    "ADMIN" -> "Esgrima Admin"
                                    "ARBITRO" -> "Esgrima Árbitro"
                                    else -> "Lista de Tiradores"
                                }
                                Text(titulo) 
                            },
                            actions = {
                                if (userRole == "ADMIN") {
                                    IconButton(onClick = { Repository.guardar() }) { Icon(Icons.Default.Check, null) }
                                }
                                IconButton(onClick = { 
                                    isLoggedIn = false
                                    user = ""; pass = ""
                                    userRole = ""
                                    screen = "tiradores" 
                                }) { Icon(Icons.Default.ExitToApp, null) }
                            }
                        )
                    },
                    bottomBar = {
                        if (userRole != "USER") {
                            BottomNavigation {
                                BottomNavigationItem(selected = screen == "tiradores", onClick = { screen = "tiradores" }, icon = { Icon(Icons.Default.Person, null) }, label = { Text("Tiradores") })
                                BottomNavigationItem(selected = screen == "arbitros", onClick = { screen = "arbitros" }, icon = { Icon(Icons.Default.Face, null) }, label = { Text("Árbitros") })
                                BottomNavigationItem(selected = screen == "poules", onClick = { screen = "poules" }, icon = { Icon(Icons.Default.List, null) }, label = { Text("Poules") })
                                BottomNavigationItem(selected = screen == "tablon", onClick = { screen = "tablon" }, icon = { Icon(Icons.Default.DateRange, null) }, label = { Text("Tablón") })
                                
                                if (userRole == "ADMIN") {
                                    BottomNavigationItem(selected = screen == "ajustes", onClick = { screen = "ajustes" }, icon = { Icon(Icons.Default.Settings, null) }, label = { Text("Ajustes") })
                                }
                            }
                        }
                    }
                ) { padding ->
                    Box(Modifier.padding(padding).fillMaxSize()) {
                        when {
                            screen == "tiradores" -> TiradoresScreen(canEdit = (userRole == "ADMIN"))
                            screen == "arbitros" -> ArbitrosScreen(canEdit = (userRole == "ADMIN"))
                            screen == "poules" -> PoulesScreen(isAdmin = (userRole == "ADMIN"))
                            screen == "tablon" -> TablonScreen(isAdmin = (userRole == "ADMIN"))
                            screen == "ajustes" && userRole == "ADMIN" -> AjustesScreen()
                        }
                    }
                }
            }
        }
    }
}