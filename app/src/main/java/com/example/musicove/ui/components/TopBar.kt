package com.example.musicove.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun TopBar(
    modifier: Modifier = Modifier,
    leadingIcon: @Composable () -> Unit,
    title: (@Composable () -> Unit)? = null,
    trailingIcon: (@Composable () -> Unit)? = null
) {

    Box(modifier = modifier.fillMaxWidth()) {

        Box(modifier = Modifier.align(alignment = Alignment.CenterStart)) {
            leadingIcon()
        }

        Box(modifier = Modifier.align(alignment = Alignment.Center)) {
            title?.invoke()
        }

        Box(modifier = Modifier.align(alignment = Alignment.CenterEnd)) {
            trailingIcon?.invoke()
        }

    }

}