package com.example.musicove.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.musicove.R
import com.example.musicove.domain.model.AudioMetadata

@Composable
fun Track(
    audio: AudioMetadata,
    isPlaying: Boolean,
    onClick: (AudioMetadata) -> Unit,
    modifier: Modifier
) {
    Row(
        modifier = modifier
            .fillMaxSize()
            .clickable { onClick(audio) },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier.fillMaxHeight(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                modifier = Modifier
                    .size(size = 60.dp)
                    .padding(5.dp),
                painter = painterResource(id = R.drawable.mp3_logo),
                contentDescription = "",
                contentScale = ContentScale.Crop
            )
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(start = 10.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    modifier = Modifier.padding(3.dp),
                    text = audio.songTitle,
                    fontWeight = FontWeight.Bold,
                    overflow = TextOverflow.Ellipsis,
                    color = if (isPlaying) MaterialTheme.colors.primary
                    else MaterialTheme.colors.onBackground
                )
                Text(
                    text = audio.artist,
                    color = if (isPlaying) MaterialTheme.colors.primary
                    else MaterialTheme.colors.onBackground
                )
            }
        }
        if (isPlaying) {
            Icon(
                modifier = Modifier.padding(end = 8.dp),
                painter = painterResource(id = R.drawable.chart_simple_solid),
                contentDescription = "",
                tint = MaterialTheme.colors.primaryVariant
            )
        }
    }
}