package com.example.lottttto11.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.lottttto11.data.DatabaseProvider
import com.example.lottttto11.viewmodel.StatisticsViewModel
import com.example.lottttto11.viewmodel.StatisticsViewModelFactory

@Composable
fun StatisticsScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    val db = DatabaseProvider.getInstance(context)
    val userId = 1 // TODO: передавать через аргументы навигации

    val statisticsViewModel: StatisticsViewModel = viewModel(
        factory = StatisticsViewModelFactory(userId, db.userDao(), db.walletDao())
    )

    val stats by statisticsViewModel.stats.collectAsState()
    val isLoading by statisticsViewModel.isLoading.collectAsState()

    Scaffold(
        topBar = { TopAppBar(title = { Text("Statistics") }) }
    ) { paddingValues ->
        if (isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
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
                StatCard("Total Hashrate", "${"%.2f".format(stats.totalHashrate)} H/s")
                StatCard("Estimated Earnings", "$${"%.4f".format(stats.estimatedEarnings)}")
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
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = 2.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = title, style = MaterialTheme.typography.body1)
            Text(text = value, style = MaterialTheme.typography.h6)
        }
    }
}
