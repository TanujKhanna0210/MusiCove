package com.example.musicove.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.musicove.ui.theme.Gray400
import com.example.musicove.ui.theme.onSurface

@Composable
fun FastButton(
    enabled: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    @DrawableRes iconResId: Int
) {
    IconButton(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier
    ) {
        Icon(
            painter = painterResource(id = iconResId),
            contentDescription = "",
            modifier = Modifier.padding(8.dp),
            tint = if (enabled) onSurface else Gray400
        )
    }
}