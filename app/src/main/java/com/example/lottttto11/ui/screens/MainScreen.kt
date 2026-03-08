package com.example.lottttto11.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun MainScreen(
    onNavigateToMining: () -> Unit,
    onNavigateToWallet: () -> Unit,
    onNavigateToStatistics: () -> Unit,
    onNavigateToSettings: () -> Unit,
    onNavigateToSubscription: () -> Unit
) {
    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Lottttto", style = MaterialTheme.typography.h3)
            Spacer(modifier = Modifier.height(32.dp))
            Button(onClick = onNavigateToMining, modifier = Modifier.fillMaxWidth()) {
                Text("Mining")
            }
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = onNavigateToWallet, modifier = Modifier.fillMaxWidth()) {
                Text("Wallet")
            }
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = onNavigateToStatistics, modifier = Modifier.fillMaxWidth()) {
                Text("Statistics")
            }
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = onNavigateToSettings, modifier = Modifier.fillMaxWidth()) {
                Text("Settings")
            }
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = onNavigateToSubscription, modifier = Modifier.fillMaxWidth()) {
                Text("Subscription")
            }
        }
    }
}
