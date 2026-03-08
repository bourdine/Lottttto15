package com.example.lottttto11.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.lottttto11.data.Wallet

@Composable
fun WalletDisplayComponent(
    wallet: Wallet,
    modifier: Modifier = Modifier
) {
    Card(modifier = modifier) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = wallet.name)
            Text(text = "Balance: ${wallet.balance}")
            Text(text = "Address: ${wallet.address.take(10)}...")
        }
    }
}
