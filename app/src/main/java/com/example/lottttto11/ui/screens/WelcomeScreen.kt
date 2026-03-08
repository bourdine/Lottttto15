package com.example.lottttto11.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun WelcomeScreen(
    onGoogleSignInClick: () -> Unit,
    onFacebookSignInClick: () -> Unit,
    onEmailSignInClick: () -> Unit,
    onEmailSignUpClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("🪙 Lottttto", fontSize = 32.sp)
        Spacer(modifier = Modifier.height(16.dp))
        Text("Майнинг 5 монет в одном приложении")
        Spacer(modifier = Modifier.height(32.dp))

        Button(onClick = onGoogleSignInClick, modifier = Modifier.fillMaxWidth()) {
            Text("Login with Google")
        }
        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = onFacebookSignInClick, modifier = Modifier.fillMaxWidth()) {
            Text("Login with Facebook")
        }
        Spacer(modifier = Modifier.height(16.dp))

        Text("or")
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedButton(onClick = onEmailSignUpClick, modifier = Modifier.fillMaxWidth()) {
            Text("Sign up with Email")
        }
        Spacer(modifier = Modifier.height(8.dp))
        TextButton(onClick = onEmailSignInClick) {
            Text("Already have an account? Log in")
        }
    }
}
