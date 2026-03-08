package com.example.lottttto11

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.example.lottttto11.ui.screens.*
import com.example.lottttto11.ui.theme.Lottttto11Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Lottttto11Theme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
                    AppNavigator()
                }
            }
        }
    }
}

@Composable
fun AppNavigator() {
    var currentScreen by remember { mutableStateOf(Screen.WELCOME) }
    var currentUserId by remember { mutableStateOf<Int?>(null) }

    when (currentScreen) {
        Screen.WELCOME -> WelcomeScreen(
            onGoogleSignInClick = { /* handle */ },
            onFacebookSignInClick = { /* handle */ },
            onEmailSignInClick = { currentScreen = Screen.LOGIN },
            onEmailSignUpClick = { currentScreen = Screen.REGISTER }
        )
        Screen.LOGIN -> LoginScreen(
            onLoginSuccess = { userId ->
                currentUserId = userId
                currentScreen = Screen.MAIN
            },
            onNavigateToRegister = { currentScreen = Screen.REGISTER }
        )
        Screen.REGISTER -> RegistrationScreen(
            onRegistrationSuccess = { userId ->
                currentUserId = userId
                currentScreen = Screen.BACKUP
            },
            onNavigateToLogin = { currentScreen = Screen.LOGIN },
            onNavigateToAgreement = { currentScreen = Screen.AGREEMENT }
        )
        Screen.BACKUP -> BackupWalletsScreen(
            wallets = remember { generateDummyWallets() },
            onComplete = { currentScreen = Screen.MAIN }
        )
        Screen.MAIN -> MainScreen(
            onNavigateToMining = { currentScreen = Screen.MINING },
            onNavigateToWallet = { currentScreen = Screen.WALLET },
            onNavigateToStatistics = { currentScreen = Screen.STATISTICS },
            onNavigateToSettings = { currentScreen = Screen.SETTINGS },
            onNavigateToSubscription = { currentScreen = Screen.SUBSCRIPTION }
        )
        Screen.MINING -> MiningScreen(onBack = { currentScreen = Screen.MAIN })
        Screen.WALLET -> WalletScreen(onBack = { currentScreen = Screen.MAIN })
        Screen.STATISTICS -> StatisticsScreen(onBack = { currentScreen = Screen.MAIN })
        Screen.SETTINGS -> SettingsScreen(onBack = { currentScreen = Screen.MAIN })
        Screen.AGREEMENT -> AgreementScreen(
            onAgree = { currentScreen = Screen.REGISTER },
            onBack = { currentScreen = Screen.REGISTER }
        )
        Screen.SUBSCRIPTION -> SubscriptionScreen(onBack = { currentScreen = Screen.MAIN })
    }
}

enum class Screen {
    WELCOME, LOGIN, REGISTER, BACKUP, MAIN, MINING, WALLET, STATISTICS, SETTINGS, AGREEMENT, SUBSCRIPTION
}

// Временная заглушка для демонстрации
fun generateDummyWallets(): List<WalletGenerator.WalletData> {
    return listOf(
        WalletGenerator.WalletData("BTC", "bc1...", "seed1"),
        WalletGenerator.WalletData("LTC", "L...", "seed2"),
        WalletGenerator.WalletData("DOGE", "D...", "seed3"),
        WalletGenerator.WalletData("BCH", "bitcoincash:...", "seed4"),
        WalletGenerator.WalletData("XMR", "4...", "seed5")
    )
}
