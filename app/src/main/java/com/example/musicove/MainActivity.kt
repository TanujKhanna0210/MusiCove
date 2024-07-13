package com.example.musicove

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Text
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.musicove.ui.components.LoadingDialog
import com.example.musicove.ui.components.TopBar
import com.example.musicove.ui.components.Track
import com.example.musicove.ui.components.WarningMessage
import com.example.musicove.ui.theme.MusiCoveTheme
import com.example.musicove.ui.theme.Pink500
import com.example.musicove.util.audio.isNotEmpty
import com.example.musicove.util.audio.setupPermissions
import com.example.musicove.util.audio.showPermissionRationalDialog
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

            val launcher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.RequestMultiplePermissions(),
                onResult = { permissions ->
                    val permissionsGranted = permissions.values.reduce { acc, next -> acc && next }
                    if (!permissionsGranted) {
                        showPermissionRationalDialog(
                            context = context,
                            okButtonTextResId = R.string.ok_button_text,
                            cancelButtonTextResId = R.string.cancel_button_text,
                            dialogText = getString(R.string.permissions_dialog_text),
                            errorText = getString(R.string.error_while_opening_settings),
                            packageName = packageName
                        )
                    }
                }
            )

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
                    content = {

                        LoadingDialog(
                            isLoading = state.isLoading,
                            onDone = {
                                mainViewModel.onEvent(event = AudioPlayerEvent.HideLoadingDialog)
                            },
                            modifier = Modifier
                                .clip(MaterialTheme.shapes.large)
                                .background(MaterialTheme.colors.surface)
                                .requiredSize(size = 80.dp)
                        )

                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .verticalScroll(state = rememberScrollState())
                                .background(MaterialTheme.colors.onBackground),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {

                            TopBar(
                                modifier = Modifier
                                    .padding(
                                        top = 16.dp,
                                        start = 16.dp,
                                        end = 16.dp
                                    )
                                    .requiredHeight(height = 80.dp),
                                leadingIcon = {
                                    Icon(
                                        painter = painterResource(id = R.drawable.heart_outlined),
                                        contentDescription = "",
                                        tint = Pink500,
                                        modifier = Modifier.requiredSize(size = 26.dp)
                                    )
                                },
                                title = {
                                    if (state.selectedAudio.isNotEmpty()) {

                                        val artist = if (state.selectedAudio.artist.contains(
                                                "unknown",
                                                ignoreCase = true
                                            )
                                        ) ""
                                        else "${state.selectedAudio.artist} - "

                                        Text(
                                            text = buildAnnotatedString {
                                                withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                                                    append(text = artist)
                                                }
                                                append(text = state.selectedAudio.songTitle)
                                            },
                                            color = MaterialTheme.colors.onSurface,
                                            overflow = TextOverflow.Ellipsis,
                                            style = MaterialTheme.typography.h3
                                        )
                                    }
                                },
                                trailingIcon = {
                                    IconButton(onClick = {
                                        // If we have the permission, then open the list of audio files.
                                        setupPermissions(
                                            context = context,
                                            permissions = arrayOf(
                                                Manifest.permission.RECORD_AUDIO,
                                                Manifest.permission.READ_EXTERNAL_STORAGE
                                            ),
                                            launcher = launcher,
                                            onPermissionsGranted = {
                                                scope.launch {
                                                    if (state.audiosList.isEmpty()) {
                                                        mainViewModel.onEvent(event = AudioPlayerEvent.LoadMedias)
                                                    }
                                                    sheetState.show()
                                                }
                                            }
                                        )
                                    }) {
                                        Icon(
                                            painter = painterResource(id = R.drawable.up_right_from_square_solid),
                                            contentDescription = "",
                                            tint = MaterialTheme.colors.onSurface
                                        )
                                    }
                                }
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                        }
                    }
                )
            }
        }
    }
}