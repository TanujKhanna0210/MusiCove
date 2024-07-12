package com.example.musicove

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Text
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.musicove.ui.components.Track
import com.example.musicove.ui.components.WarningMessage
import com.example.musicove.ui.theme.MusiCoveTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterialApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val mainViewModel by viewModels<MainViewModel>()

        installSplashScreen().apply {
            setKeepOnScreenCondition {
                mainViewModel.state.isLoading
            }
        }

        setContent {

            val sheetState = rememberModalBottomSheetState(
                initialValue = ModalBottomSheetValue.Hidden,
                skipHalfExpanded = true
            )

            val scope = rememberCoroutineScope()

            val context = LocalContext.current

            val state = mainViewModel.state

            MusiCoveTheme {
                ModalBottomSheetLayout(
                    sheetState = sheetState,
                    sheetContent = {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .verticalScroll(state = rememberScrollState())
                                .padding(top = 16.dp)
                        ) {
                            if (state.audiosList.isEmpty()) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    WarningMessage(
                                        text = getString(R.string.no_media),
                                        iconResId = R.drawable.circle_info_solid,
                                        modifier = Modifier.padding(vertical = 16.dp)
                                    )
                                }
                            } else {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    Text(
                                        modifier = Modifier
                                            .padding(top = 12.dp, bottom = 3.dp),
                                        text = getString(R.string.tracks),
                                        fontWeight = FontWeight.Bold,
                                        style = MaterialTheme.typography.h3,
                                        textDecoration = TextDecoration.Underline,
                                        color = MaterialTheme.colors.onBackground
                                    )
                                }
                                state.audiosList.forEach { audio ->
                                    Track(
                                        audio = audio,
                                        isPlaying = audio.songId == state.selectedAudio.songId,
                                        modifier = Modifier
                                            .padding(horizontal = 8.dp, vertical = 10.dp)
                                            .requiredHeight(height = 100.dp),
                                        onClick = {
                                            scope.launch {
                                                mainViewModel.onEvent(event = AudioPlayerEvent.Stop)
                                                sheetState.hide()
                                                mainViewModel.onEvent(event = AudioPlayerEvent.InitAudio(
                                                    audio = it,
                                                    context = context,
                                                    onAudioInitialized = {
                                                        mainViewModel.onEvent(event = AudioPlayerEvent.Play)
                                                    }
                                                ))
                                            }
                                        }
                                    )
                                    Divider(modifier = Modifier.padding(horizontal = 8.dp))
                                }
                            }
                        }
                    },
                    content = {}
                )
            }
        }
    }
}