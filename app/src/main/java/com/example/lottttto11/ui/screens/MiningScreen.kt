package com.example.lottttto11.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.lottttto11.data.Coin
import com.example.lottttto11.data.DatabaseProvider
import com.example.lottttto11.mining.MiningMode
import com.example.lottttto11.ui.components.CoinMiningCard
import com.example.lottttto11.viewmodel.MiningViewModel
import com.example.lottttto11.viewmodel.MiningViewModelFactory

@Composable
fun MiningScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    val db = DatabaseProvider.getInstance(context)
    val walletDao = db.walletDao()
    val userDao = db.userDao()
    val feeDao = db.feeDao()
    val userId = 1 // должно передаваться через аргументы

    val miningViewModel: MiningViewModel = viewModel(
        factory = MiningViewModelFactory(userId, walletDao, userDao, feeDao, context)
    )

    val miningStates by miningViewModel.miningStates.collectAsState()
    val userHasSubscription by miningViewModel.userHasSubscription.collectAsState()

    Scaffold(
        topBar = { TopAppBar(title = { Text("Mining") }) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            Text(
                text = if (userHasSubscription) "✨ Premium User (No Fees)" else "💰 Free User (10% Fee)",
                style = MaterialTheme.typography.h6,
                color = if (userHasSubscription) MaterialTheme.colors.primary else MaterialTheme.colors.error
            )
            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn {
                items(Coin.values().toList()) { coin ->
                    val state = miningStates[coin]
                    CoinMiningCard(
                        coin = coin,
                        state = state,
                        onStartSolo = { miningViewModel.startMining(coin, MiningMode.SOLO, getPoolUrl(coin)) },
                        onStartPool = { miningViewModel.startMining(coin, MiningMode.POOL, getPoolUrl(coin)) },
                        onStop = { miningViewModel.stopMining(coin) }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }

            Spacer(modifier = Modifier.weight(1f))
            Button(onClick = onBack, modifier = Modifier.fillMaxWidth()) {
                Text("Back")
            }
        }
    }
}

fun getPoolUrl(coin: Coin): String {
    return when (coin) {
        Coin.BITCOIN -> "stratum+tcp://btc.viabtc.com:3333"
        Coin.LITECOIN -> "stratum+tcp://ltc.viabtc.com:3333"
        Coin.DOGECOIN -> "stratum+tcp://doge.viabtc.com:3333"
        Coin.BITCOINCASH -> "stratum+tcp://bch.viabtc.com:3333"
        Coin.MONERO -> "stratum+tcp://pool.supportxmr.com:5555"
    }
}
