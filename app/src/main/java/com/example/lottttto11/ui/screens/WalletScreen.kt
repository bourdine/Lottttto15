package com.example.lottttto11.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.lottttto11.data.DatabaseProvider
import com.example.lottttto11.ui.components.WalletDisplayComponent
import com.example.lottttto11.viewmodel.WalletViewModel
import com.example.lottttto11.viewmodel.WalletViewModelFactory

@Composable
fun WalletScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    val db = DatabaseProvider.getInstance(context)
    val walletDao = db.walletDao()
    val userId = 1

    val walletViewModel: WalletViewModel = viewModel(
        factory = WalletViewModelFactory(userId, walletDao)
    )

    val wallets by walletViewModel.wallets.collectAsState()
    val isLoading by walletViewModel.isLoading.collectAsState()

    Scaffold(
        topBar = { TopAppBar(title = { Text("Wallets") }) },
        floatingActionButton = {
            FloatingActionButton(onClick = { walletViewModel.createWallet("BTC", "My Wallet") }) {
                Text("+")
            }
        }
    ) { paddingValues ->
        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(wallets) { wallet ->
                    WalletDisplayComponent(wallet = wallet)
                }
            }
        }
    }
}
