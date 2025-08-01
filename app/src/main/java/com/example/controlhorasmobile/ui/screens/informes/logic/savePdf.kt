package com.example.controlhorasmobile.ui.screens.informes.logic

import android.net.Uri
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

@Composable
fun rememberPdfSaver(
    onUriReady: (Uri) -> Unit
): ManagedActivityResultLauncher<String, Uri?> {
    val context = LocalContext.current
    return rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("application/pdf"),
        onResult = { uri ->
            uri?.let { onUriReady(it)}
        }
    )
}