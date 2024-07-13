package com.example.musicove.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.SecureFlagPolicy

@Composable
fun LoadingDialog(
    isLoading: Boolean,
    onDone: () -> Unit,
    modifier: Modifier = Modifier,
) {

    if (isLoading) {
        Dialog(onDismissRequest = onDone,
            properties = DialogProperties(
                dismissOnBackPress = false,
                dismissOnClickOutside = false,
                securePolicy = SecureFlagPolicy.Inherit,
                usePlatformDefaultWidth = true,
                decorFitsSystemWindows = true
            ),
            content = {
                Box(
                    modifier = modifier,
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.padding(26.dp)
                    )
                }
            }
        )
    }

}