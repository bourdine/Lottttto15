package com.example.lottttto11.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import com.example.lottttto11.utils.ZipHelper
import com.example.lottttto11.wallet.WalletGenerator
import kotlinx.coroutines.launch
import java.io.File

@Composable
fun BackupWalletsScreen(
    wallets: List<WalletGenerator.WalletData>,
    onComplete: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var archiveFile by remember { mutableStateOf<File?>(null) }
    var showEmailDialog by remember { mutableStateOf(false) }
    var email by remember { mutableStateOf("") }

    val saveLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("application/zip")
    ) { uri ->
        uri?.let { saveArchiveToUri(archiveFile, it, context) }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("📦  Lottttto", fontSize = 28.sp)
        Spacer(modifier = Modifier.height(16.dp))
        Text("Your wallets have been created!")
        Text("Please back them up now.")
        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {
                scope.launch {
                    ZipHelper.createWalletArchive(context, wallets) { file ->
                        archiveFile = file
                        saveLauncher.launch("wallets_backup.zip")
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("📥  Download wallet archive")
        }
        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = { showEmailDialog = true },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("📧  Send archive to email")
        }

        Spacer(modifier = Modifier.height(24.dp))
        Text("(Archive contains seed phrases, addresses, and QR codes)", fontSize = 12.sp)

        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onComplete, modifier = Modifier.fillMaxWidth()) {
            Text("Continue")
        }
    }

    if (showEmailDialog) {
        AlertDialog(
            onDismissRequest = { showEmailDialog = false },
            title = { Text("Enter your email") },
            text = {
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") }
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        scope.launch {
                            ZipHelper.createWalletArchive(context, wallets) { file ->
                                sendEmailWithAttachment(context, file, email)
                            }
                        }
                        showEmailDialog = false
                    }
                ) { Text("Send") }
            },
            dismissButton = {
                TextButton(onClick = { showEmailDialog = false }) { Text("Cancel") }
            }
        )
    }
}

private fun saveArchiveToUri(file: File?, uri: Uri, context: Context) {
    file?.let {
        context.contentResolver.openOutputStream(uri)?.use { outputStream ->
            file.inputStream().use { inputStream ->
                inputStream.copyTo(outputStream)
            }
        }
    }
}

private fun sendEmailWithAttachment(context: Context, file: File, email: String) {
    val uri = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "application/zip"
        putExtra(Intent.EXTRA_EMAIL, arrayOf(email))
        putExtra(Intent.EXTRA_SUBJECT, "Lottttto Wallet Backup")
        putExtra(Intent.EXTRA_TEXT, "Your wallets are attached. Please store this file securely.")
        putExtra(Intent.EXTRA_STREAM, uri)
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    }
    context.startActivity(Intent.createChooser(intent, "Send archive..."))
}
