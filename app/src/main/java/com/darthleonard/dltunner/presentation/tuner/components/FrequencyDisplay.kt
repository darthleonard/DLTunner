package com.darthleonard.dltunner.presentation.tuner.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.darthleonard.dltunner.R

/**
 * Display component showing current note and fundamental frequency in Hz.
 */
@Composable
fun FrequencyDisplay(
    note: String?,
    frequency: Double?,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        val noteText = note ?: stringResource(id = R.string.no_note_placeholder)
        val frequencyText = if (frequency != null && frequency > 0.0) {
            stringResource(id = R.string.frequency_format, frequency)
        } else {
            stringResource(id = R.string.no_frequency_placeholder)
        }

        Text(
            text = noteText,
            style = MaterialTheme.typography.displayLarge.copy(
                fontSize = 72.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 2.sp
            ),
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = frequencyText,
            style = MaterialTheme.typography.titleMedium.copy(
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium
            ),
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
