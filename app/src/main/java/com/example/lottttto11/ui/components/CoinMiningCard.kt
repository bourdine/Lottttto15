package com.example.lottttto11.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.lottttto11.data.Coin
import com.example.lottttto11.viewmodel.MiningViewModel

@Composable
fun CoinMiningCard(
    coin: Coin,
    state: MiningViewModel.MiningState?,
    onStartSolo: () -> Unit,
    onStartPool: () -> Unit,
    onStop: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = 4.dp
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("${coin.displayName} (${coin.symbol})", style = MaterialTheme.typography.h6)

            if (state != null && state.isActive) {
                Text("Mode: ${state.mode}")
                Text("Hashrate: ${"%.2f".format(state.hashrate)} H/s")
                Text("Total Hashes: ${state.totalHashes}")
                Text("Shares: ${state.shares}")
                Text("Blocks: ${state.blocksFound}")
                Text("Temperature: ${state.temperature}°C")
                Spacer(modifier = Modifier.height(8.dp))
                Button(onClick = onStop, modifier = Modifier.fillMaxWidth()) {
                    Text("Stop")
                }
            } else {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(onClick = onStartSolo, modifier = Modifier.weight(1f)) {
                        Text("Solo")
                    }
                    Button(onClick = onStartPool, modifier = Modifier.weight(1f)) {
                        Text("Pool")
                    }
                }
            }
        }
    }
}
