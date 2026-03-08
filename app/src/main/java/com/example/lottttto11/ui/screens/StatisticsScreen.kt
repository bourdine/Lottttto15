package com.example.lottttto11.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.lottttto11.viewmodel.StatisticsViewModel

@Composable
fun StatisticsScreen(onBack: () -> Unit, viewModel: StatisticsViewModel = viewModel()) {
    val stats by viewModel.stats.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    Scaffold(
        topBar = { TopAppBar(title = { Text("Statistics") }) }
    ) { paddingValues ->
        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp)
            ) {
                StatCard("Total Blocks Found", stats.totalBlocks.toString())
                StatCard("Total Shares", stats.totalShares.toString())
                StatCard("Total Hashrate", "${stats.totalHashrate} H/s")
                StatCard("Estimated Earnings", "$${stats.estimatedEarnings}")
                Spacer(modifier = Modifier.weight(1f))
                Button(onClick = onBack, modifier = Modifier.fillMaxWidth()) {
                    Text("Back")
                }
            }
        }
    }
}

@Composable
fun StatCard(title: String, value: String) {
    Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(title)
            Text(value, style = MaterialTheme.typography.h6)
        }
    }
}
