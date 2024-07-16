package com.example.musicove.ui.components

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.musicove.R
import com.example.musicove.ui.theme.Pink500

@Composable
fun LikeButton(
    isFavorite: Boolean,
    enabled: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    buttonSize: Dp = 26.dp
) {

    val transition = updateTransition(targetState = isFavorite, label = "transition")

    val animatedSize by transition.animateDp(
        transitionSpec = {
            keyframes {
                durationMillis = 500
                buttonSize + 10.dp at 250 with LinearOutSlowInEasing
                buttonSize at 400 with LinearOutSlowInEasing
            }
        },
        label = "animatedSize",
        targetValueByState = { state ->
            if (state) buttonSize else buttonSize
        }
    )

    val shouldBeAnimated = remember { mutableStateOf(false) }

    Icon(
        painter = painterResource(id = if (isFavorite) R.drawable.heart_solid else R.drawable.heart_outlined),
        contentDescription = "",
        tint = if (enabled) Pink500 else MaterialTheme.colors.onSurface,
        modifier = modifier
            .clip(shape = RoundedCornerShape(size = 100.dp))
            .padding(8.dp)
            .clickable(
                enabled = enabled,
                indication = rememberRipple(bounded = false),
                interactionSource = remember { MutableInteractionSource() },
                role = Role.Button,
                onClick = {
                    shouldBeAnimated.value = true
                    onClick()
                }
            )
            .size(size = if (shouldBeAnimated.value) animatedSize else buttonSize)
    )

}