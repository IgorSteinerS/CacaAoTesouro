package com.example.caaaotesouro

import android.os.SystemClock
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.*

@Composable
fun PistaScreen(
    pista: String,
    respostaCorreta: String,
    clickVoltar: () -> Unit,
    clickAvancar: () -> Unit
) {
    var resposta by remember { mutableStateOf(TextFieldValue()) }
    var respostaCorretaState by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = pista, fontSize = 24.sp)
        Spacer(modifier = Modifier.height(16.dp))
        TextField(
            value = resposta,
            onValueChange = { resposta = it },
            label = { Text("Digite sua resposta") }
        )
        Spacer(modifier = Modifier.height(16.dp))
        AnimatedVisibility(visible = respostaCorretaState, enter = fadeIn(), exit = fadeOut()) {
            Text("Resposta correta!", color = MaterialTheme.colorScheme.primary, fontSize = 18.sp)
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row {
            Button(onClick = clickVoltar) {
                Text("Voltar")
            }
            Spacer(modifier = Modifier.width(16.dp))
            Button(onClick = {
                if (resposta.text.equals(respostaCorreta, ignoreCase = true)) {
                    respostaCorretaState = true
                    clickAvancar()
                }
            }) {
                Text("Próxima Pista")
            }
        }
    }
}

@Composable
fun Navigation() {
    val navController = rememberNavController()
    val inicioTempo = remember { mutableStateOf(0L) }
    val fimTempo = remember { mutableStateOf(0L) }

    NavHost(navController, startDestination = "home") {
        composable("home") {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Caça ao Tesouro", fontSize = 36.sp)
                Button(onClick = {
                    inicioTempo.value = SystemClock.elapsedRealtime()
                    navController.navigate("pista1")
                }) {
                    Text("Iniciar Caça ao Tesouro", fontSize = 24.sp)
                }
            }
        }

        composable("pista1") {
            PistaScreen("Pista 1: O que tem boca, mas não fala; tem rio, mas não tem água?", "Mapa",
                clickVoltar = { navController.navigate("home") },
                clickAvancar = { navController.navigate("pista2") }
            )
        }
        composable("pista2") {
            PistaScreen("Pista 2: Quanto mais se tira, maior fica?", "Buraco",
                clickVoltar = { navController.navigate("pista1") },
                clickAvancar = { navController.navigate("pista3") }
            )
        }
        composable("pista3") {
            PistaScreen("Pista 3: O que sempre está à sua frente, mas não pode ser visto?", "Futuro",
                clickVoltar = { navController.navigate("pista2") },
                clickAvancar = {
                    fimTempo.value = SystemClock.elapsedRealtime()
                    navController.navigate("tesouro")
                }
            )
        }
        composable("tesouro") {
            val tempoTotal = (fimTempo.value - inicioTempo.value) / 1000 // Segundos
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Parabéns! Você encontrou o tesouro!", fontSize = 30.sp)
                Text("Tempo total: $tempoTotal segundos", fontSize = 18.sp)
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = { navController.navigate("home") }) {
                    Text("Voltar para o início")
                }
            }
        }
    }
}