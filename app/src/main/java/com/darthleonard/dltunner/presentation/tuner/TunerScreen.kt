package com.darthleonard.dltunner.presentation.tuner

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.darthleonard.dltunner.R
import com.darthleonard.dltunner.presentation.tuner.components.FrequencyDisplay
import com.darthleonard.dltunner.presentation.tuner.components.PitchMeter
import com.darthleonard.dltunner.presentation.tuner.components.StringIndicators
import com.darthleonard.dltunner.presentation.tuner.components.TunerStatus
import com.darthleonard.dltunner.presentation.tuner.components.TuningSelector

/**
 * Single-screen professional guitar tuner composable.
 */
@Composable
fun TunerScreen(
    modifier: Modifier = Modifier,
    viewModel: TunerViewModel = viewModel(),
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        viewModel.onPermissionResult(isGranted)
    }

    LaunchedEffect(Unit) {
        val hasPermission = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.RECORD_AUDIO
        ) == PackageManager.PERMISSION_GRANTED

        viewModel.onPermissionResult(hasPermission)
        if (!hasPermission) {
            permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            viewModel.stopListening()
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 24.dp, vertical = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            if (!uiState.isPermissionGranted) {
                // Permission Request Card
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp)
                ) {
                    Text(
                        text = stringResource(id = R.string.permission_title),
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = MaterialTheme.colorScheme.onBackground,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = stringResource(id = R.string.permission_rationale),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(28.dp))

                    Button(
                        onClick = { permissionLauncher.launch(Manifest.permission.RECORD_AUDIO) },
                        shape = RoundedCornerShape(24.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        ),
                        modifier = Modifier
                            .fillMaxWidth(0.8f)
                            .height(50.dp)
                    ) {
                        Text(
                            text = stringResource(id = R.string.permission_grant_button),
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                }
            } else {
                // Main Tuner Interface
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxSize()
                ) {
                    // Header Title
                    Text(
                        text = stringResource(id = R.string.app_name),
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 2.sp
                        ),
                        color = MaterialTheme.colorScheme.onBackground
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Tuning Selector Preset
                    TuningSelector(
                        selectedTuning = uiState.selectedTuning,
                        availableTunings = uiState.availableTunings,
                        onTuningSelected = { tuning ->
                            viewModel.selectTuning(tuning)
                        }
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // String Indicators (6 to 1)
                    StringIndicators(
                        strings = uiState.selectedTuning.strings,
                        activeString = uiState.detectedString
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    // Target Note & Precise Frequency Display
                    FrequencyDisplay(
                        note = uiState.detectedNote,
                        frequency = uiState.frequency
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    // Status Banner (MUY BAJA / AFINADA / MUY ALTA / Sin señal)
                    TunerStatus(
                        state = uiState.state
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Pitch Gauge Meter Needle
                    PitchMeter(
                        cents = uiState.cents,
                        state = uiState.state
                    )

                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}
