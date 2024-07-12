package com.example.musicove

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.musicove.ui.components.WarningMessage
import com.example.musicove.ui.theme.MusiCoveTheme
import dagger.hilt.android.AndroidEntryPoint

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
                                // TODO
                            }
                        }
                    },
                    content = {}
                )
            }
        }
    }
}