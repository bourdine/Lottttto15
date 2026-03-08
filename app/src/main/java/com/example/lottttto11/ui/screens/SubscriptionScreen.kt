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
import com.example.lottttto11.viewmodel.SubscriptionViewModel
import com.example.lottttto11.viewmodel.SubscriptionViewModelFactory

@Composable
fun SubscriptionScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    val db = DatabaseProvider.getInstance(context)
    val userDao = db.userDao()
    val userId = 1

    val subscriptionViewModel: SubscriptionViewModel = viewModel(
        factory = SubscriptionViewModelFactory(userId, userDao)
    )

    val isActive by subscriptionViewModel.subscriptionActive.collectAsState()
    val isLoading by subscriptionViewModel.isLoading.collectAsState()
    val error by subscriptionViewModel.error.collectAsState()

    Scaffold(topBar = { TopAppBar(title = { Text("Subscription") }) }) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (isActive) {
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("✅ Premium Active", style = MaterialTheme.typography.h5)
                        Text("You keep 100% of block rewards!")
                    }
                }
            } else {
                Text("Get Premium Subscription", style = MaterialTheme.typography.h5)
                Spacer(modifier = Modifier.height(16.dp))
                Text("For only \$2.99/month, you keep 100% of block rewards!\nOtherwise, 10% fee applies.")
                Spacer(modifier = Modifier.height(24.dp))
                Button(
                    onClick = { subscriptionViewModel.startPayment() },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !isLoading
                ) {
                    if (isLoading) CircularProgressIndicator(modifier = Modifier.size(24.dp))
                    else Text("Subscribe with Crypto")
                }
                if (error != null) Text(text = error!!, color = MaterialTheme.colors.error)
            }
            Spacer(modifier = Modifier.weight(1f))
            Button(onClick = onBack, modifier = Modifier.fillMaxWidth()) {
                Text("Back")
            }
        }
    }
}
